# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ProGuard 기본 설정
# 안드로이드 기본 라이브러리 최적화
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontpreverify
-dontwarn android.support.**
-dontnote android.support.**

# 코드 난독화 적용
-renamesourcefileattribute SourceFile
-keepattributes *Annotation*

# 안드로이드 기본 API 보호
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.Application
-keep public class * extends android.view.View
-keep public class * extends androidx.appcompat.app.AppCompatActivity

# 직렬화된 클래스 보호
-keepclassmembers class * implements java.io.Serializable { *; }

# 리플렉션을 사용하는 클래스 보호
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# 데이터 모델 클래스 보호
-keep class com.example.models.** { *; }

# Gson 사용 시 유지할 필드
-keep class com.example.model.** { *; }
-keep class com.google.gson.** { *; }
-keep class com.example.rootread.model.** { @com.google.gson.annotations.SerializedName <fields>; }

# Jackson 사용 시 유지할 필드
-keep class com.fasterxml.jackson.** { *; }

# Retrofit 관련 유지
-keep class com.example.api.** { *; }
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.squareup.okhttp3.** { *; }
-keep class com.squareup.retrofit2.** { *; }

# Gson, Jackson, Moshi 같은 JSON 파서에서 사용되는 클래스 및 필드 유지
# MutableMap<String, Any>를 사용하는 7개 모델의 난독화 예외 처리
-keep class com.example.rootread.model.board.BoardQnAResponse { *; }
-keep class com.example.rootread.model.board.BoardResponse { *; }
-keep class com.example.rootread.model.purchase.CartResponse { *; }
-keep class com.example.rootread.model.user.UserbooklistResponse { *; }
-keep class com.example.rootread.model.user.UserpurchaseResponse { *; }
-keep class com.example.rootread.model.view.ViewbooklistResponse { *; }
-keep class com.example.rootread.model.view.ViewbooksearchResponse { *; }

# Room 데이터베이스 관련 유지
-keep class androidx.room.** { *; }
-keep class com.example.database.** { *; }

# Parcelable 객체 유지
-keep class com.example.model.** implements android.os.Parcelable { *; }

# Serializable 객체 유지
-keep class com.example.model.** implements java.io.Serializable { *; }

# OkHttp GraalVM 관련 코드 제거
-dontwarn okhttp3.internal.graal.**
-dontwarn org.graalvm.**
-dontwarn com.oracle.svm.**

# 난독화 허용하지만 JSON Key 값 유지
-adaptclassstrings
-adaptresourcefilecontents
-adaptresourcefilenames

# 모든 모델 클래스의 필드는 난독화되지만, JSON 직렬화가 정상적으로 작동하도록 설정
-keepattributes Signature, RuntimeVisibleAnnotations, RuntimeInvisibleAnnotations

# MutableMap<String, Any> 사용 시 데이터 타입 유지
-keep class java.util.HashMap { *; }
-keep class java.util.LinkedHashMap { *; }