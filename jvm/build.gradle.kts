plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "dev.kobalt"
version = "0000.00.00.00.00.00.000"

repositories {
    mavenCentral()
    maven(url = "https://maven.google.com")
}

fun ktor(module: String, version: String) = "io.ktor:ktor-$module:$version"
fun exposed(module: String, version: String) = "org.jetbrains.exposed:exposed-$module:$version"
fun general(module: String, version: String) = "$module:$version"
fun kotlinx(module: String, version: String) = "org.jetbrains.kotlinx:kotlinx-$module:$version"
fun kotlinw(module: String, version: String) = "org.jetbrains.kotlin-wrappers:kotlin-$module:$version"

fun DependencyHandler.serialization() {
    implementation(kotlinx("serialization-json", "1.0.0"))
    implementation(kotlinx("serialization-core", "1.0.0"))
}

fun DependencyHandler.commandLineInterface() {
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.3")
}

fun DependencyHandler.standardLibrary() {
    implementation(kotlin("stdlib", "1.6.10"))
}

fun DependencyHandler.logger() {
    implementation(general("org.slf4j:slf4j-simple", "1.7.35"))
}

fun DependencyHandler.androidSdkTools() {
    implementation(general("com.android.tools:common", "26.1.0"))
    implementation(general("com.android.tools:sdk-common", "26.1.0")) {
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jre8")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib-jre7")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-reflect")
        exclude(group = "com.android.tools", module = "sdklib")
        exclude(group = "com.android.tools.build", module = "builder-model")
        exclude(group = "com.android.tools.ddms", module = "ddmlib")
        exclude(group = "com.google.protobuf", module = "protobuf-java")
        exclude(group = "org.bouncycastle", module = "bcpkix-jdk15on")
        exclude(group = "org.bouncycastle", module = "bcpprov-jdk15on")
        exclude(group = "com.android.tools.build", module = "builder-test-api")
    }
    implementation(general("org.apache.xmlgraphics:fop", "2.5")) {
        exclude(group = "javax.media", module = "jai-core")
        exclude(group = "com.sun.media", module = "jai-codec")
        exclude(group = "javax.servlet", module = "servlet-api")
        exclude(group = "org.apache.ant", module = "ant")
        exclude(group = "javax.servlet", module = "servlet-api")
    }
}

dependencies {
    standardLibrary()
    commandLineInterface()
    androidSdkTools()
}

tasks {
    named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
        archiveFileName.set("svg2asset.jvm.jar")
        mergeServiceFiles()
        minimize {
            exclude(dependency("org.apache.xmlgraphics:fop-core:.*"))
        }
        manifest {
            attributes("Main-Class" to "dev.kobalt.svg2asset.jvm.MainKt")
        }
    }
}