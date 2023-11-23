package com.example.wishapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.wishapp.R
import com.example.wishapp.apis.Constants
import com.example.wishapp.databinding.FragmentAddBinding
import com.example.wishapp.models.RequestAddWish
import com.example.wishapp.models.RequestUpdateWish
import com.example.wishapp.sharedpreferences.AppSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UpdateFragment : Fragment() {
    private lateinit var binding : FragmentAddBinding
    private lateinit var MyAppSharedPreferences : AppSharedPreferences
    private var idUser = ""
    private var idWish = ""
    private var fullname = ""
    private var content = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(layoutInflater, container, false)

            //get tv
        MyAppSharedPreferences = AppSharedPreferences(requireContext())
        idUser = MyAppSharedPreferences.getIdUser("idUser").toString()
        idWish = MyAppSharedPreferences.getWish("idWish").toString()
        fullname = MyAppSharedPreferences.getWish("fullname").toString()
        content = MyAppSharedPreferences.getWish("content").toString()

        //set tv
        binding.edtFullName.setText( fullname)
        binding.edtContent.setText( content)

        Toast.makeText(requireContext(), idWish, Toast.LENGTH_SHORT).show()


        //save event
        binding.apply {
            btnSave.setOnClickListener {
                if(edtFullName.text.isNotEmpty() && edtContent.text.isNotEmpty()) {
                    fullname = edtFullName.text.toString().trim()
                    content = edtContent.text.toString().trim()

                    updateWish(fullname, content)
                }
            }
        }
        return binding.root
    }

    private fun updateWish(fullname: String, content: String) {
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                val response = Constants.getInstance().updateWish(RequestUpdateWish(idUser,idWish, fullname, content)).body()

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