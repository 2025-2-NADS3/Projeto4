plugins {
    alias(libs.plugins.android.application)
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

    // Room (Banco de Dados Local)
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")

    // Volley (Cliente HTTP para API)
    implementation("com.android.volley:volley:1.2.1")

    // Picasso (Carregamento de Imagens pela URL)
    implementation("com.squareup.picasso:picasso:2.8")

    // Gson (Biblioteca para parsear JSON)
    implementation("com.google.code.gson:gson:2.10.1")
    
    // MPAndroidChart (Biblioteca de Gráficos)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}