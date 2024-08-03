plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}


fun Project.getGradleProperty(property: String): String {
    val gradleProperties = extra
    return gradleProperties[property] as String
}

android {
    namespace = "muoipt.nyt.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        println(project.getGradleProperty("api_domain_dev"))
        debug {
            isMinifyEnabled = false
            buildConfigField(
                "String",
                "BASE_URL",
                project.getGradleProperty("api_domain_dev")
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            buildConfigField(
                "String",
                "BASE_URL",
                project.getGradleProperty("api_domain_prod")
            )
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(project(":model"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp3.okhttp)
    implementation(libs.okhttp3.loggin.interceptor)
    implementation(libs.chucker)

    implementation(libs.moshi)
    ksp(libs.moshi.codegen)
    implementation(libs.moshi.codegen)
}