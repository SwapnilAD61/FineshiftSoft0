plugins {
    `kotlin-jvm-js`
    `kotlin-doc`
    `publish-on-maven`
    `publish-on-npm`
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":dsl-core"))
                api(project(":unify"))
            }
        }
    }
}
