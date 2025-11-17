@file:Suppress("UnstableApiUsage")

include(":feature:main")


include(":feature:saving")


include(":feature:budgeting")


include(":feature:transaction")


include(":feature:billing")


include(":feature:home")


include(":feature:analytics")


include(":feature:account")


include(":feature:settings")


include(":core:data")


include(":core:domain")


include(":core:presentation")


pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Monu"
include(":app")
 