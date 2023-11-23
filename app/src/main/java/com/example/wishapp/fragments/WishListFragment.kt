package com.example.wishapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wishapp.R
import com.example.wishapp.adapters.WishAdapter
import com.example.wishapp.apis.Constants
import com.example.wishapp.databinding.FragmentWishListBinding
import com.example.wishapp.models.RequestDeleteWish
import com.example.wishapp.models.Wish
import com.example.wishapp.sharedpreferences.AppSharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WishListFragment : Fragment() {
    private lateinit var binding : FragmentWishListBinding
    private lateinit var MyWishList : ArrayList<Wish>
    private lateinit var MyWishAdapter : WishAdapter
    private lateinit var MyAppSharedPreferences : AppSharedPreferences
    private var idUser = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWishListBinding.inflate(layoutInflater, container, false)

        //khoi tao MyAppSharedPreferences and get IDuser

        MyAppSharedPreferences = AppSharedPreferences(requireActivity())
        idUser = MyAppSharedPreferences.getIdUser("idUser").toString()


        //khoi taoa wishlist

        MyWishList = ArrayList()

        //call api get wishlist

        getWishList()

        binding.btnAdd.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, AddFragment())
                .commit()
        }

        return binding.root
    }

    private fun getWishList() {
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                val response = Constants.getInstance().getWishList().body()
                if(response != null) {
                    MyWishList.addAll(response)
                    initAdapterAndSetLayout()
                }
            }
        }
    }

    private fun initAdapterAndSetLayout() {
        if( context == null) {
            return
        }

        MyWishAdapter = WishAdapter(requireActivity(), MyWishList, MyAppSharedPreferences,
            object : WishAdapter.IClickItemWish{
                override fun onClickUpdate(idWish: String, fullname: String, content: String) {

                    MyAppSharedPreferences.putWish("idWish", idWish)
                    MyAppSharedPreferences.putWish("fullname", fullname)
                    MyAppSharedPreferences.putWish("content", content)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, UpdateFragment())
                        .commit()
                }

                override  fun onClickRemove(idWish: String) {
                    deleteWish(idWish)
                }
            })

        binding.rcvWishList.adapter = MyWishAdapter
        binding.rcvWishList.layoutManager = LinearLayoutManager(requireActivity())
        binding.progressBar.visibility = View.GONE
    }

    private fun deleteWish(idWish: String) {
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                val response = Constants.getInstance().deletWish(RequestDeleteWish(idUser, idWish)).body()
                if(response != null) {
                    binding.progressBar.visibility = View.GONE

                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, WishListFragment())
                        .commit()

                }
                else {
                    binding.progressBar.visibility = View.GONE

                    Toast.makeText(requireContext(), response?.message , Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, LoginFragment())
                        .commit()
                }
            }
        }
    }


}