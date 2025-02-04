package com.example.bookies_001.utils

import android.content.Context
import android.util.Base64
import com.example.bookies_001.R
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

// 앱에서 단 하나의 인스턴스를 가짐
object AESUtil {
    private const val ALGORITHM = "AES"
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"

    private lateinit var appContext : Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // lazy를 사용하여 리소스에서 값을 불러옵니다.
    // lazy { ... } 처음으로 사용될 때 블럭 내의 코드를 동작하도록 함 이후에는 캐시된 값을 재사용 함
    // 반드시 init 함수로 초기화 한 이후에 호출 되어야 함
    val key: String by lazy {
        appContext.getString(R.string.aes_key)
    }

    val iv: String by lazy {
        appContext.getString(R.string.aes_iv)
    }

    // 만약 키나 IV가 16바이트가 아닐 경우 16바이트로 맞추기 위한 헬퍼 함수
    private fun padTo16Bytes(input: ByteArray): ByteArray {
        return if (input.size >= 16) {
            input.copyOf(16)
        } else {
            ByteArray(16).also { padded -> input.copyInto(padded) }
        }
    }

    // keySpec와 ivSpec도 lazy로 선언하여, appContext 초기화 이후에 계산되도록 함.
    private val keySpec: SecretKeySpec by lazy {
        val rawKey = key
        val keyBytes = padTo16Bytes(rawKey.toByteArray(Charsets.UTF_8))
        SecretKeySpec(keyBytes, ALGORITHM)
    }

    private val ivSpec: IvParameterSpec by lazy {
        val rawIv = iv
        val ivBytes = padTo16Bytes(rawIv.toByteArray(Charsets.UTF_8))
        IvParameterSpec(ivBytes)
    }

    //  평문(plainText)을 AES로 암호화하고, 암호문을 Base64 문자열로 반환합니다.
    fun encrypt(plainText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
        // 평문을 UTF-8 바이트 배열로 변환한 후 암호화
        val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
        // 암호문 바이트 배열을 Base64 인코딩하여 문자열로 반환
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    //  암호화된 Base64 문자열(cipherText)을 복호화하여 평문 문자열로 반환합니다.
    fun decrypt(cipherText: String): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
        // 암호문 Base64 문자열을 디코딩하여 바이트 배열 생성
        val decryptedBytes = cipher.doFinal(Base64.decode(cipherText, Base64.NO_WRAP))
        // 복호화된 바이트 배열을 UTF-8 문자열로 변환하여 반환
        return String(decryptedBytes, Charsets.UTF_8)
    }
}
