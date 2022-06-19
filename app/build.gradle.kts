plugins {
    kotlin("android")
    kotlin("kapt")
    id("com.android.application")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

// Allow references to generated code
kapt.correctErrorTypes = true

val properties = LocalProperties(rootProject.extra)
android {
    compileSdk = Config.compileSdkVersion

    defaultConfig {
        applicationId = Config.applicationId
        minSdk = Config.minSdkVersion
        targetSdk = Config.targetSdkVersion
        versionCode = Config.versionCode
        versionName = Config.versionName

        buildConfigField("String", LocalProperties.API_KEY_PROP, properties.getApiKey())
        testInstrumentationRunner = "com.moviediscovery.HiltAndroidRunner"
    }

    signingConfigs {
        create("release") {
            storeFile = properties.getKeystoreFile()
            storePassword = properties.getKeyStorePassword()
            keyAlias = properties.getAlias()
            keyPassword = properties.getAliasPassword()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
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
        viewBinding = true
    }
}

dependencies {
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.material)
    implementation(Dependencies.constraintLayout)
    implementation(Dependencies.swipeRefreshLayout)

    implementation(Dependencies.lifeCycleViewModel)
    implementation(Dependencies.lifeCycleLiveData)
    implementation(Dependencies.lifeCycleCommon)
    implementation(Dependencies.lifeCycleRuntime)

    // Room
    implementation(Dependencies.roomRuntime)
    implementation(Dependencies.roomKTX)
    annotationProcessor(Dependencies.roomCompiler)
    kapt(Dependencies.roomCompilerKapt)

    // Retrofit & Gson
    implementation(Dependencies.retrofit)
    implementation(Dependencies.retrofitGsonConverter)
    implementation(Dependencies.gson)

    // Coroutines
    implementation(Dependencies.coroutines)

    // Dagger-Hilt
    implementation(Dependencies.daggerHilt)
    kapt(Dependencies.daggerHiltKaptCompiler)
    androidTestImplementation(Dependencies.daggerHiltTest)
    kaptAndroidTest(Dependencies.daggerHiltKaptCompiler)

    // Navigation component
    implementation(Dependencies.navigationComponentFragment)
    implementation(Dependencies.navigationComponentUI)

    // Glide
    implementation(Dependencies.glide)
    annotationProcessor(Dependencies.glideCompiler)
    kapt(Dependencies.glideKaptCompiler)

    // Test
    testImplementation(Dependencies.jUnit)
    implementation(Dependencies.androidJUnit)
}