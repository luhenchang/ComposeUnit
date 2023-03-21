//ext {
//    androids = [
//            compileSdkVersion: 33,
//            minSdkVersion    : 21,
//            targetSdkVersion : 33,
//            versionCode      : 1,
//            versionName      : "1.0"
//    ]
//    version = [
//
//    ]
//
//    library = [
//            room_version: '2.4.3',
//            arch_version: '1.1.1'
//
//    ]
//}
project.extra.set("kotlin_version","1.8.0")
project.extra.set("composeVersion","1.4.0")
project.extra.set("gradleVersion","7.4.2")
val compose_version by extra("1.4.1")

val compileSdkV by extra(33)
val minSdkV by extra(21)
val targetSdkV by extra(33)
val versionC by extra(1)
val versionN by extra("1.0")

project.extra.set("compileSdkVersion",33)
project.extra.set("minSdkVersion",21)
project.extra.set("targetSdkVersion",33)
project.extra.set("versionCode",1)
project.extra.set("versionName","1.0")


project.extra.set("roomVersion","2.4.3")
project.extra.set("arch_version","1.1.1")

//mapOf(
//    Pair("minSdkVer", 22),
//    Pair("targetSdkVer", 25),
//    Pair("compiledSdkVer", 25),
//    Pair("buildToolsVer", "26-rc4"),
//    Pair("gradleVersion","7.4.2")
//).entries.forEach {
//    project.extra.set(it.key, it.value)
//}