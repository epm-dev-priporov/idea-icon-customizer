package dev.priporov.customicons.pattern.panel

import javax.swing.*

class PatternPanel {
    lateinit var root: JPanel

    private lateinit var textField1: JTextField
    private lateinit var disabledCheckBox: JCheckBox
    private lateinit var comboBox1: JComboBox<*>
    private lateinit var applyButton: JButton
    private lateinit var buttonIcon: JButton
    private lateinit var imageIconPanel: JPanel

    init {

    }

    fun method(text:String){
//        imageIconPanel.add(JLabel(ImageIcon("/home/priporov/.iconscustomizer/repository.png")))
        textField1.text = text

        val imageIcon = ImageIcon("/home/priporov/.iconscustomizer/repository.png")
        val jLabel = JLabel(imageIcon)

        imageIconPanel.add(jLabel, "1,1")
    }
}