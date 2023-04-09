plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    val compileSdkV: Int by rootProject.extra
    val minSdkV: Int by rootProject.extra
    val compose_version: String by rootProject.extra

    compileSdk = compileSdkV

    defaultConfig {
        minSdk = (minSdkV)
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
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(11))
        }
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = compose_version
        kotlinCompilerVersion = "1.5.10"
    }
}

dependencies {
    val composeVersion = "1.3.3"
    val roomVersion: String by rootProject.extra
    api("androidx.compose.runtime:runtime:1.4.1")
    api("androidx.core:core-ktx:1.10.0")
    testApi("junit:junit:4.13.2")
    androidTestApi("androidx.test.ext:junit:1.1.5")
    androidTestApi("androidx.test.espresso:espresso-core:3.5.1")
    api("androidx.appcompat:appcompat:1.6.1")
    api("com.google.android.material:material:1.8.0")
    api("androidx.compose.ui:ui:1.4.1")
    api("androidx.compose.material:material:1.4.1")
    api("androidx.compose.ui:ui-tooling:$composeVersion")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    api("androidx.activity:activity-compose:1.7.0")

    // compose
    api("androidx.compose.runtime:runtime-livedata:$composeVersion")
    api("androidx.lifecycle:lifecycle-livedata-core-ktx:2.6.1")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("androidx.compose.material:material:1.4.1")
    // https://mvnrepository.com/artifact/androidx.constraintlayout/constraintlayout-compose
    api("androidx.constraintlayout:constraintlayout-compose:1.1.0-alpha09")
    api("androidx.activity:activity-compose:1.7.0")
    api("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    api("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    api("androidx.compose.foundation:foundation:1.4.1")
    api("androidx.compose.foundation:foundation-layout:1.4.1")
    api("androidx.compose.material:material-icons-extended:1.4.1")
    api ("androidx.compose.material3:material3:1.1.0-beta02")
    api("androidx.appcompat:appcompat:1.6.1")
    api("com.google.android.material:material:1.8.0")
    api("androidx.constraintlayout:constraintlayout:2.1.4")
    api("io.coil-kt:coil-compose:2.2.2")

    // 安卓Utils
    api("com.blankj:utilcode:1.30.7")
    androidTestApi("androidx.compose.ui:ui-test:$composeVersion")
    androidTestApi("androidx.compose.ui:ui-test-junit4:$composeVersion")

    // navigation
    api("androidx.navigation:navigation-compose:2.6.0-alpha09")
    api("com.google.accompanist:accompanist-insets:0.27.0")
    api("com.google.accompanist:accompanist-coil:0.15.0")
    api("com.google.accompanist:accompanist-pager:0.27.0")
    api("com.google.accompanist:accompanist-glide:0.15.0")

    //flow布局
    // https://mvnrepository.com/artifact/com.google.accompanist/accompanist-flowlayout
    api("com.google.accompanist:accompanist-flowlayout:0.27.0")

    //权限
    api("pub.devrel:easypermissions:3.0.0")
    api("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //room数据库
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    //2.5.0最新版本有问题建议低版本
    api("androidx.room:room-runtime:2.4.3")
    // optional - Kotlin Extensions and Coroutines support for Room
    api("androidx.room:room-ktx:2.4.3")
    kapt("androidx.room:room-compiler:2.4.3")

    //Retrofit
    api("com.squareup.retrofit2:retrofit:2.9.0")
    api("com.squareup.retrofit2:converter-gson:2.9.0")

}