package com.nestor.cinerama.desing.peliculas.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nestor.cinerama.desing.MainActivity
import com.nestor.cinerama.R
import com.nestor.cinerama.reservas.ReservasFragment

class ConfirmacionPagoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmacion_pago)

        // Obtén el `reservaId` pasado a esta actividad
        val reservaId = intent.getStringExtra("RESERVA_ID")

        // Busca el TextView y establece el `reservaId`, con verificación para nulo
        val txtCodReserva: TextView = findViewById(R.id.txt_codReserva)
        txtCodReserva.text = reservaId ?: "Error: Código de reserva no disponible."

        // Botón para ir a historial de reservas
        val btnHistorial: Button = findViewById(R.id.btn_historial)
        btnHistorial.setOnClickListener {
            // Cierra la actividad actual
            finish()
            // Inicia la actividad de historial de reservas
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("FRAGMENT", ReservasFragment::class.java.simpleName)
            startActivity(intent)
        }
    }
}