plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

android {
    namespace = "com.1.bachelorhelper"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.developermaheshsofttechltd.bachelorhelper"
        minSdk = 24
        targetSdk = 34
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
    viewBinding {
        enable = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-messaging:23.4.1")
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
    implementation("androidx.activity:activity:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.google.android.play:app-update-ktx:2.1.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")

    implementation("com.anggrayudi:storage:1.5.5")

    implementation("com.airbnb.android:lottie:6.2.0")




    implementation("com.google.firebase:firebase-storage:20.3.0")

    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")

    implementation("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")

    val lifecycleVersion = "2.5.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")


    implementation ("androidx.core:core-splashscreen:1.0.0-beta02")
    implementation ("com.miguelcatalan:materialsearchview:1.4.0")

    implementation  ("io.ak1:bubbletabbar:1.0.8")

    implementation ("com.github.ismaeldivita:chip-navigation-bar:1.4.0")


    val roomVersion="2.6.1"
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")


    implementation ("com.google.code.gson:gson:2.10.1")

    implementation ("com.dinuscxj:circleprogressbar:1.3.6")



//    def epoxy_version = "4.6.3"
//    implementation ("com.airbnb.android:epoxy:$epoxy_version")

    // Facebook shimmer (loading state)
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
}