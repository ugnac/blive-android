plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.learn.wandroid"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.learn.wandroid"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(mapOf("path" to ":common")))

    implementation(Deps.core_ktx)
    implementation(Deps.appcompat)
    implementation(Deps.material)
    implementation(Deps.constraintlayout)
    implementation(Deps.annotation)

    implementation(Deps.lifecycle.livedata_ktx)
    implementation(Deps.lifecycle.viewmodel_ktx)

    implementation(Deps.rxjava3.rxjava)
    implementation(Deps.rxjava3.rxandroid)

    // Retrofit Okhttp
    implementation(Deps.retrofit2.retrofit)
    implementation(Deps.retrofit2.adapter_rxjava3)
    implementation(Deps.retrofit2.converter_gson)
    implementation(Deps.retrofit2.logging_interceptor)

    // 图片加载
    implementation(Deps.glide)
    annotationProcessor(Deps.glide_compiler)

    // Test
    testImplementation(Deps.junit)
    androidTestImplementation(Deps.ext_junit)
    androidTestImplementation(Deps.espresso)
}