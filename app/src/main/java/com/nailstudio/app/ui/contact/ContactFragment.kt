package com.nailstudio.app.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nailstudio.app.R
import com.nailstudio.app.data.AppData

class ContactFragment : Fragment() {
    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_contact, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.sendMessageButton).setOnClickListener {
            val message = view.findViewById<EditText>(R.id.messageInput)
            if (message.text.isBlank()) {
                Toast.makeText(requireContext(), "Please enter a message", Toast.LENGTH_SHORT).show()
            } else {
                AppData.saveContactMessage(requireContext(), message.text.toString())
                message.setText("")
                Toast.makeText(requireContext(), "Message saved on this device. It has not been emailed to the salon.", Toast.LENGTH_LONG).show()
            }
        }
    }
}
