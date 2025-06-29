package com.nestor.cinerama.dulcería

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nestor.cinerama.R
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class DulceriaAdapter(
    private val dulceriaList: List<Dulceria>,
    private val context: Context
) : RecyclerView.Adapter<DulceriaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dulceria, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dulceria = dulceriaList[position]

        // Cargar imagen con Glide
        if (!dulceria.urlImage.isNullOrEmpty()) {
            Glide.with(context)
                .load(dulceria.urlImage)
                .into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.ic_close) // Imagen por defecto
        }

        holder.title.text = dulceria.title
        holder.cost.text = dulceria.cost

        val descriptionList = dulceria.description
        if (!descriptionList.isNullOrEmpty()) {
            val sb = StringBuilder()
            for (item in descriptionList) {
                sb.append(item).append("\n")
            }
            holder.description.text = sb.toString().trim()
        } else {
            holder.description.text = "No description available"
        }

        Log.d("DulceriaAdapter", "Tamaño total de la lista: ${dulceriaList.size}")
    }

    override fun getItemCount(): Int = dulceriaList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.img_Combo)
        val title: TextView = view.findViewById(R.id.combo_title)
        val cost: TextView = view.findViewById(R.id.txt_cost)
        val description: TextView = view.findViewById(R.id.combo_description)
        // val category: TextView = view.findViewById(R.id.combo_category) // Por si lo agregas en el layout
    }
}