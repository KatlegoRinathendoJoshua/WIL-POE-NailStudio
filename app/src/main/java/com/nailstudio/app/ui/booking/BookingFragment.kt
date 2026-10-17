package com.nailstudio.app.ui.booking

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nailstudio.app.R
import com.nailstudio.app.data.AppData
import com.nailstudio.app.model.Booking
import com.nailstudio.app.ui.services.BookingSelection
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class BookingFragment : Fragment() {
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_booking, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinner = view.findViewById<Spinner>(R.id.serviceSpinner)
        val date = view.findViewById<EditText>(R.id.dateInput)
        val time = view.findViewById<EditText>(R.id.timeInput)
        val name = view.findViewById<EditText>(R.id.customerNameInput)
        val phone = view.findViewById<EditText>(R.id.phoneInput)
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,
            AppData.services.map { "${it.name} - R${String.format(Locale("en", "ZA"), "%.2f", it.price)}" })
        BookingSelection.selectedService?.let { selected ->
            AppData.services.indexOfFirst { it.name == selected }.takeIf { it >= 0 }?.let { spinner.setSelection(it) }
            BookingSelection.selectedService = null
        }
        date.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(requireContext(), { _, y, m, d ->
                date.setText(String.format(Locale.ROOT, "%04d-%02d-%02d", y, m + 1, d))
                time.text.clear()
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).apply {
                datePicker.minDate = System.currentTimeMillis() - 1000
            }.show()
        }
        time.setOnClickListener {
            val cal = Calendar.getInstance()
            android.app.TimePickerDialog(requireContext(), { _, h, min ->
                time.setText(String.format(Locale.ROOT, "%02d:%02d", h, min))
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }
        view.findViewById<Button>(R.id.confirmBookingButton).setOnClickListener {
            val serviceName = spinner.selectedItem?.toString()?.substringBefore(" - ").orEmpty()
            val error = AppData.validateBooking(requireContext(), serviceName, date.text.toString(), time.text.toString(), name.text.toString(), phone.text.toString())
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            AppData.addBooking(requireContext(), Booking(UUID.randomUUID().toString(), serviceName,
                name.text.toString().trim(), phone.text.toString().trim(), date.text.toString(), time.text.toString()))
            Toast.makeText(requireContext(), "Booking confirmed", Toast.LENGTH_LONG).show()
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer,
                com.nailstudio.app.ui.bookings.MyBookingsFragment()).commit()
        }
    }
}
