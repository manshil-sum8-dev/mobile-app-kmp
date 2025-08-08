plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
    implementation("org.freemarker:freemarker:2.3.32")
    implementation("com.squareup:kotlinpoet:1.15.3")
}