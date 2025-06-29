package com.nestor.cinerama.login.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nestor.cinerama.R

import com.nestor.cinerama.login.activities.CreateUserActivity
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.nestor.cinerama.login.Entities.User


class UserAdapter(options: FirestoreRecyclerOptions<User>, private val activity: Activity) :
    FirestoreRecyclerAdapter<User, UserAdapter.ViewHolder>(options) {

    private val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onBindViewHolder(holder: ViewHolder, position: Int, user: User) {
        val documentSnapshot: DocumentSnapshot = snapshots.getSnapshot(holder.adapterPosition)
        val id = documentSnapshot.id

        holder.name.text = user.name
        holder.email.text = user.email
        holder.password.text = user.password

        holder.btnEditUser.setOnClickListener {
            val intent = Intent(activity, CreateUserActivity::class.java)
            intent.putExtra("id_User", id)
            activity.startActivity(intent)
        }

        holder.btnEliminar.setOnClickListener {
            userDelete(id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_show_user, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val email: TextView = itemView.findViewById(R.id.tvUsername)
        val password: TextView = itemView.findViewById(R.id.tvUserPassword)
        val btnEliminar: ImageView = itemView.findViewById(R.id.btn_delete)
        val btnEditUser: ImageView = itemView.findViewById(R.id.btn_editUser)
    }

    private fun userDelete(id: String) {
        mFirestore.collection("User").document(id).delete()
            .addOnSuccessListener {
                Toast.makeText(activity, "Usuario eliminado con éxito", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(activity, "Error al eliminar usuario: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}