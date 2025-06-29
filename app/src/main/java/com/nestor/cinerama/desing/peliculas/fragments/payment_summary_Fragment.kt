package com.nestor.cinerama.desing.peliculas.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Spinner
import com.nestor.cinerama.R
import com.nestor.cinerama.desing.peliculas.entities.PaymentInfo


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [payment_summary_Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class payment_summary_Fragment : Fragment() {
    private lateinit var radioGroupPrimary: RadioGroup
    private lateinit var spinnerSecondary: Spinner
    private lateinit var nombre: EditText
    private lateinit var email: EditText
    private var total: Double = 100.0 // Total simulado

    internal var paymentInfoList: MutableList<PaymentInfo> = mutableListOf()

    fun getPaymentInfoList(): List<PaymentInfo> {
        Log.d("PaymentInfoFragment", "Lista de PaymentInfo: $paymentInfoList")
        return paymentInfoList
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentInfoList = mutableListOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_payment_summary_, container, false)

        // Referencia de los elementos del layout
        nombre = view.findViewById(R.id.txt_name)
        email = view.findViewById(R.id.txt_email)
        radioGroupPrimary = view.findViewById(R.id.radioGroupPrimary)
        spinnerSecondary = view.findViewById(R.id.spinnerSecondary)

        setupRadioGroupListeners()

        val btnAddPaymentInfo: Button = view.findViewById(R.id.btn_proceed_payment)
        btnAddPaymentInfo.setOnClickListener {
            initializePaymentInfoList()
        }

        return view
    }

    private fun initializePaymentInfoList() {
        val nombreUsuario = nombre.text.toString()
        val emailUsuario = email.text.toString()
        var tipoPago = "N/A"
        var proveedor = "N/A"

        when (radioGroupPrimary.checkedRadioButtonId) {
            R.id.radio_tarjeta -> {
                tipoPago = "Tarjeta"
                proveedor = spinnerSecondary.selectedItem.toString()
            }
            R.id.radio_appAgorra -> {
                tipoPago = "Aplicación Agora"
                proveedor = spinnerSecondary.selectedItem.toString()
            }
            R.id.radio_billeteras -> {
                tipoPago = "Billetera Electrónica"
                proveedor = spinnerSecondary.selectedItem.toString()
            }
        }

        paymentInfoList.add(PaymentInfo(nombreUsuario, emailUsuario, tipoPago, proveedor, total))

        Log.d("PaymentInfoFragment", "Datos agregados: $paymentInfoList")
    }

    private fun setupRadioGroupListeners() {
        radioGroupPrimary.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId != -1) {
                spinnerSecondary.visibility = View.VISIBLE
                val adapter = when (checkedId) {
                    R.id.radio_tarjeta -> ArrayAdapter.createFromResource(
                        requireContext(), R.array.credit_card_providers_array,
                        android.R.layout.simple_spinner_item
                    )
                    R.id.radio_appAgorra -> ArrayAdapter.createFromResource(
                        requireContext(), R.array.app_agora_options_array,
                        android.R.layout.simple_spinner_item
                    )
                    R.id.radio_billeteras -> ArrayAdapter.createFromResource(
                        requireContext(), R.array.e_wallet_options_array,
                        android.R.layout.simple_spinner_item
                    )
                    else -> null
                }

                adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerSecondary.adapter = adapter
            }
        }
    }

}