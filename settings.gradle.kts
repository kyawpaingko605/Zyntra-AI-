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
        maven { url = java.net.URI("https://jitpack.io") } // Markdown library ဖတ်ရန်
    }
}

rootProject.name = "ZyntraAI"
include(":app") // 👈 app ဖိုဒါကို လှမ်းချိတ်ဆက်ခြင်း
