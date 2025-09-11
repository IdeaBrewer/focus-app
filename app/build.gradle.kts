// app/build.gradle.kts
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // id("com.google.dagger.hilt.android") // Disabled for now
    // id("kotlin-kapt") // Disabled for now
    id("kotlin-parcelize")
}

android {
    namespace = "com.focus.app"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.focus.app"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0.0"

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
        // compose = true // Disabled for now to focus on basic APK build
    }
    
    sourceSets {
        getByName("main") {
            java {
                srcDirs("src/main/java")
                // Exclude all problematic files that reference disabled dependencies
                include("**/MainActivity.kt")
                include("**/Repository.kt")
            }
        }
    }
}

dependencies {
    // Core Android dependencies
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // Activity and Fragment
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    
    // Lifecycle components
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    
    // Jetpack Compose - Disabled for now to focus on basic APK build
    // implementation("androidx.compose.ui:ui:1.4.3")
    // implementation("androidx.compose.ui:ui-tooling-preview:1.4.3")
    // implementation("androidx.compose.material3:material3:1.1.1")
    // implementation("androidx.compose.material:material:1.4.3")
    // implementation("androidx.compose.material:material-icons-extended:1.4.3")
    // implementation("androidx.compose.runtime:runtime:1.4.3")
    // implementation("androidx.compose.runtime:runtime-livedata:1.4.3")
    // implementation("androidx.activity:activity-compose:1.7.2")
    // implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    // implementation("androidx.navigation:navigation-compose:2.6.0")
    // implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    // debugImplementation("androidx.compose.ui:ui-tooling:1.4.3")
    
    // Room database - Disabled for now to focus on basic APK build
    // implementation("androidx.room:room-runtime:2.5.2")
    // implementation("androidx.room:room-ktx:2.5.2")
    // kapt("androidx.room:room-compiler:2.5.2")
    
    // Hilt dependency injection - Disabled for now
    // implementation("com.google.dagger:hilt-android:2.45")
    // kapt("com.google.dagger:hilt-compiler:2.45")
    
    // Navigation - Disabled for now to focus on basic APK build
    // implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    // implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    
    // DataStore preferences - Disabled for now
    // implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // RecyclerView - Disabled for now
    // implementation("androidx.recyclerview:recyclerview:1.3.1")
    
    // CardView - Disabled for now
    // implementation("androidx.cardview:cardview:1.0.0")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    
    // Hilt testing - Disabled for now
    // testImplementation("com.google.dagger:hilt-android-testing:2.45")
    // kaptTest("com.google.dagger:hilt-compiler:2.45")
}