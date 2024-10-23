package com.fabianoluiz.mycar.presentation.car

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.fabianoluiz.mycar.R
import com.fabianoluiz.mycar.databinding.FragmentCarBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class CarFragment : Fragment() {
    private lateinit var binding: FragmentCarBinding
    private val args: CarFragmentArgs by navArgs()
    private fun setupUI() {
        binding.tvUserEmail.text = args.userEmail
    }

    override fun onCreateView (
        inflater: LayoutInflater , container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCarBinding .inflate(inflater, container, false)
        setupUI()
        setUpListeners()
        loadVehicle()
        return binding.root
    }


    private fun setUpListeners() {
        binding.btnRegisterVehicle.setOnClickListener {
            registerVehicle()

        }
    }

    private fun registerVehicle() {
        val userData = hashMapOf(
            "placa" to binding.etPlate.text.toString(),
            "fabricante" to binding.etManufacturer.text.toString(),
            "modelo" to binding.etModel.text.toString(),
            "ano" to binding.etYear.text.toString().toInt(),
            //"userId" to Firebase.auth.currentUser?.uid.toString()
        )
        Firebase.firestore.collection("cars")
            //.add(userData)
            .document(Firebase.auth.currentUser?.uid.toString())
            .set(userData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Data Saved", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadVehicle () {
        Firebase .firestore.collection( "cars").document( Firebase .auth.currentUser?.uid.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    binding.etPlate.setText(document.getString( "placa"))
                    binding.etManufacturer .setText(document.getString( "fabricante" ))
                    binding.etModel.setText(document.getString( "modelo" ))
                    binding.etYear.setText(document.getLong( "ano").toString())
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }




}
