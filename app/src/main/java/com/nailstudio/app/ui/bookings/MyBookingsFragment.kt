package com.nailstudio.app.ui.bookings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nailstudio.app.R
import com.nailstudio.app.data.AppData

class MyBookingsFragment : Fragment() {
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_my_bookings, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view.findViewById<RecyclerView>(R.id.bookingsRecycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        refreshBookings(view)
    }

    override fun onResume() {
        super.onResume()
        view?.let { refreshBookings(it) }
    }

    private fun refreshBookings(view: View) {
        val bookings = AppData.getBookings(requireContext())
        val empty = view.findViewById<TextView>(R.id.emptyBookings)
        val recycler = view.findViewById<RecyclerView>(R.id.bookingsRecycler)
        empty.visibility = if (bookings.isEmpty()) View.VISIBLE else View.GONE
        recycler.visibility = if (bookings.isEmpty()) View.GONE else View.VISIBLE
        recycler.adapter = BookingAdapter(bookings)
    }
}
