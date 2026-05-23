package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val prefs = context.getSharedPreferences("finance_prefs", Context.MODE_PRIVATE)
        val style = prefs.getString("selected_style", "Монах") ?: "Монах"
        val hasTransactions = prefs.getBoolean("has_transactions_today", false)
        val isOverSpending = prefs.getBoolean("is_over_spending", false)
        val isOverLimit = prefs.getBoolean("is_over_limit", false)
        val isSaving = prefs.getBoolean("is_saving", false)

        val phrase = CharacterPhrases.getRandomPhrase(
            style = style,
            hasTransactionsToday = hasTransactions,
            isOverSpending = isOverSpending,
            isOverLimit = isOverLimit,
            isSaving = isSaving
        )

        val title = when (style) {
            "Монах" -> "Монах"
            "Темщик" -> "Темщик"
            "Заботливый" -> "Заботливый"
            "Тренер" -> "Тренер"
            else -> "Finex"
        }

        NotificationHelper.showNotification(context, title, phrase)
    }
}