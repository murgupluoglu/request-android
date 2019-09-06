# Request

## Setup

### Gradle

Add the JitPack repository to your project level `build.gradle`:

```groovy
allprojects {
 repositories {
    google()
    jcenter()
    maven { url "https://jitpack.io" }
 }
}
```

Add this to your app `build.gradle`:

```groovy
dependencies {
	implementation 'com.github.murgupluoglu:request-android:1.0.0'
}
```
