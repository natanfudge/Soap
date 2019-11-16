
plugins {
    kotlin("jvm")
}

group = "com.github.cretz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotlin_version : String by project

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation ("org.jetbrains.kotlin:kotlin-stdlib-common:$kotlin_version")
    testImplementation ("org.jetbrains.kotlin:kotlin-test-annotations-common:$kotlin_version")
    testImplementation ("org.jetbrains.kotlin:kotlin-test-common:$kotlin_version")
    implementation ("org.jetbrains.kotlin:kotlin-compiler-embeddable:$kotlin_version")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}