import com.vanniktech.maven.publish.JavaLibrary
import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.SourcesJar
import net.ltgt.gradle.errorprone.CheckSeverity
import net.ltgt.gradle.errorprone.errorprone

plugins {
    `java-library`
    jacoco
    alias(libs.plugins.spotless)
    alias(libs.plugins.errorprone)
    alias(libs.plugins.maven.publish)
}

group = "com.qeetgroup"

/** The Java release the library is compiled for: its baseline (docs/engineering-practices.md). */
val baselineJava = 17

/** The JDK that compiles: the newest LTS, which Error Prone needs (21 or later) and javac's best. */
val buildJava = 25

/** The JDK the tests run on; CI runs every supported LTS with -PtestJdk. */
val testJava = providers.gradleProperty("testJdk").map(String::toInt).getOrElse(baselineJava)

java {
    toolchain { languageVersion = JavaLanguageVersion.of(buildJava) }
}

dependencies {
    // The one runtime dependency. Kept out of the public API, so it can change without breaking callers.
    implementation(libs.jackson.databind)
    // Nullness annotations on the public API; on the compile classpath so Kotlin and IDEs read them.
    api(libs.jspecify)

    errorprone(libs.errorprone.core)
    errorprone(libs.nullaway)

    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
}

// Every resolved version is recorded in gradle.lockfile: builds are reproducible, and the
// dependency scan (.github/workflows/security.yml) reads exactly what ships.
dependencyLocking { lockAllConfigurations() }

tasks.withType<JavaCompile>().configureEach {
    options.release = baselineJava
    options.encoding = "UTF-8"
    // "exports" is off: resource constructors take the internal Transport. They are public only so
    // QeetID can build them across packages; callers get resources from QeetID.
    options.compilerArgs.addAll(listOf("-Xlint:all,-exports", "-Werror"))
    options.errorprone {
        disableWarningsInGeneratedCode = true
        check("NullAway", CheckSeverity.ERROR)
        option("NullAway:OnlyNullMarked", "true")
        option("NullAway:JSpecifyMode", "true")
    }
}

tasks.compileTestJava {
    // Tests poke at nulls on purpose, and hold raw bytes in records they never compare.
    options.errorprone.disable("NullAway", "ArrayRecordComponent")
}

tasks.processResources {
    val version = project.version.toString()
    inputs.property("version", version)
    filesMatching("**/version.properties") { expand("version" to version) }
}

tasks.test {
    useJUnitPlatform()
    javaLauncher = javaToolchains.launcherFor { languageVersion = JavaLanguageVersion.of(testJava) }
    systemProperty("junit.jupiter.execution.timeout.default", "60 s")
    systemProperty("expectedJavaFeature", testJava.toString())
    finalizedBy(tasks.jacocoTestReport)
}

jacoco { toolVersion = libs.versions.jacoco.get() }

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.required = true
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule { limit { minimum = "0.85".toBigDecimal() } }
    }
}

tasks.check { dependsOn(tasks.jacocoTestCoverageVerification) }

tasks.javadoc {
    options {
        this as StandardJavadocDocletOptions
        encoding = "UTF-8"
        addBooleanOption("Xdoclint:all,-missing", true)
        addBooleanOption("Xwerror", true)
        addStringOption("-release", baselineJava.toString())
    }
}

tasks.jar {
    manifest {
        attributes(
            "Implementation-Title" to "Qeet ID for Java",
            "Implementation-Version" to project.version.toString(),
            "Implementation-Vendor" to "Qeet Group",
        )
    }
}

spotless {
    java {
        target("src/**/*.java")
        googleJavaFormat(libs.versions.google.java.format.get())
        formatAnnotations()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

mavenPublishing {
    configure(JavaLibrary(javadocJar = JavadocJar.Javadoc(), sourcesJar = SourcesJar.Sources()))
    publishToMavenCentral()
    signAllPublications()
    coordinates(group.toString(), "qeet-id", version.toString())
    pom {
        name = "Qeet ID for Java"
        description = "The server-side Java SDK for Qeet ID: the management API, webhook and access-token verification, and OAuth."
        inceptionYear = "2026"
        url = "https://github.com/qeetgroup/qeet-id-java"
        organization {
            name = "Qeet Group"
            url = "https://qeet.in"
        }
        developers {
            developer {
                id = "qeetgroup"
                name = "Qeet Group"
                url = "https://github.com/qeetgroup"
            }
        }
        scm {
            url = "https://github.com/qeetgroup/qeet-id-java"
            connection = "scm:git:https://github.com/qeetgroup/qeet-id-java.git"
            developerConnection = "scm:git:ssh://git@github.com/qeetgroup/qeet-id-java.git"
        }
        issueManagement {
            system = "GitHub"
            url = "https://github.com/qeetgroup/qeet-id-java/issues"
        }
    }
}
