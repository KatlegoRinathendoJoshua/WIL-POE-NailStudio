package com.nailstudio.app.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ModelsTest {
    @Test
    fun bookingDefaultsToConfirmed() {
        val booking = Booking(
            id = "test-1",
            serviceName = "Classic Manicure",
            customerName = "Test Customer",
            phone = "0712345678",
            date = "2030-01-15",
            time = "10:00"
        )
        assertEquals("Confirmed", booking.status)
    }

    @Test
    fun serviceHasPositivePriceAndDuration() {
        val service = Service(
            id = "test-service",
            name = "Test Manicure",
            description = "Test service",
            price = 180.0,
            durationMinutes = 45,
            category = "Manicure",
            rating = 4.8
        )
        assertTrue(service.price > 0.0)
        assertTrue(service.durationMinutes > 0)
        assertTrue(service.rating in 0.0..5.0)
    }
}
