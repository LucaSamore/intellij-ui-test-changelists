plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktfmt)
}

group = "it.iutc"

repositories { mavenCentral() }

dependencies { testImplementation(kotlin("test")) }

kotlin { jvmToolchain(25) }

tasks.test { useJUnitPlatform() }

ktfmt { kotlinLangStyle() }
