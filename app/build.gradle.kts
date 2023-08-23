plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.lamaq.tasbeeh"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lamaq.tasbeeh"
        minSdk = 28
        targetSdk = 34
        versionCode = 11
        versionName = "4.6.9"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.navigation:navigation-compose:2.7.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.wear.compose:compose-material3:1.0.0-alpha09")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")

    implementation("com.google.firebase:firebase-config-ktx:21.4.1")
    implementation(platform("com.google.firebase:firebase-bom:32.2.2"))
    implementation("com.google.firebase:firebase-messaging-ktx:23.2.1")
    implementation ("com.google.firebase:firebase-inappmessaging-display-ktx:20.3.3")
    implementation("com.google.firebase:firebase-analytics-ktx:21.3.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.7.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation("com.google.accompanist:accompanist-permissions:0.30.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))

    // For Glance support
    implementation("androidx.glance:glance:1.0.0-rc01")
    // For AppWidgets support
    implementation("androidx.glance:glance-appwidget:1.0.0-rc01")
    implementation("androidx.benchmark:benchmark-common:1.1.1")

    implementation("com.github.bumptech.glide:glide:4.13.0")
    implementation("com.google.android.engage:engage-core:1.2.0")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    debugImplementation("androidx.compose.ui:ui-test-junit4:1.5.0")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation(kotlin("reflect"))
}