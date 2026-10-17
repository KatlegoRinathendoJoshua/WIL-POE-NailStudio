package com.nailstudio.app.ui.gallery

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nailstudio.app.R

class GalleryAdapter(private val titles: List<String>) : RecyclerView.Adapter<GalleryAdapter.Holder>() {
    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val title: TextView = v.findViewById(R.id.galleryTitle)
        val tile: View = v.findViewById(R.id.galleryTile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_gallery, parent, false))

    override fun getItemCount() = titles.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.title.text = titles[position]
        val colors = intArrayOf(0xFFB76E79.toInt(), 0xFFE8D5CB.toInt(), 0xFF96525D.toInt(), 0xFFF7F7F9.toInt())
        holder.tile.setBackgroundColor(colors[position % colors.size])
    }
}
