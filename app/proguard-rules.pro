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

-keep class com.murgupluoglu.requestsample.PeopleResponse { *; }
-keep class com.murgupluoglu.requestsample.Info { *; }
-keep class com.murgupluoglu.requestsample.User { *; }
-keep class com.murgupluoglu.requestsample.Dob { *; }
-keep class com.murgupluoglu.requestsample.Id { *; }
-keep class com.murgupluoglu.requestsample.Location { *; }
-keep class com.murgupluoglu.requestsample.Coordinates { *; }
-keep class com.murgupluoglu.requestsample.Street { *; }
-keep class com.murgupluoglu.requestsample.Timezone { *; }
-keep class com.murgupluoglu.requestsample.Login { *; }
-keep class com.murgupluoglu.requestsample.Name { *; }
-keep class com.murgupluoglu.requestsample.Picture { *; }
-keep class com.murgupluoglu.requestsample.Registered { *; }