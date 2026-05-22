package com.example.myapplication
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class StyleSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_style_select)

        val radioEnergetic = findViewById<RadioButton>(R.id.radio_energetic)
        val radioBold = findViewById<RadioButton>(R.id.radio_bold)
        val radioCaring = findViewById<RadioButton>(R.id.radio_caring)
        val radioStrict = findViewById<RadioButton>(R.id.radio_strict)
        val btnContinue = findViewById<Button>(R.id.btn_continue)

        val cardEnergetic = findViewById<CardView>(R.id.card_energetic)
        val cardBold = findViewById<CardView>(R.id.card_bold)
        val cardCaring = findViewById<CardView>(R.id.card_caring)
        val cardStrict = findViewById<CardView>(R.id.card_strict)

        val allRadios = listOf(radioEnergetic, radioBold, radioCaring, radioStrict)

        // Отключаем встроенную обработку кликов
        for (radio in allRadios) {
            radio.isClickable = false
        }

        // Обработчик для каждой пары карточка + радиокнопка
        setupCardWithRadio(cardEnergetic, radioEnergetic, allRadios)
        setupCardWithRadio(cardBold, radioBold, allRadios)
        setupCardWithRadio(cardCaring, radioCaring, allRadios)
        setupCardWithRadio(cardStrict, radioStrict, allRadios)

        btnContinue.setOnClickListener {
            val selected = when {
                radioEnergetic.isChecked -> "Энергичный"
                radioBold.isChecked -> "Дерзкий"
                radioCaring.isChecked -> "Заботливый"
                radioStrict.isChecked -> "Строгий"
                else -> null
            }

            if (selected == null) {
                Toast.makeText(this, "Выберите стиль общения", Toast.LENGTH_SHORT).show()
            } else {
                // Переход на новый экран
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("SELECTED_STYLE", selected)
                startActivity(intent)
            }
        }
    }

    private fun setupCardWithRadio(card: CardView, radio: RadioButton, allRadios: List<RadioButton>) {
        val clickListener = {
            if (radio.isChecked) {
                // Уже выбрана — снимаем
                radio.isChecked = false
            } else {
                // Не выбрана — снимаем все и выбираем эту
                uncheckAll(allRadios)
                radio.isChecked = true
            }
        }

        // Вешаем слушатель только на карточку
        card.setOnClickListener { clickListener() }
    }

    private fun uncheckAll(radios: List<RadioButton>) {
        for (radio in radios) {
            radio.isChecked = false
        }
    }
}