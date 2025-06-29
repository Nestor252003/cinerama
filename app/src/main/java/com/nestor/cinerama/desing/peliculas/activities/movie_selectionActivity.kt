package com.nestor.cinerama.desing.peliculas.activities

import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import com.nestor.cinerama.R
import com.nestor.cinerama.desing.cines.entities.Cines
import com.nestor.cinerama.desing.cines.services.CinesApi
import com.nestor.cinerama.desing.peliculas.entities.Movie
import com.nestor.cinerama.desing.peliculas.entities.PaymentInfo
import com.nestor.cinerama.desing.peliculas.entities.Reserva
import com.nestor.cinerama.desing.peliculas.entities.Ticket
import com.nestor.cinerama.desing.peliculas.fragments.payment_summary_Fragment
import com.nestor.cinerama.desing.peliculas.fragments.ticket_amount_Fragment
import com.nestor.cinerama.desing.peliculas.services.MovieApi
import com.nestor.cinerama.network.RetrofitClient
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class MovieSelectionActivity : AppCompatActivity(), ticket_amount_Fragment.OnSeatTypeSelectedListener {

    private lateinit var spinnerCity: Spinner
    private lateinit var spinnerMovie: Spinner
    private lateinit var spinnerCines: Spinner
    private lateinit var spinnerHora: Spinner
    private lateinit var spinnerDate: Spinner
    private lateinit var selectedSeatTextView: TextView
    private lateinit var seatSelectionView: SeatSelectionView
    private lateinit var actionButton: Button

    private var isSeatTypeSelected = false
    private var isPaymentSummary = false

    private var movies: List<Movie> = emptyList()
    private var cines: List<Cines> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_selection)

        spinnerCity = findViewById(R.id.spinner_city)
        spinnerMovie = findViewById(R.id.spinner_movie)
        spinnerCines = findViewById(R.id.spinner_cinema)
        spinnerHora = findViewById(R.id.spinner_hora)
        spinnerDate = findViewById(R.id.spinner_dia)
        selectedSeatTextView = findViewById(R.id.selectedSeatTextView)
        seatSelectionView = findViewById(R.id.seat_selection_view)
        actionButton = findViewById(R.id.btn_accion)

        showCity()
        showMovie()
        showCinema()
        showDate()
        showSelectedSeat()

        actionButton.text = "Seleccionar Tipo de Entrada"
        actionButton.setOnClickListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.seat_selection_container)

            when (currentFragment) {
                is ticket_amount_Fragment -> {
                    if (!isSeatTypeSelected) {
                        openSeatTypeFragment()
                    } else {
                        showPaymentSummaryFragment()
                    }
                }
                is payment_summary_Fragment -> saveReservationData()
                else -> openSeatTypeFragment()
            }
        }

        findViewById<ImageButton>(R.id.btn_close).setOnClickListener {
            finish()
        }
    }

    override fun onSeatTypeSelected() {
        isSeatTypeSelected = true
        actionButton.text = "Ver Resumen de Pago"
    }

    private fun openSeatTypeFragment() {
        val fragment = ticket_amount_Fragment().apply {
            arguments = Bundle().apply {
                putInt("selectedSeatCount", seatSelectionView.selectedSeats.size)
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.seat_selection_container, fragment, "TICKET_AMOUNT_FRAGMENT")
            .addToBackStack(null)
            .commit()
    }

    private fun showPaymentSummaryFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.seat_selection_container, payment_summary_Fragment(), "PAYMENT_SUMMARY_FRAGMENT")
            .addToBackStack(null)
            .commit()
        actionButton.text = "Pagar"
        isPaymentSummary = true
    }

    private fun showCity() {
        val cities = listOf("Cajamarca", "Lima", "Trujillo", "Cusco", "Piura")
        spinnerCity.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cities).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun showMovie() {
        RetrofitClient.instance.create(MovieApi::class.java).getMovies()
            .enqueue(object : retrofit2.Callback<List<Movie>> {
                override fun onResponse(call: retrofit2.Call<List<Movie>>, response: retrofit2.Response<List<Movie>>) {
                    response.body()?.let {
                        movies = it
                        val titles = it.map(Movie::title)
                        spinnerMovie.adapter = ArrayAdapter(this@MovieSelectionActivity, android.R.layout.simple_spinner_item, titles).apply {
                            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<Movie>>, t: Throwable) {
                    Log.e("MovieSelectionActivity", "Error al obtener películas", t)
                }
            })
    }

    private fun showCinema() {
        RetrofitClient.instance.create(CinesApi::class.java).getCines()
            .enqueue(object : retrofit2.Callback<List<Cines>> {
                override fun onResponse(call: retrofit2.Call<List<Cines>>, response: retrofit2.Response<List<Cines>>) {
                    response.body()?.let {
                        cines = it
                        val names = it.map(Cines::name)
                        spinnerCines.adapter = ArrayAdapter(this@MovieSelectionActivity, android.R.layout.simple_spinner_item, names).apply {
                            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }
                        spinnerCines.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                showHour(cines[position])
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {}
                        }
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<Cines>>, t: Throwable) {
                    Log.e("MovieSelectionActivity", "Error al obtener cines", t)
                }
            })
    }

    private fun showHour(cine: Cines) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cine.horarios).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerHora.adapter = adapter
    }

    private fun showDate() {
        val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val today = sdf.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = sdf.format(calendar.time)

        val dates = listOf(today, tomorrow)
        spinnerDate.adapter = ArrayAdapter(this, android.R.layout.select_dialog_item, dates).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun showSelectedSeat() {
        seatSelectionView.setOnSeatSelectedListener(object : SeatSelectionView.OnSeatSelectedListener {
            override fun onSeatSelected(seats: List<String>) {
                selectedSeatTextView.text = seats.joinToString(" ")
            }
        })
    }

    private fun getSelectedTicketsFromFragment(): List<Ticket> {
        val fragment = supportFragmentManager.findFragmentByTag("TICKET_AMOUNT_FRAGMENT")
        return if (fragment is ticket_amount_Fragment) {
            fragment.selectedTickets ?: listOf(Ticket("General", 100.0.toString()))
        } else emptyList()
    }

    private fun getPaymentInfoListFromFragment(): List<PaymentInfo> {
        val fragment = supportFragmentManager.findFragmentByTag("PAYMENT_SUMMARY_FRAGMENT")
        return if (fragment is payment_summary_Fragment) {
            fragment.paymentInfoList ?: listOf(
                PaymentInfo("Prueba", "correo@ejemplo.com", "Tarjeta", "BCP", 50.0)
            )
        } else emptyList()
    }

    private fun saveReservationData() {
        val reserva = Reserva(
            id = UUID.randomUUID().toString(),
            city = spinnerCity.selectedItem.toString(),
            movie = spinnerMovie.selectedItem.toString(),
            cinema = spinnerCines.selectedItem.toString(),
            hour = spinnerHora.selectedItem.toString(),
            date = spinnerDate.selectedItem.toString(),
            seat = selectedSeatTextView.text.toString(),
            tickets = getSelectedTicketsFromFragment(),
            payments = getPaymentInfoListFromFragment()
        )

        val dbRef = FirebaseDatabase.getInstance().getReference("reservas").child(reserva.id)
        dbRef.setValue(reserva).addOnSuccessListener {
            Toast.makeText(this, "Reserva registrada exitosamente", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ConfirmacionPagoActivity::class.java).apply {
                putExtra("RESERVA_ID", reserva.id)
            }
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(this, "Error al registrar la reserva", Toast.LENGTH_SHORT).show()
        }
    }
}