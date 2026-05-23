package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddIncomeActivity : AppCompatActivity() {

    private var selectedCategory = "Зарплата"
    private lateinit var tvSelectedCategory: TextView
    private lateinit var etAmount: EditText
    private lateinit var etNote: EditText
    private lateinit var tvDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_income)

        tvSelectedCategory = findViewById(R.id.tv_selected_category)
        etAmount = findViewById(R.id.et_amount)
        etNote = findViewById(R.id.et_note)
        tvDate = findViewById(R.id.tv_date)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnSave = findViewById<Button>(R.id.btn_save)
        val layoutDate = findViewById<LinearLayout>(R.id.layout_date)

        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        tvDate.text = sdf.format(Calendar.getInstance().time)

        etAmount.addTextChangedListener(object : TextWatcher {
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

        setupCategoryClick(R.id.cat_salary, "Зарплата")
        setupCategoryClick(R.id.cat_gift, "Подарок")
        setupCategoryClick(R.id.cat_freelance, "Подработка")
        setupCategoryClick(R.id.cat_investment, "Инвестиции")
        setupCategoryClick(R.id.cat_other_income, "Другое")

        layoutDate.setOnClickListener { showDatePicker() }
        btnSave.setOnClickListener { saveTransaction() }
    }

    private fun setupCategoryClick(id: Int, category: String) {
        findViewById<LinearLayout>(id).setOnClickListener {
            selectedCategory = category
            tvSelectedCategory.text = "Выбрано: $category"
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            tvDate.text = String.format("%02d.%02d.%d", day, month + 1, year)
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun saveTransaction() {
        val amountText = etAmount.text.toString().replace(',', '.')
        if (amountText.isEmpty() || amountText.toDoubleOrNull() == null || amountText.toDouble() <= 0) {
            Toast.makeText(this, "Введите корректную сумму", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDouble()
        val note = etNote.text.toString().trim()
        val date = tvDate.text.toString()

        val transaction = Transaction(
            type = "income",
            category = selectedCategory,
            amount = amount,
            note = note,
            date = date
        )

        val resultIntent = Intent().apply {
            putExtra("TRANSACTION", transaction)
        }
        setResult(RESULT_OK, resultIntent)

        Toast.makeText(this, "Доход сохранён!", Toast.LENGTH_SHORT).show()
        finish()
    }
}