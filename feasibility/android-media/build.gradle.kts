import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

plugins {
    id("com.android.application") version "9.2.0"
}

abstract class CopySessionProbe : DefaultTask() {
    @get:InputFile abstract val sessionSource: RegularFileProperty
    @get:OutputDirectory abstract val outputDirectory: DirectoryProperty

    @TaskAction fun copySource() {
        val target = outputDirectory.get().file("livestream/feasibility/StreamSession.kt").asFile
        target.parentFile.mkdirs()
        sessionSource.get().asFile.copyTo(target, overwrite = true)
    }
}

val copySessionProbe = tasks.register<CopySessionProbe>("copySessionProbe") {
    sessionSource.set(layout.projectDirectory.file("../src/main/kotlin/livestream/feasibility/StreamSession.kt"))
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
        versionName = "m2-media-1"
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
