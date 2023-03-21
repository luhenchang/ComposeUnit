plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}
android {
    val compileSdkV: Int by rootProject.extra
    val minSdkV: Int by rootProject.extra
    val targetSdkV: Int by rootProject.extra
    val versionC: Int by rootProject.extra
    val versionN: String by rootProject.extra

    compileSdk = compileSdkV

    defaultConfig {
        applicationId = "com.example.composeunit"
        minSdk = minSdkV
        targetSdk = targetSdkV
        versionCode = versionC
        versionName = versionN

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    @Suppress("UnstableApiUsage")
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }
    @Suppress("UnstableApiUsage")
    buildFeatures.compose = true
    @Suppress("UnstableApiUsage")
    composeOptions {
        val composeVersion: String by rootProject.extra
        kotlinCompilerExtensionVersion = composeVersion
        //kotlinCompilerVersion = "1.5.10"
    }
}

dependencies {
    implementation(project(":base"))
    implementation(project(":highlight"))
    //room 数据库
    val roomVersion = "2.4.3"
    kapt("androidx.room:room-compiler:$roomVersion")
}
