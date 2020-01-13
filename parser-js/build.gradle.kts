plugins {
    kotlin("js")
//    id("maven-publish")
//    signing
//    id("org.jetbrains.dokka")
//    id("com.jfrog.bintray")
//    `java-library`
//    antlr
}

buildscript {
    dependencies {
        classpath("org.antlr", "antlr4", "4.7.2")
    }
}

val javaVersion: String by project
val antlrVersion: String by project
val ktFreeCompilerArgs: String by project

val generatedSrcDir = "$buildDir/generated-src/antlr/main"

kotlin {
    target {
        nodejs()
    }

    with(sourceSets["main"]) {
        dependencies {
            api(npm("antlr4", "^$antlrVersion"))
        }
    }
}

dependencies {

//    antlr("org.antlr", "antlr4", antlrVersion)

//    api("org.antlr", "antlr4-runtime", antlrVersion)

//    testImplementation("pl.pragmatists:JUnitParams:1.1.1")
//    implementation(kotlin("stdlib-jdk8"))
}


//configure<JavaPluginConvention> {
//    targetCompatibility = JavaVersion.valueOf("VERSION_1_$javaVersion")
//    sourceCompatibility = JavaVersion.valueOf("VERSION_1_$javaVersion")
//}

//tasks.withType<KotlinCompile> {
//    kotlinOptions {
//        jvmTarget = "1.$javaVersion"
//        freeCompilerArgs = ktFreeCompilerArgs.split(";").toList()
//    }
//}

//configurations {
//    compile {
//        setExtendsFrom(emptyList())
//    }
//}

//tasks.generateGrammarSource {
//    maxHeapSize = "64m"
//    arguments = arguments + listOf("-visitor", "-long-messages")
//    outputDirectory = File("${project.buildDir}/generated-src/antlr/main/it/unibo/tuprolog/parser")
//}