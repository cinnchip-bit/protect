package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
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
        val btnBack = findViewById<ImageButton>(R.id.btn_back)

        val cardEnergetic = findViewById<CardView>(R.id.card_energetic)
        val cardBold = findViewById<CardView>(R.id.card_bold)
        val cardCaring = findViewById<CardView>(R.id.card_caring)
        val cardStrict = findViewById<CardView>(R.id.card_strict)

        val allRadios = listOf(radioEnergetic, radioBold, radioCaring, radioStrict)

        for (radio in allRadios) {
            radio.isClickable = false
        }

        setupCardWithRadio(cardEnergetic, radioEnergetic, allRadios)
        setupCardWithRadio(cardBold, radioBold, allRadios)
        setupCardWithRadio(cardCaring, radioCaring, allRadios)
        setupCardWithRadio(cardStrict, radioStrict, allRadios)

        btnContinue.setOnClickListener {
            val selected = when {
                radioEnergetic.isChecked -> "Монах"
                radioBold.isChecked -> "Темщик"
                radioCaring.isChecked -> "Заботливый"
                radioStrict.isChecked -> "Тренер"
                else -> null
            }

            if (selected == null) {
                Toast.makeText(this, "Выберите стиль общения", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("SELECTED_STYLE", selected)
                startActivity(intent)
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun setupCardWithRadio(card: CardView, radio: RadioButton, allRadios: List<RadioButton>) {
        val clickListener = {
            if (radio.isChecked) {
                radio.isChecked = false
            } else {
                uncheckAll(allRadios)
                radio.isChecked = true
            }
        }
        card.setOnClickListener { clickListener() }
    }

    private fun uncheckAll(radios: List<RadioButton>) {
        for (radio in radios) {
            radio.isChecked = false
        }
    }
}