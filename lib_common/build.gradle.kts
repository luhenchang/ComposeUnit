plugins {
    id("com.android.library")
    id("kotlin-android")
    id("com.google.devtools.ksp")
}

android {
    val sdkVersion = libs.versions.sdkVersion.get().toInt()
    val minSdkVersion = libs.versions.minSdkVersion.get().toInt()

    compileSdk = sdkVersion

    defaultConfig {
        minSdk = minSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

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
        sourceCompatibility(JavaVersion.VERSION_17)
        targetCompatibility(JavaVersion.VERSION_17)
    }
    val javaVersion = libs.versions.javaVersion.get()
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))
        }
    }
    kotlinOptions {
        jvmTarget = javaVersion
    }
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeVersion.get()
    }
    namespace = "com.example.lib_common"
}

dependencies {
    api(libs.androidx.compose.runtime.runtime)
    api(libs.core.ktx)
    testApi(libs.junit)
    androidTestApi(libs.androidx.test.ext.junit)
    androidTestApi(libs.espresso.core)
    api(libs.com.google.android.material.material)
    api(libs.androidx.compose.ui.ui)
    api(libs.material)
    api(libs.androidx.compose.ui.ui.tooling)
    api(libs.lifecycle.runtime.ktx)
    api(libs.activity.compose)

    // compose
    api(libs.androidx.compose.runtime.runtime.livedata)
    api(libs.androidx.lifecycle.lifecycle.livedata.core.ktx)
    api(libs.androidx.constraintlayout.constraintlayout)
    api(libs.androidx.constraintlayout.constraintlayout.compose)
    //systemUI
    api(libs.google.accompanist.webview)
    api(libs.google.accompanist.systemuicontroller)
    api(libs.androidx.lifecycle.viewmodel.compose)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.foundation)
    api(libs.androidx.foundation.layout)
    api(libs.androidx.material.icons.extended)
    api(libs.androidx.material3)
    api(libs.androidx.appcompat)
    api(libs.coil.compose)

    // 安卓Utils
    api(libs.utilcode)
    androidTestApi(libs.androidx.ui.test)
    androidTestApi(libs.androidx.ui.test.junit4)

    // navigation
    api(libs.androidx.navigation.compose)
    api(libs.accompanist.insets)
    api(libs.accompanist.coil)
    api(libs.accompanist.pager)
    api(libs.accompanist.glide)

    //flow布局
    // https://mvnrepository.com/artifact/com.google.accompanist/accompanist-flowlayout
    api(libs.accompanist.flowlayout)

    //权限
    api(libs.easypermissions)
    api(libs.androidx.lifecycle.extensions)

    //room数据库
    api(libs.kotlinx.coroutines.android)
    api(libs.kotlinx.coroutines.core)

    //2.5.0最新版本有问题建议低版本
    api(libs.androidx.room.runtime)
    // optional - Kotlin Extensions and Coroutines support for Room
    api(libs.androidx.room.ktx)
    ksp(libs.room.compiler)

    //Retrofit
    api(libs.retrofit)
    api(libs.com.squareup.retrofit2.converter.gson)
    api(libs.com.belerweb.pinyin4j)

}