package dev.priporov.customicons.icon

import com.intellij.openapi.application.ApplicationActivationListener
import com.intellij.openapi.components.Service
import com.intellij.openapi.wm.IdeFrame
import dev.priporov.customicons.state.Icon
import dev.priporov.customicons.state.PluginState
import java.io.File
import java.lang.invoke.MethodHandles
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension

val FILE_SEPARATOR: String = FileSystems.getDefault().getSeparator() ?: File.pathSeparator
val PLUGIN_DIR = "${System.getProperty("user.home")}${FILE_SEPARATOR}.ideaIconCustomizer"
val ICON_DIR = "${System.getProperty("user.home")}${FILE_SEPARATOR}.ideaIconCustomizer${FILE_SEPARATOR}icons"

@Service
class IconImporter : ApplicationActivationListener {

    // load icons on the first start
    override fun applicationActivated(ideFrame: IdeFrame) {
        val file = File(PLUGIN_DIR)
        if (!file.exists()) {
            file.mkdir()
            import("0")
        }
    }

    fun import(version: String) {
        if (Icon.VERSION.equals(version)) {
            return
        }
        val file = File(PLUGIN_DIR)
        if (!file.exists()) {
            file.mkdir()
        }

        val resourceDir = "${FILE_SEPARATOR}icons"
        val lookupClass = MethodHandles.lookup().lookupClass()
        val resource = lookupClass.getResource(resourceDir)

        val paths: MutableList<Path> = FileSystems.newFileSystem(
            resource.toURI(),
            emptyMap<String, String>()
        )
            .use { fs ->
                Files.walk(fs.getPath(resourceDir)).toList()
            }
        paths.map {
            if (it.extension.isBlank()) {
                return@map
            }

            lookupClass.getResourceAsStream(it.toString())?.readAllBytes()?.also { bytes ->

                val newIcon = File("$PLUGIN_DIR$it")
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