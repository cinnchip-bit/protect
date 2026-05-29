package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var tvBalance: TextView
    private lateinit var tvIncome: TextView
    private lateinit var tvExpense: TextView
    private lateinit var tvLimit: TextView
    private lateinit var viewProgress: View
    private lateinit var layoutProgressBg: LinearLayout
    private lateinit var layoutTransactions: LinearLayout
    private lateinit var notificationContainer: LinearLayout
    private lateinit var prefs: SharedPreferences

    private val transactionList = mutableListOf<Transaction>()
    private var totalIncome = 0.0
    private var totalExpense = 0.0
    private var monthlyLimit = -1.0

    companion object {
        private const val REQUEST_ADD_INCOME = 1
        private const val REQUEST_ADD_EXPENSE = 2
        private const val REQUEST_SETTINGS = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        prefs = getSharedPreferences("finance_prefs", MODE_PRIVATE)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }

        val selectedStyle = intent.getStringExtra("SELECTED_STYLE") ?: "Не выбран"
        val tvSelectedStyle = findViewById<TextView>(R.id.tv_selected_style)
        tvSelectedStyle.text = "Выбран стиль: $selectedStyle"

        val btnBack = findViewById<ImageButton>(R.id.btn_back)
        btnBack.setOnClickListener { finish() }

        tvBalance = findViewById(R.id.tv_balance)
        tvIncome = findViewById(R.id.tv_income)
        tvExpense = findViewById(R.id.tv_expense)
        tvLimit = findViewById(R.id.tv_limit)
        viewProgress = findViewById(R.id.view_progress)
        layoutProgressBg = findViewById(R.id.layout_progress_bg)
        layoutTransactions = findViewById(R.id.layout_transactions)
        notificationContainer = findViewById(R.id.notification_container)

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

        val btnSettings = findViewById<ImageButton>(R.id.btn_settings)
        btnSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent, REQUEST_SETTINGS)
        }



        val savedLimit = prefs.getFloat("monthly_limit", -1f)
        updateLimitDisplay(savedLimit)

        NotificationHelper.createChannel(this)
        prefs.edit().putString("selected_style", selectedStyle).apply()
        scheduleReminders()

        updateUI()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                REQUEST_ADD_INCOME, REQUEST_ADD_EXPENSE -> {
                    val transaction = data.getSerializableExtra("TRANSACTION") as? Transaction
                    if (transaction != null) {
                        transactionList.add(0, transaction)
                        if (transaction.type == "income") {
                            totalIncome += transaction.amount
                        } else {
                            totalExpense += transaction.amount
                        }
                        updateUI()

                        val balance = totalIncome - totalExpense
                        if (monthlyLimit > 0 && totalExpense > monthlyLimit && balance < 0) {
                            sendInstantNotification(Mood.OVER_LIMIT)
                        } else if (totalExpense > totalIncome && totalIncome > 0) {
                            sendInstantNotification(Mood.OVERSPENDING)
                        }
                    }
                }
                REQUEST_SETTINGS -> {
                    val limit = data.getFloatExtra("MONTHLY_LIMIT", -1f)
                    updateLimitDisplay(limit)
                }
            }
        }
    }

    private fun updateLimitDisplay(limit: Float) {
        monthlyLimit = limit.toDouble()
        if (limit > 0) {
            tvLimit.text = "Лимит на месяц: ${String.format("%,.0f P", limit).replace(',', ' ')}"
        } else {
            tvLimit.text = "Лимит на месяц: не установлен"
        }
    }

    private fun updateUI() {
        val balance = totalIncome - totalExpense
        tvBalance.text = String.format("%,.0f P", balance).replace(',', ' ')

        tvIncome.text = String.format("Доход: %,.0f P", totalIncome).replace(',', ' ')

        if (monthlyLimit > 0 && totalExpense > monthlyLimit && balance < 0) {
            tvExpense.text = String.format("Расход: %,.0f P (превышен лимит!)", totalExpense).replace(',', ' ')
        } else {
            tvExpense.text = String.format("Расход: %,.0f P", totalExpense).replace(',', ' ')
        }

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

            val textLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val label = transaction.category
            val categoryText = TextView(this).apply {
                text = if (transaction.note.isNotEmpty()) {
                    "$label - ${transaction.note}"
                } else {
                    label
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
                text = "$prefix${String.format("%,.0f P", transaction.amount).replace(',', ' ')}"
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

    private fun sendInstantNotification(mood: Mood) {
        val style = prefs.getString("selected_style", "Монах") ?: "Монах"
        val phrase = CharacterPhrases.getPhrase(style, mood)
        val title = when (style) {
            "Монах" -> "Монах"
            "Темщик" -> "Темщик"
            "Заботливый" -> "Заботливый"
            "Тренер" -> "Тренер"
            else -> "Finex"
        }
        showInAppNotification(title, phrase)
    }

    private fun showInAppNotification(title: String, message: String) {
        val card = androidx.cardview.widget.CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16)
            }
            radius = 40f
            cardElevation = 8f
            setCardBackgroundColor(getColor(android.R.color.white))
            setPadding(24, 16, 24, 16)
        }

        val textLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }

        val titleView = TextView(this).apply {
            text = title
            textSize = 16f
            setTextColor(getColor(android.R.color.black))
            setTypeface(null, android.graphics.Typeface.BOLD)
        }
        textLayout.addView(titleView)

        val messageView = TextView(this).apply {
            text = message
            textSize = 14f
            setTextColor(getColor(android.R.color.darker_gray))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 4
            }
        }
        textLayout.addView(messageView)

        card.addView(textLayout)
        notificationContainer.addView(card, 0)

        card.postDelayed({
            notificationContainer.removeView(card)
        }, 5000)

        card.setOnTouchListener(object : View.OnTouchListener {
            private var startX = 0f
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = event.rawX
                    }
                    MotionEvent.ACTION_UP -> {
                        if (kotlin.math.abs(event.rawX - startX) > 100) {
                            notificationContainer.removeView(card)
                        }
                    }
                }
                return true
            }
        })
    }

    private fun scheduleReminders() {
        scheduleNoTransactionsReminder()
        scheduleSavingReminder()
    }

    private fun scheduleNoTransactionsReminder() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        intent.putExtra("TYPE", "no_transactions")
        val pendingIntent = PendingIntent.getBroadcast(
            this, 100, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 2 * 60 * 60 * 1000,
            2 * 60 * 60 * 1000,
            pendingIntent
        )
    }

    private fun scheduleSavingReminder() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        intent.putExtra("TYPE", "saving")
        val pendingIntent = PendingIntent.getBroadcast(
            this, 200, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + 2 * 60 * 60 * 1000,
            2 * 60 * 60 * 1000,
            pendingIntent
        )
    }

    private fun cancelAllReminders() {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent1 = PendingIntent.getBroadcast(
            this, 100, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val pendingIntent2 = PendingIntent.getBroadcast(
            this, 200, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent1)
        alarmManager.cancel(pendingIntent2)
    }

    override fun onPause() {
        super.onPause()
        val hasTransactions = transactionList.isNotEmpty()
        val isOverSpending = totalExpense > totalIncome && totalIncome > 0
        val isOverLimit = monthlyLimit > 0 && totalExpense > monthlyLimit
        val isSaving = monthlyLimit > 0 && totalExpense < monthlyLimit * 0.5

        prefs.edit()
            .putBoolean("has_transactions_today", hasTransactions)
            .putBoolean("is_over_spending", isOverSpending)
            .putBoolean("is_over_limit", isOverLimit)
            .putBoolean("is_saving", isSaving)
            .apply()

        if (hasTransactions && !isOverSpending && !isOverLimit && !isSaving) {
            cancelAllReminders()
        }
    }
}