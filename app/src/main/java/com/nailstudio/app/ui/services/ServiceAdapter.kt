package com.nailstudio.app.ui.services

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nailstudio.app.R
import com.nailstudio.app.data.AppData
import com.nailstudio.app.model.Service

class ServiceAdapter(private var items: List<Service>) : RecyclerView.Adapter<ServiceAdapter.Holder>() {
    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.serviceName)
        val description: TextView = v.findViewById(R.id.serviceDescription)
        val meta: TextView = v.findViewById(R.id.serviceMeta)
        val book: Button = v.findViewById(R.id.bookServiceButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false))

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.description.text = item.description
        holder.meta.text = "R${"%.2f".format(item.price)} • ${item.durationMinutes} min • ${item.rating}★"
        holder.book.setOnClickListener {
            BookingSelection.selectedService = item.name
            parentFragmentManager(holder).beginTransaction()
                .replace(R.id.fragmentContainer, com.nailstudio.app.ui.booking.BookingFragment())
                .commit()
        }
    }

    private fun parentFragmentManager(holder: Holder) =
        (holder.itemView.context as? androidx.fragment.app.FragmentActivity)?.supportFragmentManager
        ?: throw IllegalStateException("Fragment activity required")

    fun filter(query: String) {
        val q = query.trim().lowercase()
        items = if (q.isEmpty()) AppData.services else AppData.services.filter {
            it.name.lowercase().contains(q) || it.category.lowercase().contains(q)
        }
        notifyDataSetChanged()
    }
}

object BookingSelection {
    var selectedService: String? = null
}
