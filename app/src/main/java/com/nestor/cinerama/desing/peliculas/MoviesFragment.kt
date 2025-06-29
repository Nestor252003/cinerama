package com.nestor.cinerama.desing.peliculas


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nestor.cinerama.R
import com.nestor.cinerama.desing.peliculas.adapters.MoviesImageAdapter
import com.nestor.cinerama.desing.peliculas.entities.Movie
import com.nestor.cinerama.desing.peliculas.services.MovieApi
import com.nestor.cinerama.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MoviesFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var moviesAdapter: MoviesImageAdapter
    private var peliculas: List<Movie> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movies, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        fetchMovies()

        return view
    }

    private fun fetchMovies() {
        val movieApi = RetrofitClient.instance.create(MovieApi::class.java)
        val call = movieApi.getMovies()

        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (response.isSuccessful && response.body() != null) {
                    peliculas = response.body()!!
                    moviesAdapter = MoviesImageAdapter(peliculas, requireContext())
                    recyclerView.adapter = moviesAdapter
                } else {
                    Toast.makeText(requireContext(), "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("RetrofitError", t.message ?: "Error desconocido", t)
            }
        })
    }
}