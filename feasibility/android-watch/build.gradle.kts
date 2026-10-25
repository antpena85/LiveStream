import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

plugins { id("com.android.application") version "9.2.0" }

abstract class CopyCorrelationProbe : DefaultTask() {
    @get:InputFile abstract val source: RegularFileProperty
    @get:OutputDirectory abstract val outputDirectory: DirectoryProperty
    @TaskAction fun copySource() {
        val target = outputDirectory.get().file("livestream/feasibility/PendingProbe.kt").asFile
        target.parentFile.mkdirs()
        source.get().asFile.copyTo(target, overwrite = true)
    }
}
val copyCorrelation = tasks.register<CopyCorrelationProbe>("copyCorrelation") {
    source.set(layout.projectDirectory.file("../src/main/kotlin/livestream/feasibility/PendingProbe.kt"))
    outputDirectory.set(layout.buildDirectory.dir("generated/correlation"))
}
android {
    namespace = "livestream.feasibility.watchprobe"
    compileSdk = 37
    defaultConfig {
        applicationId = "livestream.feasibility.watchprobe"
        minSdk = 31
        targetSdk = 37
        versionCode = 1
        versionName = "m2-watch-1"
    }
    signingConfigs.getByName("debug") {
        storeFile = rootProject.file("../../.tools/m2/inventory-debug.keystore")
    }
}
androidComponents.onVariants { variant ->
    variant.sources.kotlin!!.addGeneratedSourceDirectory(copyCorrelation, CopyCorrelationProbe::outputDirectory)
}
dependencies { implementation("com.google.android.gms:play-services-wearable:20.0.1") }