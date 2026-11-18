# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep domain models and Room entities so Gson reflection doesn't fail in release builds
-keepclassmembers class com.saurabh.sudoku.domain.model.** { <fields>; }
-keep class com.saurabh.sudoku.domain.model.** { *; }
-keepclassmembers class com.saurabh.sudoku.data.local.database.entities.** { <fields>; }
-keep class com.saurabh.sudoku.data.local.database.entities.** { *; }

# Gson specific rules
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }