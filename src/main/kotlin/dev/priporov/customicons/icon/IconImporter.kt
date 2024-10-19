package dev.priporov.customicons.icon

import com.intellij.openapi.components.Service
import java.io.File
import java.lang.invoke.MethodHandles
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension

@Service
class IconImporter {

    private val fileSeparator: String = FileSystems.getDefault().getSeparator() ?: File.pathSeparator

    val iconDir = "${System.getProperty("user.home")}${fileSeparator}.ideaIconCustomizer"

    fun import() {

        val file = File(iconDir)
        if (!file.exists()) {
            file.mkdir()
        }
        val resourceDir = "/icons"
        val lookupClass = MethodHandles.lookup().lookupClass()
        val resource = lookupClass.getResource(resourceDir)

        val paths: MutableList<Path> =
            FileSystems.newFileSystem(resource.toURI(), emptyMap<String, String>()).use { fs ->
                Files.walk(fs.getPath(resourceDir)).toList()
            }
        paths.map {
            if (it.extension.isBlank()) {
                return@map
            }

            lookupClass.getResourceAsStream(it.toString())?.readAllBytes()?.also { bytes ->

                val newIcon = File("$iconDir$it")
                if (!newIcon.parentFile.exists()) {
                    newIcon.parentFile.mkdirs()
                }
                if (!newIcon.exists()) {
                    newIcon.createNewFile()
                }
                newIcon.writeBytes(bytes)
            }

        }


    }

}