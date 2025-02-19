package com.example.bookies_001.utils

import android.util.Log
import com.example.bookies_001.model.kms.GetKeyRequest
import com.example.bookies_001.repository.KmsRepository
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Base64
import javax.crypto.Cipher

object DoRSAUtils {
    private var aesKey: String? = null
    private var aesIv: String? = null
    private var privateKey: RSAPrivateKey? = null
    private var isInitialized = false
    private val keyLoadListeners = mutableListOf<(ByteArray?, ByteArray?) -> Unit>()
    private lateinit var kmsRepository: KmsRepository

    /**
     * `kmsRepository` 설정 (외부에서 주입)
     */
    fun initialize(repository: KmsRepository) {
        if (isInitialized) return // 🔹 이미 초기화되었으면 실행하지 않음

        kmsRepository = repository
        isInitialized = true
    }

    fun isInitialized(): Boolean {
        return isInitialized
    }

    /**
     * RSA 2048비트 키 쌍을 한 번만 생성
     */
    private val rsaKeyPair: KeyPair by lazy {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048)
        keyPairGenerator.generateKeyPair().also {
            privateKey = it.private as RSAPrivateKey
        }
    }

    /**
     * 공개키를 PEM 형식으로 변환
     */
    private fun convertPublicKeyToPEM(publicKey: RSAPublicKey): String {
        val base64PublicKey = Base64.getEncoder().encodeToString(publicKey.encoded)
        return "-----BEGIN PUBLIC KEY-----\n" +
                base64PublicKey.chunked(64).joinToString("\n") +
                "\n-----END PUBLIC KEY-----"
    }

    /**
     * RSA 개인키로 Base64 인코딩된 데이터를 복호화
     */
    private fun decryptWithPrivateKey(encryptedData: String): String {
        return try {
            val cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding")
            cipher.init(Cipher.DECRYPT_MODE, privateKey)
            val decodedData = Base64.getDecoder().decode(encryptedData)
            String(cipher.doFinal(decodedData), Charsets.UTF_8)
        } catch (e: Exception) {
            Log.e("DoRSAUtils", "RSA 복호화 실패: ${e.message}")
            ""
        }
    }

    /**
     * 서버에서 AES 키 요청 및 복호화 (키가 필요할 때만 호출)
     */
    private fun requestAESKey() {
        val publicKey = rsaKeyPair.public as RSAPublicKey
        val publicKeyPEM = convertPublicKeyToPEM(publicKey)

        val publicKeyData = GetKeyRequest(rsa_public_key = publicKeyPEM)

        Log.d("DoRSAUtils", "전송되는 공개키: $publicKeyPEM")

        kmsRepository.getkey(publicKeyData) { response, error ->
            if (response != null) {
                aesKey = decryptWithPrivateKey(response.aes_key)
                aesIv = decryptWithPrivateKey(response.aes_iv)

                keyLoadListeners.forEach { listener ->
                    listener(Base64.getDecoder().decode(aesKey), Base64.getDecoder().decode(aesIv))
                }
                keyLoadListeners.clear()
            } else {
                Log.e("DoRSAUtils", "AES 키 요청 실패: ${error?.message}")
            }
        }
    }

    /**
     * 비동기적으로 AES Key & IV를 가져옴 (Base64 디코딩 후 반환, 필요할 때 요청)
     */
    fun getKeysAsync(callback: (ByteArray?, ByteArray?) -> Unit) {
        if (aesKey != null && aesIv != null) {
            // 🔹 이미 키가 있으면 바로 반환
            callback(Base64.getDecoder().decode(aesKey), Base64.getDecoder().decode(aesIv))
        } else {
            // 🔹 키가 없으면 서버에 요청 후 반환
            keyLoadListeners.add(callback)
            requestAESKey()
        }
    }

    /**
     * AES 키 삭제 (로그아웃 시 호출)
     */
    fun clearKeys() {
        aesKey = null
        aesIv = null
    }
}

