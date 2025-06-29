package com.nestor.cinerama.desing.cines.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.type.LatLng
import com.nestor.cinerama.R
import com.nestor.cinerama.desing.cines.adapters.CinemaAdapter
import com.nestor.cinerama.desing.cines.entities.Cinema

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CinemaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CinemaFragment : Fragment(), OnMapReadyCallback  {
    private lateinit var firestore: FirebaseFirestore
    private lateinit var verCine: RecyclerView
    private lateinit var cinemaAdapter: CinemaAdapter
    private val cinemaList = mutableListOf<Cinema>()
    private lateinit var mMap: GoogleMap
    private var mapFragment: SupportMapFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cinema, container, false)

        firestore = FirebaseFirestore.getInstance()
        verCine = view.findViewById(R.id.ryv_Cinemas)
        verCine.layoutManager = LinearLayoutManager(context)

        cinemaAdapter = CinemaAdapter(cinemaList)
        verCine.adapter = cinemaAdapter

        fetchCinemas()

        /*Maps*/

        val btnMostrarMapa = view.findViewById<Button>(R.id.btnMostrarMapa)
        val mapContainer = view.findViewById<FrameLayout>(R.id.mapContainer)

        btnMostrarMapa.setOnClickListener {
            if (mapContainer.visibility == View.GONE) {
                mapContainer.visibility = View.VISIBLE
                btnMostrarMapa.text = "Ocultar Mapa"

                if (mapFragment == null) {
                    mapFragment = SupportMapFragment.newInstance()
                    childFragmentManager.beginTransaction()
                        .replace(R.id.mapContainer, mapFragment!!)
                        .commit()
                    mapFragment!!.getMapAsync(this)
                }
            } else {
                mapContainer.visibility = View.GONE
                btnMostrarMapa.text = "Mostrar Mapa"
            }
        }

        return view
    }

    private fun fetchCinemas() {
        cinemaList.clear()

        firestore.collection("cines")
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Toast.makeText(context, "Error al cargar los cines: ${e.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    cinemaList.clear()
                    for (document: QueryDocumentSnapshot in querySnapshot) {
                        val cinema = document.toObject(Cinema::class.java)
                        cinemaList.add(cinema)
                    }
                    cinemaAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(context, "No se encontraron cines.", Toast.LENGTH_SHORT).show()
                }
            }
    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val cajamarca = com.google.android.gms.maps.model.LatLng(-7.162, -78.512)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cajamarca, 14f))

        val cineplanet = com.google.android.gms.maps.model.LatLng(-7.15437, -78.50519)
        mMap.addMarker(
            MarkerOptions()
                .position(cineplanet)
                .title("Cineplanet Real Plaza Cajamarca")
        )
    }
}