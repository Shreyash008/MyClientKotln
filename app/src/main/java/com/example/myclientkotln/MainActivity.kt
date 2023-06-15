package com.example.myclientkotln

import android.os.Bundle
import android.util.Log
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
            binding.button.setOnClickListener {
                mainActivityViewModel.initSocket(binding.ipTv.text.toString())
            }
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

        binding.selectKeypad.setOnClickListener {
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.SELECT)
        }

        binding.keypadTvguide.setOnClickListener {
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.TV_GUIDE)
        }
    }

    override fun onStop() {
        Log.e("onstreamclient", "onStop is called")
        mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.STOP)
        super.onStop()
    }

    override fun onBackPressed() {
        Log.e("onstreamclient", "onStop is called")
        mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.BACK)
        super.onBackPressed()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleScope.launch(Dispatchers.IO) {
            mainActivityViewModel.disconnect()
        }
    }
}