plugins {
    id("com.android.library")
    id("kotlin-android")
}


//    androids = [
//        compileSdkVersion: 33,
//    minSdkVersion    : 21,
//    targetSdkVersion : 33,
//    versionCode      : 1,
//    versionName      : "1.0"
//    ]
//    version = [
//
//    ]
//
//    library = [
//        room_version: '2.4.3',
//    arch_version: '1.1.1'
//
//    ]
//}
android {
    compileSdkVersion(33)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(33)
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
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
