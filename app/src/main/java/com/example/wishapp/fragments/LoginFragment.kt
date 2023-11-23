package com.example.wishapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wishapp.R
import com.example.wishapp.apis.Constants
import com.example.wishapp.databinding.FragmentLoginBinding
import com.example.wishapp.models.RequestRegisterOrLogin
import com.example.wishapp.sharedpreferences.AppSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private lateinit var MyAppSharedPreferences : AppSharedPreferences
    private var username = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        //khoi tao MyAppSharedPreferences

        MyAppSharedPreferences = AppSharedPreferences(requireContext())

        binding.apply {
            tvRegister.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, RegisterFragment())
                    .commit()
            }

            btnLogin.setOnClickListener {
                if(edtUsername.text.isNotEmpty()) {
                    username = edtUsername.text.toString().trim()
                    loginUser(username)
                }
            }
        }
        return binding.root
    }

    private fun loginUser(username : String) {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    val response = Constants.getInstance().loginUser(RequestRegisterOrLogin(username))
                        .body()
                    if(response != null) {
                        if(response.success) {
                            //login success
                            //get id and save to sharedpreferences

                            MyAppSharedPreferences.putIdUser("idUser", response.idUser!!)
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, WishListFragment())
                                .commit()
                            progressBar.visibility = View.GONE
                        }
                        else {
                            // login fail
                            tvMessage.text = response.message
                            tvMessage.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


}