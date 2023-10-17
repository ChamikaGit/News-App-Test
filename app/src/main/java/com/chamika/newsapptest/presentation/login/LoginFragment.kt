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
import com.chamika.newsapptest.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegister.setOnClickListener {
            navigateToRegistration()
        }

        binding.buttonLogin.setOnClickListener {
            userLogin()
        }
    }

    private fun navigateToRegistration() {
        findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
    }

    private fun userLogin() {
        binding.apply {
            val isUserValidUser = viewModel.validateInputUserData(
                userEmail = etEmail.text.toString(),
                userPW = etPassword.text.toString()
            )

            val isRegisteredUser = viewModel.isValidUser(
                enteredEmail = etEmail.text.toString().trim(),
                enteredPw = etPassword.text.toString().trim()
            )

            if (isUserValidUser.first) {
                if (isRegisteredUser) {
                    viewModel.setUserLoginStatus(isLogged = true)
                    val navController = findNavController()
                    navController.navigate(R.id.action_loginFragment_to_main_nav_graph)
                    navController.setGraph(R.navigation.dashboard_nav_graph)
                    navController.graph.setStartDestination(R.id.homeFragment)
                } else {
                    viewModel.setUserLoginStatus(isLogged = false)
                    Toast.makeText(
                        requireActivity(),
                        "User name or password invalid",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                viewModel.setUserLoginStatus(isLogged = false)
                Toast.makeText(requireActivity(), isUserValidUser.second, Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }


}