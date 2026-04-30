plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktfmt)
}

group = "it.iutc"

repositories {
    mavenCentral()
    maven(url = "https://cache-redirector.jetbrains.com/intellij-dependencies")
    maven(url = "https://www.jetbrains.com/intellij-repository/releases")
    maven(url = "https://www.jetbrains.com/intellij-repository/snapshots")
    maven(
        url =
            "https://cache-redirector.jetbrains.com/packages.jetbrains.team/maven/p/grazi/grazie-platform-public"
    )
}

dependencies {
    testImplementation(libs.jetbrains.ide.starter.squashed)
    testImplementation(libs.jetbrains.ide.starter.junit5)
    testImplementation(libs.jetbrains.ide.metrics.collector)
    testImplementation(libs.jetbrains.ide.metrics.collector.starter)
    testImplementation(libs.jetbrains.ide.performance.testing.commands)
    testImplementation(libs.jetbrains.ide.starter.driver)
    testImplementation(libs.jetbrains.driver.client)
    testImplementation(libs.jetbrains.driver.sdk)
    testImplementation(libs.jetbrains.driver.model)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

kotlin { jvmToolchain(25) }

tasks.test { useJUnitPlatform() }

ktfmt { kotlinLangStyle() }
