import de.fayard.dependencies.bootstrapRefreshVersionsAndDependencies

rootProject.name = "2p"

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("de.fayard:dependencies:0.+")
    }
}

bootstrapRefreshVersionsAndDependencies()

include("documentation")
include("utils")
include("core")
include("unify")
include("theory")
include("bdd")
include("dsl-core")
include("dsl-unify")
include("dsl-theory")
include("dsl-solve")
include("solve")
include("solve-classic")
include("solve-streams")
include("solve-concurrent")
include("test-solve")
include("parser-core")
include("parser-jvm")
include("parser-js")
include("parser-theory")
include("solve-plp")
include("solve-problog")
include("serialize-core")
include("serialize-theory")
include("repl")
include("oop-lib")
include("io-lib")
include("ide-plp")
include("ide")
include("examples")
