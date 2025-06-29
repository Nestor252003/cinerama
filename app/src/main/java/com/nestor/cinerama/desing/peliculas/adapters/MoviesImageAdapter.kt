package com.nestor.cinerama.desing.peliculas.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.nestor.cinerama.R
import com.bumptech.glide.Glide
import com.nestor.cinerama.desing.peliculas.activities.movie_detailactivity

class MoviesImageAdapter(
    private val movies: List<com.nestor.cinerama.desing.peliculas.entities.Movie>,
    private val context: Context
) : RecyclerView.Adapter<MoviesImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pelicula, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]

        // Cargar imagen usando Glide
        Glide.with(context)
            .load(movie.url)
            .into(holder.imageView)

        // Abrir detalles de película al hacer clic
        holder.itemView.setOnClickListener {
            val intent = Intent(context, movie_detailactivity::class.java)
            intent.putExtra("MOVIE_ID", movie.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = movies.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.movie_image)
    }
}