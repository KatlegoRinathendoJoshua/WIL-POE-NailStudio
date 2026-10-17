package com.nailstudio.app.data

import android.content.Context
import com.nailstudio.app.model.Booking
import com.nailstudio.app.model.Service
import org.json.JSONArray
import org.json.JSONObject
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object AppData {
    val services = listOf(
        Service("1", "Classic Manicure", "Shape, cuticle care, polish and hand massage.", 180.0, 45, "Manicure", 4.8),
        Service("2", "Gel Manicure", "Long-lasting gel polish with a glossy finish.", 280.0, 60, "Manicure", 4.9),
        Service("3", "Classic Pedicure", "Soak, exfoliation, nail care and polish.", 220.0, 50, "Pedicure", 4.7),
        Service("4", "Acrylic Full Set", "Custom acrylic nail extensions with your chosen shape.", 450.0, 90, "Extensions", 4.9),
        Service("5", "Nail Art", "Hand-painted designs, rhinestones or French tips.", 150.0, 30, "Nail Art", 5.0),
        Service("6", "Nail Repair", "Repair for a broken or lifted nail.", 90.0, 20, "Repairs", 4.6)
    )

    private const val PREFS = "nail_studio_prefs"
    private const val KEY_BOOKINGS = "bookings"
    private const val KEY_USER = "user"
    private const val KEY_ACCOUNTS = "accounts"
    private const val KEY_MESSAGES = "messages"

    private fun prefs(context: Context) = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    /** Local demo authentication. For production, use a trusted authentication provider. */
    fun registerUser(context: Context, name: String, email: String, password: String): String? {
        val normalizedEmail = email.trim().lowercase(Locale.ROOT)
        if (name.trim().length < 2) return "Enter your full name."
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches()) return "Enter a valid email address."
        if (password.length < 8) return "Password must contain at least 8 characters."
        val p = prefs(context)
        val accounts = try { JSONObject(p.getString(KEY_ACCOUNTS, "{}") ?: "{}") } catch (_: Exception) { JSONObject() }
        if (accounts.has(normalizedEmail)) return "An account with this email already exists. Please log in."
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val saltHex = salt.joinToString("") { "%02x".format(it) }
        accounts.put(normalizedEmail, JSONObject().put("name", name.trim()).put("salt", saltHex).put("passwordHash", hashPassword(password, salt)))
        p.edit().putString(KEY_ACCOUNTS, accounts.toString()).apply()
        saveUser(context, name.trim(), normalizedEmail)
        return null
    }

    fun loginUser(context: Context, email: String, password: String): String? {
        val normalizedEmail = email.trim().lowercase(Locale.ROOT)
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches() || password.isBlank()) return "Enter a valid email and password."
        val accounts = try { JSONObject(prefs(context).getString(KEY_ACCOUNTS, "{}") ?: "{}") } catch (_: Exception) { JSONObject() }
        val account = accounts.optJSONObject(normalizedEmail) ?: return "No account found. Please register first."
        val salt = hexToBytes(account.optString("salt")) ?: return "Account data is invalid. Please register again."
        val expected = account.optString("passwordHash")
        if (!MessageDigest.isEqual(expected.toByteArray(), hashPassword(password, salt).toByteArray())) return "Incorrect email or password."
        saveUser(context, account.optString("name", normalizedEmail.substringBefore('@')), normalizedEmail)
        return null
    }

    private fun hashPassword(password: String, salt: ByteArray): String {
        val spec = PBEKeySpec(password.toCharArray(), salt, 120_000, 256)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).encoded
                .joinToString("") { "%02x".format(it) }
        } finally {
            spec.clearPassword()
        }
    }

    private fun hexToBytes(value: String): ByteArray? {
        return try {
            if (value.length % 2 != 0) null
            else ByteArray(value.length / 2) { i -> value.substring(i * 2, i * 2 + 2).toInt(16).toByte() }
        } catch (_: Exception) { null }
    }

    fun saveUser(context: Context, name: String, email: String) {
        prefs(context).edit().putString(KEY_USER, JSONObject().put("name", name).put("email", email).toString()).apply()
    }

    fun getUserName(context: Context): String {
        val raw = prefs(context).getString(KEY_USER, null) ?: return "Guest"
        return try { JSONObject(raw).optString("name", "Guest") } catch (_: Exception) { "Guest" }
    }

    fun addBooking(context: Context, booking: Booking) {
        val p = prefs(context)
        val array = try { JSONArray(p.getString(KEY_BOOKINGS, "[]") ?: "[]") } catch (_: Exception) { JSONArray() }
        array.put(JSONObject().put("id", booking.id).put("serviceName", booking.serviceName)
            .put("customerName", booking.customerName).put("phone", booking.phone)
            .put("date", booking.date).put("time", booking.time).put("status", booking.status))
        p.edit().putString(KEY_BOOKINGS, array.toString()).apply()
    }

    fun getBookings(context: Context): List<Booking> {
        val raw = prefs(context).getString(KEY_BOOKINGS, "[]") ?: "[]"
        return try {
            val array = JSONArray(raw)
            (0 until array.length()).mapNotNull { index ->
                val o = array.optJSONObject(index) ?: return@mapNotNull null
                Booking(o.optString("id"), o.optString("serviceName"), o.optString("customerName"),
                    o.optString("phone"), o.optString("date"), o.optString("time"), o.optString("status", "Confirmed"))
            }.sortedWith(compareBy({ it.date }, { it.time }))
        } catch (_: Exception) { emptyList() }
    }

    fun validateBooking(context: Context, serviceName: String, date: String, time: String, customerName: String, phone: String): String? {
        if (customerName.trim().length < 2) return "Enter the customer's full name."
        val digits = phone.count { it.isDigit() }
        if (digits < 9 || digits > 15) return "Enter a valid phone number."
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT).apply { isLenient = false }
        val parsedDate = try { dateFormat.parse(date) } catch (_: Exception) { null } ?: return "Choose a valid date."
        val timeParts = time.split(":")
        if (timeParts.size != 2) return "Choose a valid time."
        val hour = timeParts[0].toIntOrNull() ?: return "Choose a valid time."
        val minute = timeParts[1].toIntOrNull() ?: return "Choose a valid time."
        if (hour !in 0..23 || minute !in 0..59) return "Choose a valid time."
        val now = Date()
        val today = dateFormat.format(now)
        if (date < today || (date == today && hour * 60 + minute <= java.util.Calendar.getInstance().let { it.get(java.util.Calendar.HOUR_OF_DAY) * 60 + it.get(java.util.Calendar.MINUTE) })) return "Choose a future date and time."
        val start = hour * 60 + minute
        val service = services.firstOrNull { it.name == serviceName } ?: return "Please select a service."
        if (start < 9 * 60 || start + service.durationMinutes > 18 * 60) return "Choose a time between 09:00 and 18:00 that fits the service duration."
        val overlaps = getBookings(context).any { booking ->
            if (booking.date != date || !booking.status.equals("Confirmed", ignoreCase = true)) false
            else {
                val parts = booking.time.split(":")
                val existingStart = if (parts.size == 2) (parts[0].toIntOrNull() ?: 0) * 60 + (parts[1].toIntOrNull() ?: 0) else 0
                val existingDuration = services.firstOrNull { it.name == booking.serviceName }?.durationMinutes ?: 60
                start < existingStart + existingDuration && existingStart < start + service.durationMinutes
            }
        }
        if (overlaps) return "That time conflicts with an existing booking. Please choose another time."
        return null
    }

    fun saveContactMessage(context: Context, message: String) {
        val p = prefs(context)
        val array = try { JSONArray(p.getString(KEY_MESSAGES, "[]") ?: "[]") } catch (_: Exception) { JSONArray() }
        array.put(JSONObject().put("message", message.trim()).put("createdAt", System.currentTimeMillis()))
        p.edit().putString(KEY_MESSAGES, array.toString()).apply()
    }
}
