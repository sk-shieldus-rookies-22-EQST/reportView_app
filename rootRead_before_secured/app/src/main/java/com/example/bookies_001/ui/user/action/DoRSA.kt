package com.example.bookies_001.ui.user.action

import android.util.Log
import com.example.bookies_001.model.kms.GetKeyRequest
import com.example.bookies_001.repository.KmsRepository
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Base64
import javax.crypto.Cipher

class DoRSA(
    private val kmsRepository: KmsRepository,
    private val verify_url : String
) {
    private var aesKey: String? = null
    private var aesIv: String? = null
    private var privateKey: RSAPrivateKey? = null
    private var isKeyLoaded = false
    private val keyLoadListeners = mutableListOf<(String?, String?) -> Unit>()

    /**
     * RSA 2048비트 키 생성 및 개인키 저장
     */
    private fun generateRSAKeyPair(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048)
        val keyPair = keyPairGenerator.generateKeyPair()
        privateKey = keyPair.private as RSAPrivateKey // 개인키 저장
        return keyPair
    }

    /**
     * 공개키를 PEM 형식으로 변환
     */
    private fun convertPublicKeyToPEM(publicKey: RSAPublicKey): String {
        val base64PublicKey = Base64.getEncoder().encodeToString(publicKey.encoded)

        // 64자 단위로 줄바꿈 적용
        val formattedKey = base64PublicKey.chunked(64).joinToString("\n")

        return "-----BEGIN PUBLIC KEY-----\n$formattedKey\n-----END PUBLIC KEY-----"
    }

    /**
     * RSA 개인키로 Base64 인코딩된 데이터를 복호화
     */
    private fun decryptWithPrivateKey(encryptedData: String): String {
        return try {
            val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding")
            cipher.init(Cipher.DECRYPT_MODE, privateKey)

            val decodedData = Base64.getDecoder().decode(encryptedData) // Base64 디코딩
            val decryptedBytes = cipher.doFinal(decodedData) // RSA 복호화
            String(decryptedBytes, Charsets.UTF_8) // 문자열 변환
        } catch (e: Exception) {
            Log.e("DoRSA", "RSA 복호화 실패: ${e.message}")
            ""
        }
    }

    /**
     * 서버에서 AES 키 요청 및 복호화
     */
    private fun fileOpenKey() {
        val rsaKeyPair = generateRSAKeyPair()
        val publicKey = rsaKeyPair.public as RSAPublicKey
        val publicKeyPEM = convertPublicKeyToPEM(publicKey)

        // 서버로 보낼 JSON 객체 생성
        val publicKeyData = GetKeyRequest(
            rsa_public_key = publicKeyPEM,
            verify_url = verify_url
        )

        // 실제 전송 JSON 로그 출력 (디버깅)
        Log.d("DoRSA", "전송되는 공개키: $publicKeyPEM")

        kmsRepository.getkey(publicKeyData) { response, error ->
            if (response != null) {
                aesKey = decryptWithPrivateKey(response.aes_key)
                aesIv = decryptWithPrivateKey(response.aes_iv)
                isKeyLoaded = true

                // 🔹 키가 로드되었으므로 대기 중이던 콜백 실행
                keyLoadListeners.forEach { listener -> listener(aesKey, aesIv) }
                keyLoadListeners.clear()
            } else {
                Log.e("DoRSA", "AES 키 요청 실패: ${error?.message}")
            }
        }
    }

    init {
        fileOpenKey()
    }

    fun getKeysAsync(callback: (String?, String?) -> Unit) {
        if (aesKey != null && aesIv != null) {
            callback(aesKey, aesIv)
        } else {
            keyLoadListeners.add(callback)
        }
    }

    fun close() {
        aesKey = null
        aesIv = null
    }
}
