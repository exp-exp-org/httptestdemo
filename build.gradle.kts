plugins {
    java
    kotlin("jvm") version "1.3.70"
}

group = "io.hkhc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val rxjava_version = "2.2.16"
val rxkotlin_version = "2.4.0"
val assertj_version = "3.11.1"
val junit_version = "4.12"
val retrofit_version = "2.7.0"
val okhttp_version = "3.14.7"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.reactivex.rxjava2:rxjava:$rxjava_version")
    implementation("io.reactivex.rxjava2:rxkotlin:$rxkotlin_version")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttp_version")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-scalars:$retrofit_version")
    implementation("com.squareup.okhttp3:okhttp:$okhttp_version")
    testImplementation("junit:junit:$junit_version")
    testImplementation("org.assertj:assertj-core:$assertj_version")
    testImplementation("com.squareup.okhttp3:mockwebserver:$okhttp_version")
    testImplementation("com.squareup.okhttp3:okhttp-tls:$okhttp_version")
    testImplementation("io.mockk:mockk:1.9")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}