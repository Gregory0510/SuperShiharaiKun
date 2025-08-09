rootProject.name = "ktor-sample-app"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        // Add the following to enable toolchain auto-provisioning:
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
	gradlePluginPortal()
    }
}