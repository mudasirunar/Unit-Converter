# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Keep Gson serialization annotations and class members
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod

# Keep Retrofit classes and API interface methods
-keep class retrofit2.** { *; }
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepclassmembers interface * {
    @retrofit2.http.* <methods>;
}

# Keep our data models, room entities, DAOs, view models, and repository classes
-keep class com.example.unitconverter.data.** { *; }
-keepclassmembers class com.example.unitconverter.data.** { *; }