package com.chamika.newsapptest.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.chamika.newsapptest.R
import com.chamika.newsapptest.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

    }

    private fun registerUser() {
        binding.apply {
            val isUserValidUser  = viewModel.validateUserData(
                userName = etUsername.text.toString().trim(),
                userEmail = etEmail.text.toString().trim(),
                userPW = etPassword.text.toString().trim()
            )

            if (isUserValidUser.first){
                viewModel.setUserEmail(userEmail = etEmail.text.toString().trim())
                viewModel.setPassword(password = etPassword.text.toString().trim())
                Toast.makeText(requireActivity(), isUserValidUser.second, Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
            }else{
                Toast.makeText(requireActivity(), isUserValidUser.second, Toast.LENGTH_SHORT).show()
            }

        }
    }


}