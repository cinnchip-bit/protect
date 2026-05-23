package com.example.myapplication

import java.io.Serializable

data class Transaction(
    val type: String,
    val category: String,
    val amount: Double,
    val note: String,
    val date: String
) : Serializable