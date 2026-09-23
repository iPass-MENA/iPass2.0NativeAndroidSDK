plugins {
    id("com.android.library")
    id("maven-publish")
    id("org.jetbrains.kotlin.plugin.compose")
//    id("com.google.gms.google-services")

}

var artifactId = "IPassPlusSDK"
var groupId = "com.sdk.ipassplussdk"

android {
    namespace = "com.sdk.ipassplussdk"
    compileSdk = 37

    defaultConfig {
        minSdk = 24
//        resConfigs("en")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }


    ndkVersion = "26.3.11579264"

    buildTypes {
        release {
            ndk.debugSymbolLevel = "FULL"
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
//        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }

//    packagingOptions {
//        jniLibs {
//            useLegacyPackaging = true // Enabling flag to compress JNI Libs to reduce APK size Ref: https://developer.android.com/studio/releases/gradle-plugin#compress-native-libs-dsl
//        }
//    }

    packaging {
        jniLibs {
            useLegacyPackaging = true
        }

        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.19.0")
    implementation("androidx.appcompat:appcompat:1.8.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")

//    implementation("com.regula.documentreader:api:8.3.11882@aar") {
//        isTransitive = true
//    }


//    implementation("com.regula.documentreader:api:9.1.12250@aar") {
//        isTransitive = true
//    }

    implementation("com.regula.documentreader:api:9.8.13324@aar") {
        isTransitive = true
    }


    // FaceLivenessDetector dependency
    implementation("com.amplifyframework.ui:liveness:1.11.0")

    // Amplify Auth dependency (unnecessary if using your own credentials provider)
//    implementation ("com.amplifyframework:aws-auth-cognito:2.14.5")
//    implementation ("com.amplifyframework:aws-auth-cognito:2.25.1")
    implementation("com.amplifyframework:aws-auth-cognito:2.42.0")

    // Material3 dependency for theming FaceLivenessDetector
//    implementation ("androidx.compose.material3:material3:1.3.1")

    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.compose.material3:material3:1.4.0")

    // Support for Java 8 features
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.1.5")

// https://mvnrepository.com/artifact/com.amazonaws/aws-android-sdk-rekognition
//    implementation("com.amazonaws:aws-android-sdk-rekognition:2.75.0")
//    implementation ("com.amazonaws:aws-android-sdk-core:2.16.0")
//    implementation("com.amazonaws:aws-android-sdk-core:2.77.1")
    implementation("com.amazonaws:aws-android-sdk-core:2.81.1")
//    implementation ("com.amazonaws:aws-android-sdk-rekognition:2.16.0")
//    implementation ("com.amazonaws:aws-android-sdk-rekognition:2.77.1")
    implementation("com.amazonaws:aws-android-sdk-rekognition:2.81.1")

    implementation ("com.squareup.retrofit2:retrofit:3.0.0")
    implementation ("com.squareup.retrofit2:converter-gson:3.0.0")
    implementation("com.squareup.okhttp3:okhttp:5.5.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.5.0")
    implementation("com.google.code.gson:gson:2.14.0")
    implementation("com.google.android.material:material:1.14.0")
}
project.afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.sdk.ipassplussdk"

                artifactId = "iPass2.0NativeAndroidSDK"
                version = "2.23"
            }
        }
    }
}