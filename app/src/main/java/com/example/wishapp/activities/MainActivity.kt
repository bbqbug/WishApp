package com.example.wishapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wishapp.R
import com.example.wishapp.databinding.ActivityMainBinding
import com.example.wishapp.fragments.LoginFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, LoginFragment())
            .commit()
    }
}