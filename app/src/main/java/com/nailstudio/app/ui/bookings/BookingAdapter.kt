package com.nailstudio.app.ui.bookings

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nailstudio.app.R
import com.nailstudio.app.model.Booking

class BookingAdapter(private val items: List<Booking>) : RecyclerView.Adapter<BookingAdapter.Holder>() {
    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val service: TextView = v.findViewById(R.id.bookingService)
        val details: TextView = v.findViewById(R.id.bookingDetails)
        val status: TextView = v.findViewById(R.id.bookingStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val b = items[position]
        holder.service.text = b.serviceName
        holder.details.text = "${b.date} at ${b.time}\n${b.customerName} • ${b.phone}"
        holder.status.text = b.status
    }
}
