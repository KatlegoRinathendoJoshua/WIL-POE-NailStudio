package com.nailstudio.app.ui.services

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nailstudio.app.R
import com.nailstudio.app.data.AppData

class ServicesFragment : Fragment() {
    private lateinit var adapter: ServiceAdapter

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View =
        i.inflate(R.layout.fragment_services, c, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ServiceAdapter(AppData.services)
        view.findViewById<RecyclerView>(R.id.servicesRecycler).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ServicesFragment.adapter
        }
        view.findViewById<EditText>(R.id.serviceSearch).addTextChangedListenerSafe { text ->
            adapter.filter(text)
        }
    }
}

private fun EditText.addTextChangedListenerSafe(block: (String) -> Unit) {
    addTextChangedListener(object : android.text.TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
        override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
        override fun afterTextChanged(s: android.text.Editable?) { block(s?.toString().orEmpty()) }
    })
}
