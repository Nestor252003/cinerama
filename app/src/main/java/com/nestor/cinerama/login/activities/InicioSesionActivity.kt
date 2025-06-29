package com.nestor.cinerama.login.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.nestor.cinerama.desing.MainActivity
import com.nestor.cinerama.R

class InicioSesionActivity : AppCompatActivity() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var btnIngresar: Button
    private lateinit var btnUnete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_inicio_sesion)

        mAuth = FirebaseAuth.getInstance()

        emailEditText = findViewById(R.id.txt_emailUser)
        passwordEditText = findViewById(R.id.txt_passwordUser)
        btnIngresar = findViewById(R.id.btn_ingresar)
        btnUnete = findViewById(R.id.btn_addUser)

        btnIngresar.setOnClickListener {
            val emailUser = emailEditText.text.toString().trim()
            val passwordUser = passwordEditText.text.toString().trim()

            if (emailUser.isEmpty() || passwordUser.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese todos los datos", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(emailUser, passwordUser)
            }
        }

        btnUnete.setOnClickListener {
            startActivity(Intent(this, CreateUserActivity::class.java))
        }
    }

    private fun loginUser(emailUser: String, passwordUser: String) {
        mAuth.signInWithEmailAndPassword(emailUser, passwordUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al iniciar sesión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
