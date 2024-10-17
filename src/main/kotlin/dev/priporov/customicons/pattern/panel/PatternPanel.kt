package dev.priporov.customicons.pattern.panel

import dev.priporov.customicons.pattern.FileType
import dev.priporov.customicons.pattern.PatternType
import dev.priporov.customicons.pattern.RegexpPatternItem
import javax.swing.*

class PatternPanel {
    lateinit var root: JPanel

    private lateinit var conditionField: JTextField
    private lateinit var disabledCheckBox: JCheckBox
    private lateinit var conditionTypeBox: JComboBox<PatternType>
    private lateinit var fileTypeBox: JComboBox<FileType>
    private lateinit var applyButton: JButton
    private lateinit var buttonIcon: JButton
    private lateinit var imageIconPanel: JPanel

    init {
        PatternType.entries.forEach { type ->
            conditionTypeBox.addItem(type)
        }
        FileType.entries.forEach { fileType -> fileTypeBox.addItem(fileType) }
        root.isVisible = false
    }

    fun showSelectedItem(item: RegexpPatternItem){
        conditionField.text = item.condition

        val imageIcon = ImageIcon("/home/priporov/.iconscustomizer/repository.png")
        val jLabel = JLabel(imageIcon)

        imageIconPanel.add(jLabel, "1,1")
        root.isVisible = true
    }


}