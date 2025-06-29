package com.nestor.cinerama.desing.cines.adapters

import android.R.attr.width
import android.content.Context
import android.content.Intent

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nestor.cinerama.R
import com.nestor.cinerama.desing.cines.entities.CineHorario
import com.nestor.cinerama.desing.peliculas.activities.MovieSelectionActivity
import com.nestor.cinerama.desing.peliculas.activities.movie_detailactivity
import com.nestor.cinerama.desing.peliculas.entities.Movie

class CinemaSelecionAdapter(
    private val movieHorarios: List<CineHorario>
) : RecyclerView.Adapter<CinemaSelecionAdapter.CinemaViewHolder>() {

    var currentMovie: Movie? = null
    var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CinemaViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.itms_cinemas_selection, parent, false)
        return CinemaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CinemaViewHolder, position: Int) {
        val horario = movieHorarios[position]
        holder.txtCineTitle.text = horario.name

        bindHours(holder, horario)
        visibilityDataCine(holder)
    }

    private fun bindHours(holder: CinemaViewHolder, cineHorario: CineHorario) {
        holder.horariosCine.removeAllViews()
        val horarios = cineHorario.horarios

        if (!horarios.isNullOrEmpty()) {
            for (hour in horarios) {
                val textView = TextView(holder.itemView.context)
                textView.text = hour
                textView.setTextColor(holder.itemView.context.getColor(R.color.white))
                textView.textSize = 20f

                textView.setOnClickListener {
                    val intent = Intent(context, MovieSelectionActivity::class.java)
                    intent.putExtra("MOVIE_ID", currentMovie?.id)
                    intent.putExtra("cineHorario", cineHorario as java.io.Serializable)
                    intent.putExtra("horarioSeleccionado", hour)
                    context?.startActivity(intent)
                }

                val params = GridLayout.LayoutParams().apply {
                    width = 0
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(0, 0, 8, 0)
                }
                textView.layoutParams = params
                holder.horariosCine.addView(textView)
            }
        }
    }

    private fun visibilityDataCine(holder: CinemaViewHolder) {
        holder.idiomasCine.visibility = View.GONE
        holder.horariosCine.visibility = View.GONE

        holder.txtCineTitle.setOnClickListener {
            val isVisible = holder.idiomasCine.visibility == View.VISIBLE
            holder.idiomasCine.visibility = if (isVisible) View.GONE else View.VISIBLE
            holder.horariosCine.visibility = if (isVisible) View.GONE else View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        val itemCount = movieHorarios.size
        Log.d("CinemaSelecionAdapter", "getItemCount: $itemCount")
        return itemCount
    }

    class CinemaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCineTitle: TextView = itemView.findViewById(R.id.txt_cineTitle)
        val idiomasCine: LinearLayout = itemView.findViewById(R.id.idiomas_cine)
        val horariosCine: GridLayout = itemView.findViewById(R.id.horarios_cine)
    }
}