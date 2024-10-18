package dev.priporov.customicons.extension

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator
import com.intellij.openapi.components.service
import com.intellij.psi.impl.file.PsiDirectoryImpl
import com.intellij.psi.impl.file.PsiJavaDirectoryImpl
import com.intellij.psi.impl.source.PsiClassImpl
import com.intellij.psi.impl.source.PsiFileImpl
import dev.priporov.customicons.pattern.common.ConditionType
import dev.priporov.customicons.pattern.common.FileType
import dev.priporov.customicons.pattern.item.BaseConditionItem
import dev.priporov.customicons.service.SettingsListModelService
import javax.swing.Icon

class ProjectViewIconNodeDecorator : ProjectViewNodeDecorator {

    override fun decorate(node: ProjectViewNode<*>?, presentationData: PresentationData?) {
        val items = service<SettingsListModelService>().getItems()
        val value = node?.value

        if (value == null || presentationData == null || items.isEmpty()) {
            return
        }

        val text = presentationData.presentableText!!
        val name = text.substringBeforeLast(".")
        val extension = text.substringAfterLast(".")

        items.asSequence()
            .filterNot { it.disabled }
            .forEach { item ->
                if (item.fileType == FileType.FOLDER) {
                    if (value is PsiJavaDirectoryImpl || value is PsiDirectoryImpl) {
                        getIcon(item, name, extension)?.also {
                            presentationData.setIcon(it)

                        }
                    }
                }
                if (item.fileType == FileType.FILE) {
                    if (value is PsiClassImpl || value is PsiFileImpl) {
                        getIcon(item, name, extension)?.also {
                            presentationData.setIcon(it)
                        }
                    }
                }
            }
    }

    fun getIcon(item: BaseConditionItem, name: String, extension: String?): Icon? {
        when (item.conditionType) {
            ConditionType.REGEXP -> {
                if (Regex(item.condition).matches(name)) {
                    return item.icon
                }
            }

            ConditionType.EQUALS -> {
                if (name.equals(item.condition)) {
                    return item.icon
                }
            }

            ConditionType.CONTAINS -> {
                if (name.contains(item.condition)) {
                    return item.icon
                }
            }

            ConditionType.EXTENSION_EQUALS -> {
                if (extension == item.condition) {
                    return item.icon
                }
            }
        }
        return null
    }
}