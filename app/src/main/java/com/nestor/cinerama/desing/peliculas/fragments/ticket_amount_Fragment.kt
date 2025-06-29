package com.nestor.cinerama.desing.peliculas.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.nestor.cinerama.R
import com.nestor.cinerama.desing.peliculas.entities.Ticket

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ticket_amount_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ticket_amount_Fragment : Fragment() {
    private lateinit var incrementGeneralButton: Button
    private lateinit var decrementGeneralButton: Button
    private lateinit var incrementChildButton: Button
    private lateinit var decrementChildButton: Button
    private lateinit var incrementSeniorButton: Button
    private lateinit var decrementSeniorButton: Button
    private lateinit var generalTicketAmountText: TextView
    private lateinit var childTicketAmountText: TextView
    private lateinit var seniorTicketAmountText: TextView
    private lateinit var remainingSeatsText: TextView

    private var generalTicketCount = 0
    private var childTicketCount = 0
    private var seniorTicketCount = 0

    private var totalSelectedSeats = 0
    private var remainingSeats = 0

    internal val selectedTickets: MutableList<Ticket> = mutableListOf()

    fun getSelectedTickets(): List<Ticket> = selectedTickets

    interface OnSeatTypeSelectedListener {
        fun onSeatTypeSelected()
    }

    private var listener: OnSeatTypeSelectedListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnSeatTypeSelectedListener) {
            listener = context
        } else {
            throw RuntimeException("$context debe implementar OnSeatTypeSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ticket_amount_, container, false)

        // Recupera número de asientos seleccionados
        totalSelectedSeats = arguments?.getInt("selectedSeatCount", 0) ?: 0
        Toast.makeText(context, "Asientos seleccionados: $totalSelectedSeats", Toast.LENGTH_SHORT).show()

        // Referencias UI
        incrementGeneralButton = view.findViewById(R.id.btn_increment_general)
        decrementGeneralButton = view.findViewById(R.id.btn_decrement_general)
        generalTicketAmountText = view.findViewById(R.id.txt_general_amount)

        incrementChildButton = view.findViewById(R.id.btn_increment_child)
        decrementChildButton = view.findViewById(R.id.btn_decrement_child)
        childTicketAmountText = view.findViewById(R.id.txt_child_amount)

        incrementSeniorButton = view.findViewById(R.id.btn_increment_senior)
        decrementSeniorButton = view.findViewById(R.id.btn_decrement_senior)
        seniorTicketAmountText = view.findViewById(R.id.txt_senior_amount)

        remainingSeatsText = view.findViewById(R.id.txt_remaining_seats)

        remainingSeats = totalSelectedSeats
        updateRemainingSeats()

        // Listeners
        incrementGeneralButton.setOnClickListener { updateTicketCount("general", true) }
        decrementGeneralButton.setOnClickListener { updateTicketCount("general", false) }

        incrementChildButton.setOnClickListener { updateTicketCount("child", true) }
        decrementChildButton.setOnClickListener { updateTicketCount("child", false) }

        incrementSeniorButton.setOnClickListener { updateTicketCount("senior", true) }
        decrementSeniorButton.setOnClickListener { updateTicketCount("senior", false) }

        return view
    }

    private fun updateTicketCount(ticketType: String, isIncrement: Boolean) {
        when (ticketType) {
            "general" -> {
                if (isIncrement && remainingSeats > 0) {
                    generalTicketCount++
                    remainingSeats--
                    addTicket("general")
                } else if (!isIncrement && generalTicketCount > 0) {
                    generalTicketCount--
                    remainingSeats++
                    removeTicket("general")
                }
                generalTicketAmountText.text = generalTicketCount.toString()
            }

            "child" -> {
                if (isIncrement && remainingSeats > 0) {
                    childTicketCount++
                    remainingSeats--
                    addTicket("child")
                } else if (!isIncrement && childTicketCount > 0) {
                    childTicketCount--
                    remainingSeats++
                    removeTicket("child")
                }
                childTicketAmountText.text = childTicketCount.toString()
            }

            "senior" -> {
                if (isIncrement && remainingSeats > 0) {
                    seniorTicketCount++
                    remainingSeats--
                    addTicket("senior")
                } else if (!isIncrement && seniorTicketCount > 0) {
                    seniorTicketCount--
                    remainingSeats++
                    removeTicket("senior")
                }
                seniorTicketAmountText.text = seniorTicketCount.toString()
            }
        }

        updateRemainingSeats()
        showSelectedTickets()

        if (remainingSeats == 0) {
            listener?.onSeatTypeSelected()
        }
    }

    private fun updateRemainingSeats() {
        remainingSeatsText.text = "Asientos restantes: $remainingSeats"
    }

    private fun addTicket(ticketType: String) {
        val price = getTicketPrice(ticketType)
        selectedTickets.add(Ticket(ticketType, price.toString()))
    }

    private fun removeTicket(ticketType: String) {
        for (i in selectedTickets.size - 1 downTo 0) {
            if (selectedTickets[i].type == ticketType) {
                selectedTickets.removeAt(i)
                break
            }
        }
    }

    private fun getTicketPrice(ticketType: String): Int {
        return when (ticketType) {
            "general" -> 20
            "child" -> 10
            "senior" -> 15
            else -> 0
        }
    }

    private fun showSelectedTickets() {
        selectedTickets.forEach {
            Log.d("Selected Ticket", "Tipo: ${it.type}, Precio: S/ ${it.price}")
        }
    }
}