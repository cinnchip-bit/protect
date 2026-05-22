package com.example.myapplication

import java.io.Serializable

data class Transaction(
    val type: String,       // "income" или "expense"
    val category: String,   // "💰 Зарплата", "🍕 Еда" и т.д.
    val amount: Double,
    val note: String,
    val date: String
) : Serializable