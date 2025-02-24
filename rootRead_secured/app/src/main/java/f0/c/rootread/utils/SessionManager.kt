package f0.c.rootread.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import android.util.Base64

object SessionManager {
    private const val PREF_NAME = "secure_pref"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_USER_TOKEN = "user_token"
    private const val USER_LEVEL = "user_level"
    private const val AUTO_LOGIN = "auto_login"
    private const val REMEMBER_ID = "remember_id"

    private const val KEY_ALIAS = "secure_key"
    private const val ANDROID_KEYSTORE = "AndroidKeyStore"

    /**
     * Android Keystore에서 암호화 키 가져오기
     */
    private fun getSecretKey(): SecretKey {
        val keyStore = java.security.KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        return keyStore.getKey(KEY_ALIAS, null) as? SecretKey ?: run {
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE)
            keyGenerator.init(
                KeyGenParameterSpec.Builder(KEY_ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setUserAuthenticationRequired(false)
                    .build()
            )
            keyGenerator.generateKey()
        }
    }


    /**
     * AES-GCM을 사용하여 데이터 암호화
     */
    private fun encryptData(data: String): String {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey())
        val iv = cipher.iv
        val encryptedData = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(iv + encryptedData, Base64.DEFAULT)
    }

    /**
     * AES-GCM을 사용하여 데이터 복호화
     */
    private fun decryptData(encryptedData: String): String {
        val decodedData = Base64.decode(encryptedData, Base64.DEFAULT)
        val iv = decodedData.copyOfRange(0, 12)
        val encryptedBytes = decodedData.copyOfRange(12, decodedData.size)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, iv))
        return String(cipher.doFinal(encryptedBytes), Charsets.UTF_8)
    }

    /**
     * EncryptedSharedPreferences 사용
     */
    private fun getPreferences(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            PREF_NAME,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    /**
     * 사용자 로그인 세션 저장 (암호화 적용)
     */
    fun saveLoginSession(context: Context, userId: String) {
        val encryptedUserId = encryptData(userId) // 사용자 ID 암호화
        val editor = getPreferences(context).edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_ID, encryptedUserId)
        editor.apply()
    }

    /**
     * 사용자 레벨 저장
     */
    fun saveToken(context: Context, token: String) {
        var encrypteToken = encryptData(token)
        val editor = getPreferences(context).edit()
        editor.putString(KEY_USER_TOKEN,encrypteToken)
        editor.apply()
    }

    /**
     * 사용자 레벨 저장
     */
    fun saveUserLevel(context: Context, userLevel: Int) {
        val editor = getPreferences(context).edit()
        editor.putInt(USER_LEVEL, userLevel)
        editor.apply()
    }

    /**
     * 자동 로그인 설정 저장
     */
    fun saveAutoLogin(context: Context, autoLogin: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(AUTO_LOGIN, autoLogin)
        editor.apply()
    }

    /**
     * ID 저장 여부 설정
     */
    fun saveRememberID(context: Context, rememberID: Boolean) {
        val editor = getPreferences(context).edit()
        editor.putBoolean(REMEMBER_ID, rememberID)
        editor.apply()
    }

    /**
     * 암호화된 사용자 ID 가져오기
     */
    fun getUserID(context: Context): String? {
        val sharedPreferences = getPreferences(context)
        val encryptedUserId = sharedPreferences.getString(KEY_USER_ID, null) ?: return null
        return try {
            decryptData(encryptedUserId) // 복호화
        } catch (e: Exception) {
            null
        }
    }

    fun getUserToken(context: Context): String? {
        val sharedPreferences = getPreferences(context)
        val encryptedToken = sharedPreferences.getString(KEY_USER_TOKEN, null) ?: return null
        return try {
            decryptData(encryptedToken)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * 사용자 레벨 가져오기
     */
    fun getUserLevel(context: Context): Int {
        val sharedPreferences = getPreferences(context)
        return sharedPreferences.getInt(USER_LEVEL, -1)
    }

    /**
     * 자동 로그인 여부 확인
     */
    fun isAutoLogin(context: Context): Boolean {
        return getPreferences(context).getBoolean(AUTO_LOGIN, false)
    }

    /**
     * ID 저장 여부 확인
     */
    fun isRememberID(context: Context): Boolean {
        return getPreferences(context).getBoolean(REMEMBER_ID, false)
    }

    /**
     * 로그인 상태 확인
     */
    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * 사용자 세션 초기화 (보안 유지)
     */
    fun clearSession(context: Context) {
        val editor = getPreferences(context).edit()

        val autoLogin = isAutoLogin(context)
        val rememberID = isRememberID(context)
        val userID = getUserID(context)
        val userLevel = getUserLevel(context)

        editor.clear() // 🔥 모든 데이터 삭제

        editor.putBoolean(AUTO_LOGIN, autoLogin)
        editor.putBoolean(REMEMBER_ID, rememberID)

        if (!rememberID) {
            editor.remove(KEY_USER_ID)
            editor.remove(USER_LEVEL)
        } else {
            userID?.let { editor.putString(KEY_USER_ID, encryptData(it)) }
            editor.putInt(USER_LEVEL, userLevel)
        }

        editor.apply()
    }
}
