package com.example.wishapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wishapp.R
import com.example.wishapp.apis.Constants
import com.example.wishapp.databinding.FragmentRegisterBinding
import com.example.wishapp.models.RequestRegisterOrLogin
import com.example.wishapp.sharedpreferences.AppSharedPreferences
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegisterFragment : Fragment() {
    private lateinit var binding : FragmentRegisterBinding
    private lateinit var MyAppSharedPreferences : AppSharedPreferences
    private var username = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)

        //init MyAppSharedPreferences

        MyAppSharedPreferences = AppSharedPreferences(requireContext())

        binding.apply {
            btnRegister.setOnClickListener {
                if(edtUsername.text.isNotEmpty()) {
                    username  = edtUsername.text.toString()
                    registerUser(username)
                }
                else {
                    Snackbar.make(it, "Vui Lòng nhập mã số sinh viên!!", Snackbar.LENGTH_LONG).show()
                }
            }

            tvLogin.setOnClickListener {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.frame_layout, LoginFragment())
                    .commit()
            }
        }

        return binding.root
    }

    private fun registerUser(username : String) {
        binding.apply {
            progressBar.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    val response = Constants.getInstance().registerUser(RequestRegisterOrLogin(username))
                        .body()
                    if(response != null) {
                        if(response.success) {
                            //register success
                            // get and save into SharedPreferences

                            MyAppSharedPreferences.putIdUser("idUser", response.idUser!!)
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, WishListFragment())
                                .commit()
                            progressBar.visibility = View.GONE
                        }
                        else {
                            // register fail
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