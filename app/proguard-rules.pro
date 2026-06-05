# Retrofit
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**

# Gson / models
-keep class com.styleme.app.models.** { *; }
-keepclassmembers class com.styleme.app.models.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** { *; }
