plugins {
    `java-library`
    alias(libs.plugins.gradleJavaConventions)
}

group = "com.opencastsoftware"
description = "WebAssembly toolkit for Java"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

dependencies {
    compileOnly(libs.jetBrainsAnnotations)
    testImplementation(libs.junitJupiter)
    testImplementation(libs.hamcrest)
    testImplementation(libs.jqwik)
}

mavenPublishing {
    coordinates("com.opencastsoftware", "wasm4j", project.version.toString())

    pom {
        name.set("wasm4j")
        description.set(project.description)
        url.set("https://github.com/opencastsoftware/wasm4j")
        inceptionYear.set("2023")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        organization {
            name.set("Opencast Software Europe Ltd")
            url.set("https://opencastsoftware.com")
        }
        developers {
            developer {
                id.set("DavidGregory084")
                name.set("David Gregory")
                organization.set("Opencast Software Europe Ltd")
                organizationUrl.set("https://opencastsoftware.com/")
                timezone.set("Europe/London")
                url.set("https://github.com/DavidGregory084")
            }
        }
        ciManagement {
            system.set("Github Actions")
            url.set("https://github.com/opencastsoftware/wasm4j/actions")
        }
        issueManagement {
            system.set("GitHub")
            url.set("https://github.com/opencastsoftware/wasm4j/issues")
        }
        scm {
            connection.set("scm:git:https://github.com/opencastsoftware/wasm4j.git")
            developerConnection.set("scm:git:git@github.com:opencastsoftware/wasm4j.git")
            url.set("https://github.com/opencastsoftware/wasm4j")
        }
    }
}

tasks.named<Test>("test") {
    useJUnitPlatform {
        includeEngines("junit-jupiter", "jqwik")
    }
}

// Create an integration test source set and configuration
testing {
    suites {
        val integrationTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project())
                implementation(libs.apacheCommonsLang)
                implementation(libs.junitJupiter)
                implementation(libs.hamcrest)
            }
        }
    }
}

tasks.check {
    dependsOn(testing.suites.named("integrationTest"))
}