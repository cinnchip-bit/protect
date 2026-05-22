package com.example.myapplication

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
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

class AddTransactionActivity : AppCompatActivity() {

    private var selectedType = "expense"
    private var selectedCategory = "🍕 Еда"
    private lateinit var tvSelectedCategory: TextView
    private lateinit var etAmount: EditText
    private lateinit var etNote: EditText
    private lateinit var tvDate: TextView
    private lateinit var btnExpenseTab: Button
    private lateinit var btnIncomeTab: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        tvSelectedCategory = findViewById(R.id.tv_selected_category)
        etAmount = findViewById(R.id.et_amount)
        etNote = findViewById(R.id.et_note)
        tvDate = findViewById(R.id.tv_date)
        btnExpenseTab = findViewById(R.id.btn_expense_tab)
        btnIncomeTab = findViewById(R.id.btn_income_tab)
        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        val btnSave = findViewById<Button>(R.id.btn_save)
        val layoutDate = findViewById<LinearLayout>(R.id.layout_date)

        // Установка текущей даты
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        tvDate.text = sdf.format(Calendar.getInstance().time)

        // Приём типа транзакции с главного экрана
        val transactionType = intent.getStringExtra("TRANSACTION_TYPE") ?: "expense"
        if (transactionType == "income") {
            selectType("income")
        } else {
            selectType("expense")
        }

        // Стрелка назад — возврат на главный экран
        btnBack.setOnClickListener { finish() }

        // Переключатель Расход / Доход
        btnExpenseTab.setOnClickListener { selectType("expense") }
        btnIncomeTab.setOnClickListener { selectType("income") }

        // Выбор категории
        setupCategoryClick(R.id.cat_food, "🍕 Еда")
        setupCategoryClick(R.id.cat_transport, "🚗 Транспорт")
        setupCategoryClick(R.id.cat_entertainment, "🎮 Развлечения")
        setupCategoryClick(R.id.cat_shopping, "🛒 Покупки")
        setupCategoryClick(R.id.cat_health, "💊 Здоровье")
        setupCategoryClick(R.id.cat_education, "📚 Учёба")
        setupCategoryClick(R.id.cat_home, "🏠 Жильё")
        setupCategoryClick(R.id.cat_other, "❓ Другое")

        // Выбор даты
        layoutDate.setOnClickListener { showDatePicker() }

        // Сохранение
        btnSave.setOnClickListener { saveTransaction() }
    }

    private fun selectType(type: String) {
        selectedType = type
        if (type == "expense") {
            btnExpenseTab.backgroundTintList = ColorStateList.valueOf(getColor(android.R.color.holo_red_light))
            btnExpenseTab.setTextColor(getColor(android.R.color.white))
            btnIncomeTab.backgroundTintList = ColorStateList.valueOf(getColor(android.R.color.transparent))
            btnIncomeTab.setTextColor(getColor(android.R.color.darker_gray))
        } else {
            btnIncomeTab.backgroundTintList = ColorStateList.valueOf(getColor(android.R.color.holo_green_light))
            btnIncomeTab.setTextColor(getColor(android.R.color.white))
            btnExpenseTab.backgroundTintList = ColorStateList.valueOf(getColor(android.R.color.transparent))
            btnExpenseTab.setTextColor(getColor(android.R.color.darker_gray))
        }
    }

    private fun setupCategoryClick(id: Int, category: String) {
        findViewById<LinearLayout>(id).setOnClickListener {
            selectedCategory = category
            tvSelectedCategory.text = "Выбрано: $category"
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                tvDate.text = String.format("%02d.%02d.%d", day, month + 1, year)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun saveTransaction() {
        val amountText = etAmount.text.toString()
        if (amountText.isEmpty() || amountText.toDoubleOrNull() == null || amountText.toDouble() <= 0) {
            Toast.makeText(this, "Введите корректную сумму", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDouble()
        val note = etNote.text.toString()
        val date = tvDate.text.toString()

        // TODO: сохранить в базу данных через Room
        // val transaction = Transaction(
        //     type = selectedType,
        //     category = selectedCategory,
        //     amount = amount,
        //     note = note.ifEmpty { null },
        //     date = date
        // )
        // repository.insert(transaction)

        Toast.makeText(this, "✅ Запись сохранена!", Toast.LENGTH_SHORT).show()
        finish()
    }
}