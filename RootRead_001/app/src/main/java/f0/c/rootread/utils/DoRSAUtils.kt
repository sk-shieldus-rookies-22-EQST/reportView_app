package f0.c.rootread.utils

import android.util.Log
import f0.c.rootread.model.kms.GetKeyRequest
import f0.c.rootread.model.kms.MobileKeyRequest
import f0.c.rootread.repository.KmsRepository
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.Base64
import javax.crypto.Cipher

class DoRSAUtils(private val kmsRepository: KmsRepository, keyword:String) {
    private var aesKey: String? = null
    private var aesIv: String? = null
    private var privateKey: RSAPrivateKey? = null
    private var rsaKeyPair: KeyPair? = null // 🔹 nullable로 변경하여 초기화 시점 조정
    private val keyLoadListeners = mutableListOf<(ByteArray?, ByteArray?) -> Unit>()

    init {
        initializeRSAKey() // 🔹 객체가 생성될 때 RSA 키 생성
        if (keyword == "login") {
            loginAESKey() // 🔹 로그인을 위한 AES 키 요청
        } else if (keyword == "decrypt") {
            decryptAESKey() // 🔹 복호화를 위한 AES 키 요청
        }
    }

    /**
     * RSA 2048비트 키 쌍을 미리 생성
     */
    private fun initializeRSAKey() {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048)
        rsaKeyPair = keyPairGenerator.generateKeyPair().also {
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
     * 로그인 AES 키 요청 및 복호화
     */
    private fun loginAESKey() {
        if (rsaKeyPair == null) {
            Log.e("DoRSAUtils", "RSA 키가 초기화되지 않음")
            return
        }

        val publicKey = rsaKeyPair!!.public as RSAPublicKey
        val publicKeyPEM = convertPublicKeyToPEM(publicKey)

        val publicKeyData = MobileKeyRequest(rsa_public_key = publicKeyPEM)

        Log.d("DoRSAUtils", "전송되는 공개키: $publicKeyPEM")

        kmsRepository.mobileKey(publicKeyData) { response, error ->
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
     * 웹툰 복호화 AES 키 요청 및 복호화
     */
    private fun decryptAESKey() {
        if (rsaKeyPair == null) {
            Log.e("DoRSAUtils", "RSA 키가 초기화되지 않음")
            return
        }

        val publicKey = rsaKeyPair!!.public as RSAPublicKey
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
     * 비동기적으로 AES Key & IV를 가져옴 (Base64 디코딩 후 반환)
     */
    fun getKeysAsync(callback: (ByteArray?, ByteArray?) -> Unit) {
        if (aesKey != null && aesIv != null) {
            callback(Base64.getDecoder().decode(aesKey), Base64.getDecoder().decode(aesIv))
        } else {
            keyLoadListeners.add(callback)
        }
    }

    /**
     * AES 키 삭제 (객체 사용 후 호출해야 함)
     */
    fun clearKeys() {
        aesKey = null
        aesIv = null
        privateKey = null
        rsaKeyPair = null // 🔹 RSA 키도 초기화하여 메모리 해제
    }
}
