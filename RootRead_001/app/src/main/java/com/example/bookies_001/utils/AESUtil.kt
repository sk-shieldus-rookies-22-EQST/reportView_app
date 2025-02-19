package com.example.bookies_001.utils

import android.app.ProgressDialog
import android.content.Context
import android.util.Base64
import android.util.Log
import com.example.bookies_001.App
import com.example.bookies_001.R
import com.example.bookies_001.api.KMSAPI
import com.example.bookies_001.repository.KmsRepository
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

// 앱에서 단 하나의 인스턴스를 가짐
object AESUtil {
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"

    // 만약 키나 IV가 16바이트가 아닐 경우 16바이트로 맞추기 위한 헬퍼 함수
    private fun padTo16Bytes(input: ByteArray): ByteArray {
        return if (input.size >= 16) {
            input.copyOf(16)
        } else {
            ByteArray(16).also { padded -> input.copyInto(padded) }
        }
    }


    //  평문(plainText)을 AES로 암호화하고, 암호문을 Base64 문자열로 반환합니다.
    fun encrypt(plainText: String, kmsRepository: KmsRepository, callback: (String?) -> Unit) {
        val doRSAUtils = DoRSAUtils(kmsRepository)
        doRSAUtils.getKeysAsync { aesKey, aesIv ->
            if (aesKey == null || aesIv == null) {
                Log.e("AESUtil", "AES Key 또는 IV를 가져오는 데 실패했습니다.")
                callback(null)
                return@getKeysAsync
            }

            try {
                val cipher = Cipher.getInstance(TRANSFORMATION)
                val keySpec = SecretKeySpec(padTo16Bytes(aesKey), ALGORITHM)
                val ivSpec = IvParameterSpec(padTo16Bytes(aesIv))

                cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

                val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
                val encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)

                Log.d("AESUtil", "Encrypted Data: $encryptedBase64")
                callback(encryptedBase64)
            } catch (e: Exception) {
                Log.e("AESUtil", "AES 암호화 실패: ${e.message}")
                callback(null)
            } finally {
                doRSAUtils.clearKeys()
            }
        }
    }

    //  암호화된 Base64 문자열(cipherText)을 복호화하여 평문 문자열로 반환합니다.
    fun decrypt(cipherText: String, kmsRepository: KmsRepository, callback: (String?) -> Unit) {
        val doRSAUtils = DoRSAUtils(kmsRepository)
        doRSAUtils.getKeysAsync { aesKey, aesIv ->
            if (aesKey == null || aesIv == null) {
                Log.e("AESUtil", "AES Key 또는 IV를 가져오는 데 실패했습니다.")
                callback(null)
                return@getKeysAsync
            }

            try {
                val cipher = Cipher.getInstance(TRANSFORMATION)
                val keySpec = SecretKeySpec(padTo16Bytes(aesKey), ALGORITHM)
                val ivSpec = IvParameterSpec(padTo16Bytes(aesIv))

                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

                val decryptedBytes = cipher.doFinal(Base64.decode(cipherText, Base64.NO_WRAP))
                val decryptedText = String(decryptedBytes, Charsets.UTF_8)

                Log.d("AESUtil", "Decrypted Data: $decryptedText")
                callback(decryptedText)
            } catch (e: Exception) {
                Log.e("AESUtil", "AES 복호화 실패: ${e.message}")
                callback(null)
            } finally {
                doRSAUtils.clearKeys()
            }
        }
    }
}
