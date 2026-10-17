package com.nailstudio.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.nailstudio.app.R
import com.nailstudio.app.ui.booking.BookingFragment
import com.nailstudio.app.ui.services.ServicesFragment
import com.nailstudio.app.ui.contact.ContactFragment
import com.nailstudio.app.ui.about.AboutFragment

class HomeFragment : Fragment() {
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_home, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.bookNowButton).setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer, BookingFragment()).commit()
        }
        view.findViewById<Button>(R.id.viewServicesButton).setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer, ServicesFragment()).commit()
        }
        view.findViewById<Button>(R.id.contactButton).setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer, ContactFragment()).commit()
        }
        view.findViewById<Button>(R.id.aboutButton).setOnClickListener {
            parentFragmentManager.beginTransaction().replace(R.id.fragmentContainer, AboutFragment()).commit()
        }
    }
}
