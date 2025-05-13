plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt")
    alias(libs.plugins.google.firebase.crashlytics)


}

android {
    namespace = "com.multitv.eventbuilder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.multitv.eventbuilder"
        minSdk = 24
        targetSdk = 35
        versionCode = 12
        versionName = "1.12"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding {
            enable = true
        }
        dataBinding{
            enable = true
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.camera.core)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    implementation(libs.app.update.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    /* ---------- i added dependency here-------------*/
    implementation("com.google.firebase:firebase-messaging:23.4.1")

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    /* coroutine  */
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

    val lifecycle_version = "2.8.7"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    /*  navigation dependency */
    val nav_version = "2.8.8"
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")


    implementation ("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    /* circular image */
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7") //

    /* for otp */
    implementation("io.github.chaosleung:pinview:1.4.4")

    /* for dot circular and apply on viewpager2 */
    implementation("com.tbuonomo:dotsindicator:5.1.0")

    /* this is for rating apply on feedback fragment*/
    implementation("io.github.dmitrytsyvtsyn:nicestarratingview:1.0.3")

    /* this is for cameraX apply on login activity */
    val camerax_version = "1.5.0-alpha06"
    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation ("androidx.camera:camera-core:${camerax_version}")
    implementation ("androidx.camera:camera-camera2:${camerax_version}")
    // If you want to additionally use the CameraX Lifecycle library
    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
    // If you want to additionally use the CameraX VideoCapture library
    implementation ("androidx.camera:camera-video:${camerax_version}")
    // If you want to additionally use the CameraX View class
    implementation ("androidx.camera:camera-view:${camerax_version}")
    // If you want to additionally add CameraX ML Kit Vision Integration
    implementation ("androidx.camera:camera-mlkit-vision:${camerax_version}")
    // If you want to additionally use the CameraX Extensions library
    implementation ("androidx.camera:camera-extensions:${camerax_version}")


    implementation ("androidx.core:core-ktx:1.12.0")

/*    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")

// For streaming formats like HLS or DASH
    implementation("androidx.media3:media3-exoplayer-hls:1.2.0")
    implementation("androidx.media3:media3-exoplayer-dash:1.2.0")
    implementation("androidx.media3:media3-datasource:1.2.0") // needed for custom headers*/

    implementation( "androidx.media3:media3-exoplayer:1.3.0")
    implementation ("androidx.media3:media3-ui:1.3.0")
    implementation("androidx.media3:media3-exoplayer-hls:1.6.0")

    implementation ("com.jakewharton.threetenabp:threetenabp:1.4.5")


    implementation("jp.wasabeef:blurry:4.0.0")


    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation("com.github.chrisbanes:PhotoView:2.3.0")




}