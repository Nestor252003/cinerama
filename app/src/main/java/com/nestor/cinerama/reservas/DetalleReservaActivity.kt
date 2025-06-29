package com.nestor.cinerama.reservas

import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nestor.cinerama.R
import com.nestor.cinerama.desing.peliculas.entities.Reserva
import com.google.firebase.database.ValueEventListener

class DetalleReservaActivity : AppCompatActivity() {
    private lateinit var tvMovie: TextView
    private lateinit var tvCinema: TextView
    private lateinit var tvHour: TextView
    private lateinit var tvDate: TextView
    private lateinit var tvSeat: TextView
    private lateinit var codigoReserva: TextView
    private lateinit var ticketsLayout: LinearLayout
    private lateinit var paymentsLayout: LinearLayout

    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_reserva)

        // Obtener ID de reserva desde el intent
        val reservaId = intent.getStringExtra("reservaId")

        // Inicializar referencia a Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("reservas")

        // Inicializar vistas
        tvMovie = findViewById(R.id.tvMovie)
        tvCinema = findViewById(R.id.tvCinema)
        tvHour = findViewById(R.id.tvHour)
        tvDate = findViewById(R.id.tvDate)
        tvSeat = findViewById(R.id.tvSeat)
        codigoReserva = findViewById(R.id.txt_codReserva)
        ticketsLayout = findViewById(R.id.ticketsLayout)
        paymentsLayout = findViewById(R.id.paymentsLayout)

        // Consultar datos de la reserva
        reservaId?.let {
            fetchReserva(it)
        }
    }

    private fun fetchReserva(reservaId: String) {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (child in snapshot.children) {
                    val reserva = child.getValue(Reserva::class.java)
                    if (reserva?.id == reservaId) {
                        updateReservaDetails(reserva)
                        return
                    }
                }
                Log.e("FetchReserva", "No reserva found with id: $reservaId")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FetchReserva", "Database error: ${error.message}")
            }
        })
    }

    private fun updateReservaDetails(reserva: Reserva) {
        tvMovie.text = reserva.movie
        tvCinema.text = reserva.cinema
        tvHour.text = reserva.hour
        tvDate.text = reserva.date
        tvSeat.text = reserva.seat
        codigoReserva.text = reserva.id

        // Limpiar layouts antes de agregar nuevas vistas
        ticketsLayout.removeAllViews()
        paymentsLayout.removeAllViews()

        // Agregar dinámicamente tickets
        reserva.tickets?.forEach { ticket ->
            val ticketView = TextView(this)
            ticketView.text = "Ticket: ${ticket.type} - Price: $${ticket.price}"
            ticketsLayout.addView(ticketView)
        }

        // Agregar dinámicamente métodos de pago
        reserva.payments?.forEach { payment ->
            val paymentView = TextView(this)
            paymentView.text = "Payment: ${payment.nombre}, Method: ${payment.metodoPago}, Total: $${payment.total}"
            paymentsLayout.addView(paymentView)
        }
    }
}