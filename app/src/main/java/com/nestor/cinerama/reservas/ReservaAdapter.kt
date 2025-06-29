package com.nestor.cinerama.reservas

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nestor.cinerama.R
import com.nestor.cinerama.desing.peliculas.entities.Reserva
import kotlin.collections.indexOfFirst

class ReservaAdapter(
    private val context: Context,
    private val reservaList: MutableList<Reserva>
) : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("reservas")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_reserva, parent, false)
        return ReservaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservaList[position]
        holder.tvMovie.text = reserva.movie
        holder.tvCinema.text = reserva.cinema
        holder.tvHour.text = reserva.hour

        holder.btnViewDetails.setOnClickListener {
            val intent = Intent(context, DetalleReservaActivity::class.java)
            intent.putExtra("reservaId", reserva.id)
            context.startActivity(intent)
        }

        holder.btnDelete.setOnClickListener {
            val reservaId = reserva.id
            deleteReserva(reservaId)
        }
    }

    override fun getItemCount(): Int = reservaList.size

    inner class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMovie: TextView = itemView.findViewById(R.id.tv_movie)
        val tvCinema: TextView = itemView.findViewById(R.id.tv_cinema)
        val tvHour: TextView = itemView.findViewById(R.id.tv_hour)
        val btnViewDetails: TextView = itemView.findViewById(R.id.btn_view_details)
        val btnDelete: Button = itemView.findViewById(R.id.btn_cancel)
    }

    private fun deleteReserva(reservaId: String) {
        databaseReference.child(reservaId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Firebase", "Reserva eliminada con éxito")
                Toast.makeText(context, "Reserva eliminada con éxito", Toast.LENGTH_SHORT).show()
                val index = reservaList.indexOfFirst { it.id == reservaId }
                if (index != -1) {
                    reservaList.removeAt(index)
                    notifyItemRemoved(index)
                }
            } else {
                Log.e("Firebase", "Error al eliminar la reserva", task.exception)
                Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}