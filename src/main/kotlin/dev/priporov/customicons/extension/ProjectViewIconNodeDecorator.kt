package dev.priporov.customicons.extension

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.projectView.ProjectViewNode
import com.intellij.ide.projectView.ProjectViewNodeDecorator
import com.intellij.ide.projectView.impl.nodes.PsiFileNode
import com.intellij.openapi.components.service
import com.intellij.psi.PsiDirectory
import com.intellij.psi.impl.source.PsiClassImpl
import com.intellij.psi.impl.source.PsiFileImpl
import dev.priporov.customicons.pattern.common.ConditionType
import dev.priporov.customicons.pattern.common.FileType
import dev.priporov.customicons.pattern.item.BaseConditionItem
import dev.priporov.customicons.service.SettingsListModelService
import org.jetbrains.kotlin.psi.KtClass
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
                    if (value is PsiDirectory) {
                        getIcon(item, name, extension)?.also {
                            presentationData.setIcon(it)
                        }
                    }
                } else if (item.fileType == FileType.FILE) {
                    if (value is PsiClassImpl || value is PsiFileImpl || value is KtClass || value is PsiFileNode) {
                        getIcon(item, name, extension)?.also {
                            presentationData.setIcon(it)
                        }
                    }
                }
            }
    }

    fun getIcon(item: BaseConditionItem, name: String, extension: String?): Icon? {
        val icon = item.iconContainer?.icon

        when (item.conditionType) {
            ConditionType.REGEXP -> {
                if (Regex(item.condition).matches(name)) {
                    return icon
                }
            }
            ConditionType.EQUALS -> {
                if (name.equals(item.condition)) {
                    return icon
                }
            }
            ConditionType.CONTAINS -> {
                if (name.contains(item.condition)) {
                    return icon
                }
            }
            ConditionType.EXTENSION -> {
                if (extension == item.condition) {
                    return icon
                }
            }
            ConditionType.ENDS_WITH -> {
                if (name.endsWith(item.condition)) {
                    return icon
                }
            }
            ConditionType.STARTS_WITH -> {
                if (name.startsWith(item.condition)) {
                    return icon
                }
            }
        }
        return null
    }
}