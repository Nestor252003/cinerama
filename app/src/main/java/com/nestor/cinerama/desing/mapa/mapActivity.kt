package com.nestor.cinerama.desing.mapa

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.nestor.cinerama.R
import com.nestor.cinerama.databinding.ActivityMapBinding

class mapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_map)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.map)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val puntos =listOf(
            PuntoMapa(LatLng(-7.1491706,-78.5125977),"Cinerama"),
            PuntoMapa(LatLng(-7.1544844,-78.5085531),"Cine Planet"),
            PuntoMapa(LatLng(-7.1517887,-78.5079092),"Cinemark")
        )
        for (punto in puntos){
            mMap.addMarker(MarkerOptions().position(punto.latLng).title(punto.titulo))
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntos[0].latLng, 6f))
    }
}