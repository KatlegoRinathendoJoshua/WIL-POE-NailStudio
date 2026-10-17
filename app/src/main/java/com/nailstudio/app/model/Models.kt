package com.nailstudio.app.model

data class Service(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val durationMinutes: Int,
    val category: String,
    val rating: Double
)

data class Booking(
    val id: String,
    val serviceName: String,
    val customerName: String,
    val phone: String,
    val date: String,
    val time: String,
    val status: String = "Confirmed"
)
