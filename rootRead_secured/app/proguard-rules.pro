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

## ✅ 난독화해도 영향 없는 부분은 유지하지 않도록 최적화
-keep class f0.c.rootread.model.** { *; }  # Model 클래스 유지 (JSON 변환용)
-keep class * extends com.bumptech.glide.module.AppGlideModule { *; }  # Glide Module 유지

# ❌ 기타 코드 (ViewModel, Repository, UseCase 등)는 난독화해도 문제 없음