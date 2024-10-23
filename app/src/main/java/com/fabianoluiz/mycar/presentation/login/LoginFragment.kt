package com.fabianoluiz.mycar.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.fabianoluiz.mycar.R
import com.fabianoluiz.mycar.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private var isLoginMode = true
    private var isActive = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupUI()
        setUpListeners()
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        isActive = true
    }

    override fun onStop() {
        super.onStop()
        isActive = false
    }

    private fun setUpListeners() {
        // Alternar entre login e signup
        binding.tvSwitchMode.setOnClickListener {
            isLoginMode = !isLoginMode
            setupUI()
        }

        // Ação do botão para Login ou SignUp
        binding.btnAuth.setOnClickListener {
            if (isLoginMode) login() else signUp()
        }
    }

    private fun setupUI() {
        if (isLoginMode) {
            binding.btnAuth.text = "Login"
            binding.tvSwitchMode.text = "Don't have an account? Sign Up"
        } else {
            binding.btnAuth.text = "Sign Up"
            binding.tvSwitchMode.text = "Already have an account? Login"
        }
    }

    private fun signUp() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navigateToCarFragment()
                    } else {
                        showMessage("Registration Failed: ${task.exception?.message}")
                    }
                }
        } else {
            showMessage("Please fill out all fields")
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navigateToCarFragment()
                    } else {
                        showMessage("Login Failed: ${task.exception?.message}")
                    }
                }
        } else {
            showMessage("Please fill out all fields")
        }
    }

    private fun navigateToCarFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToCarFragment(binding.etEmail.text.toString())
        findNavController().navigate(action)
    }

    private fun showMessage(message: String) {
        if (isActive) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }
    }
}
