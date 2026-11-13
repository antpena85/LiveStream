import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.security.MessageDigest

plugins {
    id("com.android.application") version "9.2.0"
}

// Stable for identical source inputs, including uncommitted changes; not a release version.
val diagnosticDigest = MessageDigest.getInstance("SHA-256")
val diagnosticInputs = (fileTree("src/main") { include("**/*") }.files +
    fileTree("../src/main/kotlin/livestream/feasibility") { include("*.kt") }.files +
    setOf(file("build.gradle.kts"))).filter { it.isFile }.sortedBy { it.absolutePath }
diagnosticInputs.forEach { input ->
    diagnosticDigest.update(input.name.toByteArray(Charsets.UTF_8))
    diagnosticDigest.update(input.readBytes())
}
val diagnosticVersion = "m2-dev-" + diagnosticDigest.digest().take(6).joinToString("") { "%02x".format(it.toInt() and 0xff) }

abstract class CopySessionProbe : DefaultTask() {
    @get:InputFile abstract val sessionSource: RegularFileProperty
    @get:InputFile abstract val scoreboardSource: RegularFileProperty
    @get:InputFile abstract val destinationSource: RegularFileProperty
    @get:InputFile abstract val streamSessionSource: RegularFileProperty
    @get:InputFile abstract val safetyStateSource: RegularFileProperty
    @get:InputFile abstract val manualPublisherSource: RegularFileProperty
    @get:OutputDirectory abstract val outputDirectory: DirectoryProperty

    @TaskAction fun copySource() {
        outputDirectory.get().asFile.deleteRecursively()
        val target = outputDirectory.get().file("livestream/feasibility/LocalCaptureReadiness.kt").asFile
        target.parentFile.mkdirs()
        sessionSource.get().asFile.copyTo(target, overwrite = true)
        scoreboardSource.get().asFile.copyTo(target.parentFile.resolve("DiagnosticScoreboard.kt"), overwrite = true)
        destinationSource.get().asFile.copyTo(target.parentFile.resolve("ManualDestination.kt"), overwrite = true)
        streamSessionSource.get().asFile.copyTo(target.parentFile.resolve("StreamSession.kt"), overwrite = true)
        safetyStateSource.get().asFile.copyTo(target.parentFile.resolve("SessionSafetyState.kt"), overwrite = true)
        manualPublisherSource.get().asFile.copyTo(target.parentFile.resolve("ManualPublisherControl.kt"), overwrite = true)
    }
}

val copySessionProbe = tasks.register<CopySessionProbe>("copySessionProbe") {
    sessionSource.set(layout.projectDirectory.file("../src/main/kotlin/livestream/feasibility/LocalCaptureReadiness.kt"))
    scoreboardSource.set(layout.projectDirectory.file("../src/main/kotlin/livestream/feasibility/DiagnosticScoreboard.kt"))
    destinationSource.set(layout.projectDirectory.file("../src/main/kotlin/livestream/feasibility/ManualDestination.kt"))
    streamSessionSource.set(layout.projectDirectory.file("../src/main/kotlin/livestream/feasibility/StreamSession.kt"))
    safetyStateSource.set(layout.projectDirectory.file("../src/main/kotlin/livestream/feasibility/SessionSafetyState.kt"))
    manualPublisherSource.set(layout.projectDirectory.file("../src/main/kotlin/livestream/feasibility/ManualPublisherControl.kt"))
    outputDirectory.set(layout.buildDirectory.dir("generated/session-probe"))
}

android {
    namespace = "livestream.feasibility.mediaprobe"
    compileSdk = 37
    defaultConfig {
        applicationId = "livestream.feasibility.mediaprobe"
        minSdk = 31
        targetSdk = 37
        versionCode = 1
        versionName = diagnosticVersion
        testInstrumentationRunner = "livestream.feasibility.mediaprobe.SafetyStoreInstrumentation"
    }
    signingConfigs.getByName("debug") {
        storeFile = rootProject.file("../../.tools/m2/inventory-debug.keystore")
    }
}

androidComponents.onVariants { variant ->
    variant.sources.kotlin!!.addGeneratedSourceDirectory(copySessionProbe, CopySessionProbe::outputDirectory)
}

dependencies {
    implementation("com.github.pedroSG94.RootEncoder:library:2.8.1")
}
