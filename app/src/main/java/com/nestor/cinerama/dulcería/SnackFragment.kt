package com.nestor.cinerama.dulcería

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
import com.nestor.cinerama.network.RetrofitSnack
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SnackFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var dulceriaAdapter: DulceriaAdapter
    private val dulceriaList: MutableList<Dulceria> = mutableListOf()

    // Paginación
    private var isLoading = false
    private var currentPage = 1
    private val PAGE_SIZE = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_snack, container, false)

        recyclerView = view.findViewById(R.id.ryv_mostarCombos)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        dulceriaAdapter = DulceriaAdapter(dulceriaList, requireContext())
        recyclerView.adapter = dulceriaAdapter

        loadPage(currentPage)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= (lastVisibleItem + 2)) {
                    loadPage(++currentPage)
                    isLoading = true
                }
            }
        })

        return view
    }

    private fun loadPage(page: Int) {
        val snackApi = RetrofitSnack.getRetrofitInstance().create(SnackApi::class.java)
        val call: Call<List<Dulceria>> = snackApi.getDulceria(page, PAGE_SIZE)

        call.enqueue(object : Callback<List<Dulceria>> {
            override fun onResponse(call: Call<List<Dulceria>>, response: Response<List<Dulceria>>) {
                if (response.isSuccessful && response.body() != null) {
                    val newItems = response.body()!!
                    dulceriaList.addAll(newItems)
                    dulceriaAdapter.notifyDataSetChanged()
                    Log.d("LoadPage", "Página: $page, Elementos cargados: ${newItems.size}")
                } else {
                    Toast.makeText(context, "Error al obtener datos", Toast.LENGTH_SHORT).show()
                }
                isLoading = false
            }

            override fun onFailure(call: Call<List<Dulceria>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("RetrofitError", t.message ?: "Error desconocido", t)
                isLoading = false
            }
        })
    }
}