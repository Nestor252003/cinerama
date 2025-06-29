package com.nestor.cinerama.desing.cines.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nestor.cinerama.R
import com.nestor.cinerama.desing.cines.adapters.CinemaSelecionAdapter
import com.nestor.cinerama.desing.cines.entities.CineHorario
import com.nestor.cinerama.desing.peliculas.entities.Movie
import com.nestor.cinerama.desing.peliculas.services.MovieApi
import com.nestor.cinerama.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class Fragment_Cine_selection : Fragment() {

    private lateinit var cinemaRecyclerView: RecyclerView
    private lateinit var cinemaAdapter: CinemaSelecionAdapter
    private val cinemaList = mutableListOf<CineHorario>()
    private lateinit var movieApi: MovieApi
    private lateinit var spinnerEleccionDia: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment__cine_selection, container, false)

        cinemaRecyclerView = view.findViewById(R.id.rv_cinemas)
        cinemaRecyclerView.layoutManager = LinearLayoutManager(context)

        cinemaAdapter = CinemaSelecionAdapter(cinemaList)
        cinemaRecyclerView.adapter = cinemaAdapter

        spinnerEleccionDia = view.findViewById(R.id.spinner_eleccionDia)
        setupDaysSpinner()

        fetchCinemas()

        return view
    }

    private fun setupDaysSpinner() {
        val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val today = dateFormat.format(calendar.time)
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = dateFormat.format(calendar.time)

        val dates = listOf(today, tomorrow)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.select_dialog_item, dates)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEleccionDia.adapter = adapter
    }

    private fun fetchCinemas() {
        movieApi = RetrofitClient.instance.create(MovieApi::class.java)
        val call = movieApi.getMovies()

        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (response.isSuccessful && response.body() != null) {
                    val peliculas = response.body()!!
                    if (peliculas.size > 1) {
                        val movie = peliculas[1]
                        fetchCineHorariosFromMovie(movie)
                    } else {
                        Toast.makeText(context, "No hay suficientes películas", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchCineHorariosFromMovie(movie: Movie) {
        cinemaList.clear()
        cinemaAdapter.notifyDataSetChanged()

        val cineHorariosList = movie.cineHorarios
        if (!cineHorariosList.isNullOrEmpty()) {
            cinemaList.addAll(cineHorariosList)
            cinemaAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(context, "No se encontraron horarios de cines.", Toast.LENGTH_SHORT).show()
        }
    }
}
