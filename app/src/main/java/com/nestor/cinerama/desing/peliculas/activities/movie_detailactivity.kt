package com.nestor.cinerama.desing.peliculas.activities

import android.content.Intent

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.ChipGroup
import com.nestor.cinerama.R
import com.nestor.cinerama.network.RetrofitClient
import retrofit2.Callback
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.nestor.cinerama.desing.cines.adapters.CinemaSelecionAdapter
import com.nestor.cinerama.desing.cines.entities.CineHorario
import com.nestor.cinerama.desing.peliculas.entities.Movie
import com.nestor.cinerama.desing.peliculas.services.MovieApi
import retrofit2.Call
import retrofit2.Response

class movie_detailactivity : AppCompatActivity() {
    private lateinit var peliculaTitle: TextView
    private lateinit var generoHoraEdad: TextView
    private lateinit var peliculaSinopsis: TextView
    private lateinit var directorMovie: TextView
    private lateinit var disponibleMovie: TextView
    private lateinit var url: ImageView
    private lateinit var urlTrailer: VideoView
    private lateinit var idiomaPelicula: ChipGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.movie_detail_activity)

        initViews()

        val movieId = intent.getStringExtra("MOVIE_ID")
        if (movieId != null) {
            fetchMovieDetails(movieId)
        } else {
            showErrorToast("Error: No se pudo obtener el ID de la película")
        }

        horarioData()
    }

    private fun initViews() {
        peliculaTitle = findViewById(R.id.pelicula_title)
        generoHoraEdad = findViewById(R.id.genero_hora_edad)
        urlTrailer = findViewById(R.id.video_Trailer)
        peliculaSinopsis = findViewById(R.id.pelicula_sinopsis)
        url = findViewById(R.id.img_url)
        idiomaPelicula = findViewById(R.id.idioma_pelicula)
        directorMovie = findViewById(R.id.director_name)
        disponibleMovie = findViewById(R.id.disponible_content)

        val buttonVer = findViewById<Button>(R.id.btn_ver)
        buttonVer?.setOnClickListener {
            startActivity(Intent(this, MovieSelectionActivity::class.java))
        }
    }

    private fun horarioData() {
        val movieApi = RetrofitClient.instance.create(MovieApi::class.java)
        val call: Call<List<Movie>> = movieApi.getMovies()
        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(
                call: Call<List<Movie>?>,
                response: Response<List<Movie>?>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val movies = response.body()!!
                    for (movie in movies) {
                        Log.d("HorarioData", "Movie: ${movie.title}")
                        movie.cineHorarios?.forEach { cine ->
                            Log.d("HorarioData", "Cine: ${cine.name}")
                            cine.horarios?.forEach { hora ->
                                Log.d("HorarioData", "Horario: $hora")
                            }
                        }
                    }
                } else {
                    Log.e("HorarioData", "Error al obtener datos")
                }
            }
            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Log.e("HorarioData", "Error: ${t.message}")
            }
        })
    }

    private fun fetchMovieDetails(movieId: String) {
        val movieApi = RetrofitClient.instance.create(MovieApi::class.java)
        movieApi.getMovieDetails(movieId).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                val movie = response.body()
                if (response.isSuccessful && movie != null) {
                    updateUI(movie)

                    val cineHorariosList = listOf(
                        CineHorario("1", listOf("", "2", "3", "4"), "panel")
                    )
                    setupCineHorariosRecyclerView(cineHorariosList)
                } else {
                    Log.e("fetchMovieDetails", "Película nula o respuesta no exitosa.")
                    showErrorToast("No se pudo obtener los detalles de la película")
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                showErrorToast("Error: ${t.message}")
            }
        })
    }

    private fun setupCineHorariosRecyclerView(cineHorariosList: List<CineHorario>) {
        val recyclerView = findViewById<RecyclerView>(R.id.ryv_mostrarCine)
        if (cineHorariosList.isNotEmpty()) {
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = CinemaSelecionAdapter(cineHorariosList)
        } else {
            Log.e("SetupRecyclerView", "cineHorariosList está vacío o es nulo.")
            showErrorToast("No hay horarios de cine disponibles")
        }
    }

    private fun updateUI(movie: Movie) {
        peliculaTitle.text = movie.title
        peliculaSinopsis.text = movie.sinopsis
        directorMovie.text = movie.director
        generoHoraEdad.text = "${movie.genre} | ${movie.durationMovie} min | ${movie.age}"

        Glide.with(this).load(movie.url).into(url)

        idiomaPelicula.removeAllViews()
        movie.idioma?.forEach { idioma ->
            val chip = Chip(this)
            chip.text = idioma
            chip.setTextColor(resources.getColor(R.color.white))
            chip.setChipBackgroundColorResource(R.color.black)
            idiomaPelicula.addView(chip)
        }

        disponibleMovie.text = movie.disponible?.joinToString(", ") ?: ""
    }

    private fun showErrorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}