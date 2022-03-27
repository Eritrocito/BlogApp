package com.example.blogapp.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.blogapp.R
import com.example.blogapp.core.Result
import com.example.blogapp.data.remote.auth.AuthDataSource
import com.example.blogapp.databinding.FragmentRegisterBinding
import com.example.blogapp.domain.auth.AuthRepoImpl
import com.example.blogapp.presentation.auth.AuthViewModel
import com.example.blogapp.presentation.auth.AuthViewModelFactory

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            AuthRepoImpl(
                AuthDataSource()
            )
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)
        SignUp()
    }

    private fun SignUp() {
        binding.btnSignup.setOnClickListener {

            val username = binding.editTextUsername.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val confirmPassword = binding.editTextConfirmPassword.text.toString().trim()
            val email = binding.editTextEmail.text.toString().trim()

            if (password != confirmPassword) {
                binding.editTextConfirmPassword.error = "Passwords do not match"
                binding.editTextPassword.error = "Passwords do not match"
                return@setOnClickListener
            }

            if (validateUserData(
                    username,
                    email,
                    password,
                    confirmPassword
                )
            ) return@setOnClickListener
            //Puedo hacer ctrl+alt+m para abstraer varias funciones en una sola

            createUser(email,password,username)
            //Log.d("signUpData", "data: $username $password $confirmPassword $email")
        }
    }

    private fun createUser(email: String, password: String, username: String) {
        viewModel.signUp(email, password, username).observe(viewLifecycleOwner, Observer {result ->
            when(result){
                is Result.Loading->{
                    binding.progressBar.visibility=View.VISIBLE
                    binding.btnSignup.isEnabled=false
                }
                is Result.Success ->{
                    binding.progressBar.visibility=View.GONE
                    findNavController().navigate(R.id.action_registerFragment_to_homeScreenFragment)
                }
                is Result.Failure->{
                    binding.progressBar.visibility=View.GONE
                    binding.btnSignup.isEnabled=true
                    Toast.makeText(requireContext(),"Error:${result.exception}",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


    private fun validateUserData(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (username.isEmpty()) {
            binding.editTextUsername.error = "Username is empty"
            return true
        }

        if (email.isEmpty()) {
            binding.editTextEmail.error = "Email is empty"
            return true
        }

        if (password.isEmpty()) {
            binding.editTextPassword.error = "Password is empty"
            return true
        }

        if (confirmPassword.isEmpty()) {
            binding.editTextConfirmPassword.error = "Password is empty"
            return true
        }
        return false
    }
}