import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "ru.testapp.nework"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.testapp.nework"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
            if(rootProject.file("maps.properties").exists()) {
                properties.load(rootProject.file("maps.properties").inputStream())
            }

        buildConfigField("String", "BASE_URL", "\"https://netomedia.ru/\"")

        buildConfigField("String", "MAPS_API_KEY", properties.getProperty("MAPS_API_KEY", ""))
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    val roomVersion = "2.6.0-rc01"
    val retrofitVersion = "2.9.0"
    val retrofitGsonVersion = "2.9.0"
    val lifecycleVersion = "2.6.2"
    val gsonVersion = "2.10.1"
    val firebaseVersion = "30.5.0"
    val navVersion = "2.5.2"
    val playServicesBaseVersion = "18.1.0"
    val coroutinesVersion = "1.7.3"
    val workManagerVersion = "2.7.1"
    val pagingVersion = "3.2.0"
    val imagePickerVersion = "2.1"
    val hiltVersion = "2.48.1"

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitGsonVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")
    implementation("com.google.code.gson:gson:$gsonVersion")
    implementation(platform("com.google.firebase:firebase-bom:$firebaseVersion"))
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation ("com.github.bumptech.glide:glide:4.14.2")
    implementation("com.squareup.okhttp3:okhttp")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation ("com.google.android.gms:play-services-base:$playServicesBaseVersion")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion")
    implementation ("androidx.work:work-runtime-ktx:$workManagerVersion")
    implementation ("com.google.dagger:hilt-android:$hiltVersion")
    kapt ("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation ("androidx.paging:paging-runtime-ktx:$pagingVersion")
    implementation ("androidx.room:room-paging:$roomVersion")
    implementation ("com.github.dhaval2404:imagepicker:$imagePickerVersion")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    implementation ("com.yandex.android:maps.mobile:4.4.0-lite")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")

    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.3")
}