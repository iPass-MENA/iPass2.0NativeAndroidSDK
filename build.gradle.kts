buildscript {
    val agp_version by extra("8.4.2")
    val agp_version1 by extra("8.4.1")
}
plugins {
    id("com.android.application") version "8.9.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}