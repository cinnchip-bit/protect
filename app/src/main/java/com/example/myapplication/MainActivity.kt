package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {

    private lateinit var tvBalance: TextView
    private lateinit var tvIncome: TextView
    private lateinit var tvExpense: TextView
    private lateinit var viewProgress: View
    private lateinit var layoutProgressBg: LinearLayout
    private lateinit var layoutTransactions: LinearLayout

    private val transactionList = mutableListOf<Transaction>()
    private var totalIncome = 0.0
    private var totalExpense = 0.0

    companion object {
        private const val REQUEST_ADD_INCOME = 1
        private const val REQUEST_ADD_EXPENSE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val selectedStyle = intent.getStringExtra("SELECTED_STYLE") ?: "Не выбран"
        val tvSelectedStyle = findViewById<TextView>(R.id.tv_selected_style)
        tvSelectedStyle.text = "Выбран стиль: $selectedStyle"

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { finish() }

        tvBalance = findViewById(R.id.tv_balance)
        tvIncome = findViewById(R.id.tv_income)
        tvExpense = findViewById(R.id.tv_expense)
        viewProgress = findViewById(R.id.view_progress)
        layoutProgressBg = findViewById(R.id.layout_progress_bg)
        layoutTransactions = findViewById(R.id.layout_transactions)

        val btnAddIncome = findViewById<Button>(R.id.btn_add_income)
        val btnAddExpense = findViewById<Button>(R.id.btn_add_expense)

        btnAddIncome.setOnClickListener {
            val intent = Intent(this, AddIncomeActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_INCOME)
        }

        btnAddExpense.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_EXPENSE)
        }

        updateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            val transaction = data.getSerializableExtra("TRANSACTION") as? Transaction
            if (transaction != null) {
                transactionList.add(0, transaction)
                if (transaction.type == "income") {
                    totalIncome += transaction.amount
                } else {
                    totalExpense += transaction.amount
                }
                updateUI()
            }
        }
    }

    private fun updateUI() {
        val balance = totalIncome - totalExpense
        tvBalance.text = String.format("%,.0f ₽", balance).replace(',', ' ')
        tvIncome.text = String.format("Доход: %,.0f ₽", totalIncome).replace(',', ' ')
        tvExpense.text = String.format("Расход: %,.0f ₽", totalExpense).replace(',', ' ')

        layoutProgressBg.post {
            val totalWidth = layoutProgressBg.width
            if (totalIncome > 0 && totalWidth > 0) {
                val percent = (totalExpense / totalIncome).coerceIn(0.0, 1.0)
                val newWidth = (totalWidth * percent).toInt()
                viewProgress.layoutParams.width = newWidth
                viewProgress.requestLayout()
            } else {
                viewProgress.layoutParams.width = 0
                viewProgress.requestLayout()
            }
        }

        updateTransactionList()
    }

    private fun updateTransactionList() {
        layoutTransactions.removeAllViews()

        if (transactionList.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = "Нет транзакций за сегодня"
                textSize = 14f
                setTextColor(getColor(android.R.color.darker_gray))
                setPadding(0, 16, 0, 0)
            }
            layoutTransactions.addView(emptyText)
            return
        }

        for (i in transactionList.indices) {
            val transaction = transactionList[i]

            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = android.view.Gravity.CENTER_VERTICAL
                setPadding(4, 12, 4, 12)
            }

            val iconView = TextView(this).apply {
                text = transaction.category.split(" ").firstOrNull() ?: "❓"
                textSize = 20f
            }
            row.addView(iconView)

            val textLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    marginStart = 12
                }
            }

            val categoryText = TextView(this).apply {
                val label = transaction.category.split(" ").drop(1).joinToString(" ")
                text = if (transaction.note.isNotEmpty()) {
                    "$label — ${transaction.note}"
                } else {
                    label.ifEmpty { transaction.category }
                }
                textSize = 16f
                setTextColor(getColor(android.R.color.black))
            }
            textLayout.addView(categoryText)

            val dateText = TextView(this).apply {
                text = transaction.date
                textSize = 11f
                setTextColor(getColor(android.R.color.darker_gray))
            }
            textLayout.addView(dateText)

            row.addView(textLayout)

            val amountText = TextView(this).apply {
                val prefix = if (transaction.type == "income") "+" else "-"
                text = "$prefix${String.format("%,.0f ₽", transaction.amount).replace(',', ' ')}"
                textSize = 16f
                setTextColor(
                    if (transaction.type == "income") getColor(android.R.color.holo_green_dark)
                    else getColor(android.R.color.holo_red_dark)
                )
            }
            row.addView(amountText)

            layoutTransactions.addView(row)

            if (i < transactionList.size - 1) {
                val divider = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1
                    )
                    setBackgroundColor(getColor(android.R.color.darker_gray))
                    alpha = 0.2f
                }
                layoutTransactions.addView(divider)
            }
        }
    }
}