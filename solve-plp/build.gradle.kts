plugins {
    `kotlin-jvm-js`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":solve"))
                api(project(":bdd"))
            }
        }
    }
}
