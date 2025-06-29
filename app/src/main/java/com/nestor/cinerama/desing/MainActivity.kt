package com.nestor.cinerama.desing

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.nestor.cinerama.R
import com.nestor.cinerama.databinding.ActivityMainBinding
import com.nestor.cinerama.desing.cines.fragments.CinemaFragment
import com.nestor.cinerama.desing.peliculas.MoviesFragment
import com.nestor.cinerama.desing.start.MainHomeFragment
import com.nestor.cinerama.dulcería.SnackFragment
import com.nestor.cinerama.login.activities.InicioSesionActivity
import com.nestor.cinerama.reservas.ReservasFragment

class MainActivity : AppCompatActivity() {
    // Variable de view binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Fragmento inicial
        replaceFragment(MainHomeFragment())

        // Navegación inferior
        binding.btnNavegation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeM -> replaceFragment(MainHomeFragment())
                R.id.moviesM -> replaceFragment(MoviesFragment())
                R.id.cinemaM -> replaceFragment(CinemaFragment())
                R.id.foodM -> replaceFragment(SnackFragment())
                R.id.moreM -> replaceFragment(ReservasFragment())
            }
            true
        }

        // Botón para iniciar sesión
        binding.btnRegistro.setOnClickListener {
            val intent = Intent(this, InicioSesionActivity::class.java)
            startActivity(intent)
        }

        /*
        // Código comentado para buscador
        binding.buscadorSearchMain.setOnClickListener {
            val intent = Intent(this, BuscadorMain::class.java)
            startActivity(intent)
        }
        */
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .commit()
    }
}