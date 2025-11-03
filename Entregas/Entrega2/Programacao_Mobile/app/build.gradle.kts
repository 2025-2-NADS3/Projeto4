plugins {
    alias(libs.plugins.android.application)
    // Plugins de Kotlin e KSP foram removidos para um projeto 100% Java
}

android {
    namespace = "com.example.projeto_entrega2"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.projeto_entrega2"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.recyclerview)
    implementation(libs.cardview)

    // Room (Banco de Dados Local) - Usando annotationProcessor para Java
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version") // MUDANÇA DE ksp PARA annotationProcessor

    // Retrofit (Cliente HTTP para API)
    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")

    // Gson (Biblioteca para parsear JSON)
    implementation("com.google.code.gson:gson:2.10.1")

    // Glide (Carregamento de Imagens pela URL) - Usando annotationProcessor para Java
    val glide_version = "4.16.0"
    implementation("com.github.bumptech.glide:glide:$glide_version")
    annotationProcessor("com.github.bumptech.glide:compiler:$glide_version") // MUDANÇA DE ksp PARA annotationProcessor

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}