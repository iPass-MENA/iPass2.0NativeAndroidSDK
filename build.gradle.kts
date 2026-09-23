buildscript {
    val agp_version by extra("8.4.2")
    val agp_version1 by extra("8.4.1")
}
plugins {
    id("com.android.application") version "9.4.1" apply false
    id("com.android.library") version "9.4.1" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.4.20" apply false
}