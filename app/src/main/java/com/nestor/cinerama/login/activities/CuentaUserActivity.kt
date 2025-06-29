package com.nestor.cinerama.login.activities
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nestor.cinerama.R
import com.nestor.cinerama.login.Entities.User
import com.nestor.cinerama.login.adapters.UserAdapter


class CuentaUserActivity : AppCompatActivity() {
    // Variables para mostrar datos
    private lateinit var mRecycler: RecyclerView
    private var uAdapter: UserAdapter? = null
    private lateinit var mFirestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_cuenta)

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        val currentUser: FirebaseUser? = mAuth.currentUser

        // Verificar si hay un usuario logueado
        if (currentUser != null) {
            val userId = currentUser.uid

            // Inicializar Firestore
            mFirestore = FirebaseFirestore.getInstance()
            mRecycler = findViewById(R.id.rV_showData)
            mRecycler.layoutManager = LinearLayoutManager(this)

            // Consulta para obtener el usuario actual
            val query: Query = mFirestore.collection("User").whereEqualTo("userId", userId)

            // Configurar opciones para FirestoreRecyclerAdapter
            val options = FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User::class.java)
                .build()

            // Inicializar el adaptador y configurarlo para el RecyclerView
            uAdapter = UserAdapter(options, this)
            mRecycler.adapter = uAdapter

        } else {
            // Mostrar mensaje si no hay usuario logueado
            Toast.makeText(this, "No se ha encontrado el ID del usuario. Por favor, inicia sesión.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        uAdapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        uAdapter?.stopListening()
    }
}
