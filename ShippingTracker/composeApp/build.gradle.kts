import org.jetbrains.compose.desktop.application.dsl.TargetFormat

configurations.all {
    resolutionStrategy {
        force("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
        force("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
        force("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")
    }
}


plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation("io.ktor:ktor-server-tests:2.3.8")
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")
            implementation("org.slf4j:slf4j-simple:2.0.12")
            implementation("io.ktor:ktor-server-netty:2.3.8")
            implementation("io.ktor:ktor-server-html-builder:2.3.8")
        }
    }
}

compose.desktop {
    application {
        mainClass = "org.example.project.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "org.example.project"
            packageVersion = "1.0.0"
        }
    }
}