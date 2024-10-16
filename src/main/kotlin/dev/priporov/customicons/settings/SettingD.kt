package dev.priporov.customicons.settings

import dev.priporov.customicons.pattern.RegexpPatternItem
import javax.swing.*
import javax.swing.event.ListSelectionEvent
import javax.swing.event.ListSelectionListener

class SettingD {

    lateinit var root: JPanel
    private lateinit var patternPanel: JPanel
    private lateinit var patternList: JList<RegexpPatternItem>
    private lateinit var textField1: JTextField
    private lateinit var disableCheckBox: JCheckBox
    private lateinit var applyButton: JButton
    private lateinit var comboBox1: JComboBox<*>

    init {
        val toolbar = NoteToolbarFactory.getInstance(patternList)

        root.add(toolbar)

        patternPanel.apply {
            isVisible = false
        }

        patternList.apply {
            addListSelectionListener(
                ItemListSelectionListener(
                    patternPanel,
                    patternList,
                    textField1,
                    disableCheckBox,
                    applyButton,
                    comboBox1
                )
            )
            setListData(arrayOf(RegexpPatternItem("./*")))
        }
        patternPanel.apply {

        }

    }


    class ItemListSelectionListener(
        val patternPanel: JPanel,
        val patternList: JList<*>,
        val textField1: JTextField,
        val disableCheckBox: JCheckBox,
        val applyButton: JButton,
        val comboBox1: JComboBox<*>
    ) : ListSelectionListener {

        override fun valueChanged(e: ListSelectionEvent) {
            val selectedValue: RegexpPatternItem = (e.source as JList<*>).selectedValue as RegexpPatternItem

            textField1.setText(selectedValue.regex)

            patternPanel.isVisible = true
        }
    }

}