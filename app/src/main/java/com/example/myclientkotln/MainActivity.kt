package com.example.myclientkotln

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myclientkotln.databinding.ActivityMain11Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain11Binding

    private lateinit var mainActivityViewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain11Binding.inflate(layoutInflater)
        setContentView(binding.root)
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        lifecycleScope.launch(Dispatchers.IO) {
            mainActivityViewModel.initSocket()
        }
        binding.keypadCast.setOnClickListener {
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.CAST)
        }

        binding.keypadDown.setOnClickListener {
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.DOWN)
        }

        binding.keypadUp.setOnClickListener {
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.UP)
        }

        binding.keypadRight.setOnClickListener {
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.RIGHT)
        }

        binding.keypadLeft.setOnClickListener {
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.LEFT)
        }

        binding.keypadTvguide.setOnClickListener {
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.TV_GUIDE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch(Dispatchers.IO) {
            mainActivityViewModel.disconnect()
        }
    }
}