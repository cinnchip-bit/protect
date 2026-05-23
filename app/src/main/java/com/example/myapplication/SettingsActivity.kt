package com.example.myapplication

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var etLimit: EditText
    private lateinit var tvCurrentLimit: TextView
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        etLimit = findViewById(R.id.et_limit)
        tvCurrentLimit = findViewById(R.id.tv_current_limit)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnSave = findViewById<Button>(R.id.btn_save_limit)
        val btnRemove = findViewById<Button>(R.id.btn_remove_limit)

        prefs = getSharedPreferences("finance_prefs", MODE_PRIVATE)

        // Показать текущий лимит
        val currentLimit = prefs.getFloat("monthly_limit", -1f)
        if (currentLimit > 0) {
            tvCurrentLimit.text = "Текущий лимит: ${String.format("%,.0f P", currentLimit).replace(',', ' ')}"
            etLimit.setText(String.format("%,.0f", currentLimit).replace(',', '.'))
        }

        // Фильтр ввода
        etLimit.addTextChangedListener(object : TextWatcher {
            private var isEditing = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isEditing) return
                isEditing = true
                val filtered = s.toString().replace(Regex("[^0-9.,]"), "")
                if (filtered != s.toString()) {
                    s?.replace(0, s.length, filtered)
                }
                isEditing = false
            }
        })

        btnBack.setOnClickListener { finish() }

        btnSave.setOnClickListener {
            val limitText = etLimit.text.toString().replace(',', '.')
            if (limitText.isEmpty() || limitText.toFloatOrNull() == null || limitText.toFloat() <= 0) {
                Toast.makeText(this, "Введите корректную сумму", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val limit = limitText.toFloat()
            prefs.edit().putFloat("monthly_limit", limit).apply()
            Toast.makeText(this, "Лимит сохранён!", Toast.LENGTH_SHORT).show()

            // Отправляем результат обратно
            val resultIntent = Intent().apply {
                putExtra("MONTHLY_LIMIT", limit)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        btnRemove.setOnClickListener {
            prefs.edit().remove("monthly_limit").apply()
            Toast.makeText(this, "Лимит убран", Toast.LENGTH_SHORT).show()
            val resultIntent = Intent().apply {
                putExtra("MONTHLY_LIMIT", -1f)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}