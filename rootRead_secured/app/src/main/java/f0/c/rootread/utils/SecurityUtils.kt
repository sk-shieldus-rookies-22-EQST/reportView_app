package f0.c.rootread.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Debug
import android.util.Base64
import android.util.Log
import java.io.File
import java.security.MessageDigest
import android.widget.Toast
import kotlin.system.exitProcess

object SecurityUtils {

    /**
     * 리패키징 탐지 - 앱 서명 확인
     */
    fun verifyAppSignature(context: Context): Boolean {
        return try {
            val packageInfo: PackageInfo =
                context.packageManager.getPackageInfo(context.packageName, PackageManager.GET_SIGNING_CERTIFICATES)

            val signatures = packageInfo.signingInfo!!.apkContentsSigners
            for (signature in signatures) {
                val md = MessageDigest.getInstance("SHA-1")
                md.update(signature.toByteArray())
                val currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT).trim()

                Log.d("SIGNATURE", currentSignature)

                val expectedSignature = "pPnDMDMoidOlNzhRmGafNWefk8U=" // 원본 APK 서명 값
                if (currentSignature == expectedSignature) {
                    return true
                }
            }
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 루팅 탐지 - 루트 관련 파일 확인
     */
    fun isDeviceRooted(): Boolean {
        val rootFiles = arrayOf(
            "/system/app/Superuser.apk",
            "/system/xbin/su",
            "/system/bin/su",
            "/system/sd/xbin/su",
            "/system/bin/failsafe/su",
            "/data/local/xbin/su",
            "/data/local/bin/su",
            "/system/su",
            "/system/bin/.ext/.su"
        )
        for (path in rootFiles) {
            if (File(path).exists()) {
                return true
            }
        }
        return false
    }

    /**
     * 루팅 탐지 - su 명령어 실행
     */
    fun checkSuCommand(): Boolean {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            val inputStream = process.inputStream
            inputStream.read() != -1
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 디버깅 탐지 - Debugger.isDebuggerConnected() 사용
     */
    fun isDebuggerAttached(): Boolean {
        return Debug.isDebuggerConnected()
    }

    /**
     * 디버깅 탐지 - TracerPid 검사
     */
    fun isBeingTraced(): Boolean {
        return try {
            val statusFile = File("/proc/${android.os.Process.myPid()}/status")
            val reader = statusFile.bufferedReader()
            reader.useLines { lines ->
                lines.forEach { line ->
                    if (line.startsWith("TracerPid:")) {
                        val pid = line.split("\t")[1].trim().toInt()
                        return pid > 0
                    }
                }
            }
            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 프록시 사용 탐지
     */
    fun isUsingProxy(): Boolean {
        val proxyAddress = System.getProperty("http.proxyHost")
        return proxyAddress != null
    }

//    init {
//        System.loadLibrary("securityutils") // 🔥 .so 라이브러리 로드
//    }
//
//    external fun isAppTampered(context: Context): Boolean
//    external fun isDeviceRooted(): Boolean
//    external fun checkSuCommand(): Boolean
//    external fun isDebuggerAttached(): Boolean
//    external fun verifyAppSignature(context: Context): Boolean
//    external fun isBeingTraced(): Boolean

    //  보안 체크 수행 및 앱 종료 처리
    fun performSecurityCheck(activity: Activity) {
//        if (!verifyAppSignature(activity) || checkSuCommand() || isDebuggerAttached() || isBeingTraced() || isDeviceRooted()) {
//            Toast.makeText(activity, "앱을 정상적으로 실행 할 수 없음", Toast.LENGTH_SHORT).show()
//
//            activity.finishAffinity() // 모든 Activity 종료
//            android.os.Process.killProcess(android.os.Process.myPid()) // 프로세스 종료
//            exitProcess(1)
//        }
        if (!verifyAppSignature(activity)) {
            Log.d("SECURITY_CHECK", "verifyAppSignature(activity) ${!verifyAppSignature(activity)}")
        }
        if (checkSuCommand()) {
            Log.d("SECURITY_CHECK", "checkSuCommand() ${checkSuCommand()}")
        }
        if (isDebuggerAttached()) {
            Log.d("SECURITY_CHECK", "isDebuggerAttached() ${isDebuggerAttached()}")
        }
        if (isBeingTraced()) {
            Log.d("SECURITY_CHECK", "isBeingTraced() ${isBeingTraced()}")
        }
        if (isDeviceRooted()) {
            Log.d("SECURITY_CHECK", "isDeviceRooted() ${isDeviceRooted()}")
        }
    }

}