plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.khalinimaltaapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.khalinimaltaapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    // Use esta forma simples que não dá erro de "Unresolved reference"
    kotlinOptions {
        jvmTarget = "17"
        // Use exatamente assim, com o 'listOf'
        freeCompilerArgs = freeCompilerArgs + listOf("-Xjvm-default=all")
    }

    buildFeatures {
        compose = true
    }
}


kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.3")

    // Room
    // Mude de 2.6.1 para 2.7.0-alpha01 ou alpha02
    dependencies {
        // Mude a versão para esta aqui:
        val room_version = "2.7.0-alpha01"

        implementation("androidx.room:room-runtime:$room_version")
        implementation("androidx.room:room-ktx:$room_version")
        ksp("androidx.room:room-compiler:$room_version")

    }

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.icons.extended)

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
}

ksp {
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}