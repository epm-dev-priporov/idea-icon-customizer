package dev.priporov.customicons.icon

import com.intellij.openapi.components.Service
import java.io.File
import java.net.URL
import java.nio.file.FileSystems

@Service
class IconImporter {

    private val fileSeparator: String = FileSystems.getDefault().getSeparator() ?: File.pathSeparator

    val iconDir = "${System.getProperty("user.home")}${fileSeparator}.ideaIconCustomizer"

    fun import() {
        val resource: URL = IconImporter::class.java.getResource("/icons")
        val file = File(iconDir)
        if (!file.exists()) {
            file.mkdir()
        }

        File(resource.toURI()).copyRecursively(file, overwrite = true)
    }

}