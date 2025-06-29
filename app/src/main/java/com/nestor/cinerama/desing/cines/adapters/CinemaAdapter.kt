package com.nestor.cinerama.desing.cines.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nestor.cinerama.R
import com.nestor.cinerama.desing.cines.entities.Cinema

class CinemaAdapter(private val cinemaList: List<Cinema>) : RecyclerView.Adapter<CinemaAdapter.CinemaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CinemaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cinema_item, parent, false)
        return CinemaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CinemaViewHolder, position: Int) {
        val cinema = cinemaList[position]
        holder.cinemaName.text = cinema.name
        holder.cinemaAddress.text = cinema.address
        holder.cinemaFormat.text = cinema.format.joinToString(", ")

        Glide.with(holder.itemView.context)
            .load(cinema.urlImage)
            .into(holder.cinemaImage)
    }

    override fun getItemCount(): Int = cinemaList.size

    class CinemaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cinemaImage: ImageView = itemView.findViewById(R.id.img_cinema)
        val cinemaName: TextView = itemView.findViewById(R.id.txt_titleCinema)
        val cinemaAddress: TextView = itemView.findViewById(R.id.txt_address)
        val cinemaFormat: TextView = itemView.findViewById(R.id.txt_formats)
    }
}