plugins {
    id("io.cloudflight.autoconfigure-settings") version "1.1.1"
}

rootProject.name = "structurizr-autoconfigure"

configure<org.ajoberstar.reckon.gradle.ReckonExtension> {
    setScopeCalc(calcScopeFromCommitMessages())
}