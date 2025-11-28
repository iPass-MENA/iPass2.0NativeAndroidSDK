pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

    }

}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
//        jcenter()
        maven {
            url = uri("https://jitpack.io")
        }
    //    maven { url =uri("https://maven.regulaforensics.com/RegulaDocumentReader") }
        maven { url = uri("https://maven.regulaforensics.com/RegulaDocumentReader") }
//        maven { url =uri("https://maven.regulaforensics.com/RegulaDocumentReader/Beta") }
//        maven { url = uri("https://maven.regulaforensics.com/RegulaDocumentReader") }


    }

}

rootProject.name = "IPassPlus"
include(":app")
include(":IPassPlusSdk")
//include(":document_reader_sdk")
