package com.nestor.cinerama.login.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Spinner
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.nestor.cinerama.R
import com.nestor.cinerama.desing.MainActivity
import java.util.UUID

class CreateUserActivity : AppCompatActivity() {

    private lateinit var mfirestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    private lateinit var passwordPrimary: TextView
    private lateinit var passwordConfirm: TextView
    private lateinit var nombre: EditText
    private lateinit var correo: EditText
    private lateinit var apellidosPaterno: EditText
    private lateinit var apellidoMaterno: EditText
    private lateinit var telefono: EditText
    private lateinit var gender: RadioGroup
    private lateinit var btnRegister: Button
    private lateinit var cineFavorito: Spinner
    private lateinit var cineName: ArrayList<String>
    private lateinit var cineMap: HashMap<String, String>
    private lateinit var tipoDocumentoSpinner: Spinner
    private lateinit var numeroDocumento: EditText
    private lateinit var fechaNacimiento: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        val id = intent.getStringExtra("id_User")
        inicializarVistas()
        inicializarFirebase()
        configurarSpinners()
        configurarListeners(id)
    }
    private fun inicializarVistas() {
        gender = findViewById(R.id.rb_genero)
        nombre = findViewById(R.id.txt_nameUser)
        apellidosPaterno = findViewById(R.id.txt_apellidoPaterno)
        apellidoMaterno = findViewById(R.id.txt_apellidoMaterno)
        telefono = findViewById(R.id.txt_phone)
        correo = findViewById(R.id.txt_emailUser)
        passwordPrimary = findViewById(R.id.txt_passworPrimary)
        passwordConfirm = findViewById(R.id.txt_passwordConfirm)
        numeroDocumento = findViewById(R.id.txt_nro_documento)
        fechaNacimiento = findViewById(R.id.txt_fecha_nacimiento)
        cineFavorito = findViewById(R.id.spinnerCineplanet)
        tipoDocumentoSpinner = findViewById(R.id.spinnerTipoDocumento)
        btnRegister = findViewById(R.id.btn_addUser)
    }

    private fun inicializarFirebase() {
        mfirestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        cineName = ArrayList()
        cineMap = HashMap()
    }

    private fun configurarSpinners() {
        datosCineFirestore()
        cargarTiposDocumento(tipoDocumentoSpinner)
    }

    private fun configurarListeners(id: String?) {
        passwordConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        if (id.isNullOrEmpty()) {
            btnRegister.setOnClickListener { registrarUsuario() }
        } else {
            btnRegister.text = "Actualizar"
            getUser(id)
            btnRegister.setOnClickListener {
                // implementación
            }
        }
    }

    private fun registrarUsuario() {
        val nameUser = nombre.text.toString().trim()
        val surnamePaternal = apellidosPaterno.text.toString().trim()
        val surnameMaternal = apellidoMaterno.text.toString().trim()
        val phone = telefono.text.toString().trim()
        val emailUser = correo.text.toString().trim()
        val passwordUser = passwordConfirm.text.toString().trim()
        val numDocumento = numeroDocumento.text.toString().trim()
        val fechaNac = fechaNacimiento.text.toString().trim()
        val tipoDoc = tipoDocumentoSpinner.selectedItem?.toString() ?: ""

        val generoSeleccionado = genderSelected() ?: run {
            Toast.makeText(this, "Seleccione un género", Toast.LENGTH_SHORT).show()
            return
        }

        val cineSeleccionadoNombre = cineFavorito.selectedItem?.toString() ?: "No seleccionado"
        val cineFavoritoId = cineMap[cineSeleccionadoNombre] ?: run {
            Toast.makeText(this, "Por favor selecciona un cine favorito", Toast.LENGTH_SHORT).show()
            return
        }

        if (passwordPrimary.text.toString() != passwordConfirm.text.toString()) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return
        }

        if (nameUser.isEmpty() || emailUser.isEmpty() || numDocumento.isEmpty() || fechaNac.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.createUserWithEmailAndPassword(emailUser, passwordUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = mAuth.currentUser?.uid ?: return@addOnCompleteListener

                    val userMap = hashMapOf(
                        "nombre" to nameUser,
                        "email" to emailUser,
                        "apellidoPaterno" to surnamePaternal,
                        "apellidoMaterno" to surnameMaternal,
                        "telefono" to phone,
                        "genero" to generoSeleccionado,
                        "cineFavoritoId" to cineFavoritoId,
                        "tipoDocumento" to tipoDoc,
                        "numeroDocumento" to numDocumento,
                        "fechaNacimiento" to fechaNac
                    )

                    mfirestore.collection("Usuarios").document(userId).set(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al guardar usuario", Toast.LENGTH_SHORT).show()
                            Log.e("Firestore", "Error al guardar usuario", e)
                        }
                } else {
                    Toast.makeText(this, "Error al registrar autenticación", Toast.LENGTH_SHORT).show()
                    Log.e("FirebaseAuth", "Error: ${task.exception}")
                }
            }
    }

    private fun getUser(id: String) {
        mfirestore.collection("Usuarios").document(id).get().addOnSuccessListener { documentSnapshot ->
            nombre.setText(documentSnapshot.getString("name"))
            correo.setText(documentSnapshot.getString("email"))
            passwordConfirm.setText(documentSnapshot.getString("password"))
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "Data error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validatePassword() {
        val password = passwordPrimary.text.toString().trim()
        val confirmPassword = passwordConfirm.text.toString().trim()
        if (password != confirmPassword) {
            passwordConfirm.error = "Passwords do not match"
        }
    }

    private fun datosCineFirestore() {
        FirebaseFirestore.getInstance().collection("cines").get().addOnCompleteListener { task: Task<QuerySnapshot> ->
            if (task.isSuccessful) {
                val cineSnapshots = task.result
                cineName.clear()
                cineMap.clear()

                if (cineSnapshots != null && !cineSnapshots.isEmpty) {
                    for (document in cineSnapshots.documents) {
                        val idcine = document.id
                        val nameCine = document.getString("name")
                        if (nameCine != null) {
                            cineName.add(nameCine)
                            cineMap[nameCine] = idcine
                            Log.d("FirestoreData", "Cine encontrado: $nameCine (ID: $idcine)")
                        } else {
                            Log.w("FirestoreData", "Documento sin nombre de cine: ${document.id}")
                        }
                    }

                    if (cineName.isNotEmpty()) {
                        val adapter = ArrayAdapter(this, R.layout.spinner_item, cineName)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        cineFavorito.adapter = adapter
                    }
                } else {
                    Log.w("FirestoreData", "No se encontraron documentos en la colección 'cines'.")
                    Toast.makeText(this, "No se encontraron cines", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Error al cargar cines", Toast.LENGTH_SHORT).show()
                Log.e("FirestoreData", "Error al obtener cines", task.exception)
            }
        }
    }

    private fun genderSelected(): String? {
        val selectedId = gender.checkedRadioButtonId
        return if (selectedId != -1) {
            findViewById<RadioButton>(selectedId).text.toString()
        } else {
            null
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    private fun cargarTiposDocumento(spinner: Spinner) {
        val tiposDocumento = listOf("DNI", "Carné de extranjería", "Pasaporte")

        val adaptador = ArrayAdapter(
            this,
            R.layout.spinner_item,
            tiposDocumento
        )

        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adaptador
    }
}
