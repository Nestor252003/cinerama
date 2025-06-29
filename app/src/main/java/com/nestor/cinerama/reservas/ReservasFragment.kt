package com.nestor.cinerama.reservas

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.nestor.cinerama.R
import com.nestor.cinerama.desing.peliculas.entities.Reserva
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ReservasFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReservasFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var reservaAdapter: ReservaAdapter
    private val reservaList = mutableListOf<Reserva>()

    // Paginación Firebase
    private val PAGE_SIZE = 10
    private var lastLoadedKey: String? = null
    private var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_reservas, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        reservaAdapter = ReservaAdapter(requireContext(), reservaList)
        recyclerView.adapter = reservaAdapter

        loadReservas()

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager
                if (layoutManager != null && layoutManager.findLastVisibleItemPosition() == reservaList.size - 1) {
                    if (!isLoading) loadReservas()
                }
            }
        })

        return view
    }

    private fun loadReservas() {
        if (isLoading) return
        isLoading = true

        val reservasRef = FirebaseDatabase.getInstance().getReference("reservas")
        val query: Query = if (lastLoadedKey == null) {
            reservasRef.orderByKey().limitToLast(PAGE_SIZE)
        } else {
            reservasRef.orderByKey().endBefore(lastLoadedKey).limitToLast(PAGE_SIZE)
        }

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val tempReservaList = mutableListOf<Reserva>()
                    var tempLastKey = lastLoadedKey

                    for (dataSnapshot in snapshot.children) {
                        val reserva = dataSnapshot.getValue(Reserva::class.java)
                        if (reserva != null) {
                            tempReservaList.add(reserva)
                            tempLastKey = dataSnapshot.key
                        }
                    }

                    tempReservaList.reverse()
                    reservaList.addAll(0, tempReservaList)
                    lastLoadedKey = tempLastKey
                    reservaAdapter.notifyDataSetChanged()
                }
                isLoading = false
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al cargar reservas", error.toException())
                isLoading = false
            }
        })
    }
}