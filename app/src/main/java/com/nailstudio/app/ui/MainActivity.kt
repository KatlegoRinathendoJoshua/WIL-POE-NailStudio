package com.nailstudio.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nailstudio.app.R
import com.nailstudio.app.ui.gallery.GalleryFragment
import com.nailstudio.app.ui.home.HomeFragment
import com.nailstudio.app.ui.booking.BookingFragment
import com.nailstudio.app.ui.bookings.MyBookingsFragment
import com.nailstudio.app.ui.services.ServicesFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val nav = findViewById<BottomNavigationView>(R.id.bottomNav)
        if (savedInstanceState == null) show(HomeFragment())

        nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> show(HomeFragment())
                R.id.nav_services -> show(ServicesFragment())
                R.id.nav_booking -> show(BookingFragment())
                R.id.nav_gallery -> show(GalleryFragment())
                R.id.nav_bookings -> show(MyBookingsFragment())
            }
            true
        }
    }

    private fun show(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
