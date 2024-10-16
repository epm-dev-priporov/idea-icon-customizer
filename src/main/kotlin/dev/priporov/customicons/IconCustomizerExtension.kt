package dev.priporov.customicons

import com.intellij.ide.IconProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.file.PsiJavaDirectoryImpl
import com.intellij.psi.impl.source.PsiClassImpl
import com.intellij.psi.impl.source.PsiMethodImpl
import javax.swing.Icon
import javax.swing.ImageIcon

class IconCustomizerExtension: IconProvider() {

    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        if(element is PsiClassImpl){
            if(element.name?.contains("Repository")?:false) {
                return ImageIcon("/home/priporov/.iconscustomizer/repository.png")
            }
            return ImageIcon("/home/priporov/.iconscustomizer/icons8-file-16.png")
        }
        if(element is PsiJavaDirectoryImpl){

        }
        return null
    }

}