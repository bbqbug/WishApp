package com.example.wishapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.wishapp.R
import com.example.wishapp.apis.Constants
import com.example.wishapp.databinding.FragmentAddBinding
import com.example.wishapp.models.RequestAddWish
import com.example.wishapp.sharedpreferences.AppSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddFragment : Fragment() {
    private lateinit var binding : FragmentAddBinding
    private lateinit var MyAppSharedPreferences : AppSharedPreferences
    private var idUser = ""
    private var fullname = ""
    private var content = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentAddBinding.inflate(layoutInflater, container, false)

        MyAppSharedPreferences = AppSharedPreferences(requireContext())
        idUser = MyAppSharedPreferences.getIdUser("idUser").toString()

        binding.apply {
            btnSave.setOnClickListener {
                if(edtFullName.text.isNotEmpty() && edtContent.text.isNotEmpty()) {
                    fullname = edtFullName.text.toString().trim()
                    content = edtContent.text.toString().trim()

                    addWish(fullname, content)
                }
            }
        }
        return binding.root
    }

    private fun addWish(fullname: String, content: String) {
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                val response = Constants.getInstance().addWish(RequestAddWish(idUser, fullname, content)).body()

                if(response != null) {
                    if(response.success) {
                        binding.progressBar.visibility = View.GONE

                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, WishListFragment())
                            .commit()
                    }
                    else {
                        binding.progressBar.visibility = View.GONE

                        Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, LoginFragment())
                            .commit()
                    }
                }
            }
        }
    }


}