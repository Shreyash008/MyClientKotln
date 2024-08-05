package com.example.myclientkotln

import android.content.Context
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.myclientkotln.databinding.ActivityMain11Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain11Binding
    private lateinit var vibrator: Vibrator
    private lateinit var mainActivityViewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain11Binding.inflate(layoutInflater)
        setContentView(binding.root)
        mainActivityViewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        // Get the Vibrator service
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        binding.button.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                if (binding.ipTv.isVisible) {
                    mainActivityViewModel.initSocket(binding.ipTv.text.toString()) {
                        if (it) {
                            lifecycleScope.launch(Dispatchers.Main)
                            {
                                powerOn()
                            }
                        }
                    }
                } else {
                    mainActivityViewModel.disconnect()
                    lifecycleScope.launch(Dispatchers.Main)
                    { powerOff() }

                }

            }
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.keypadCast.setOnClickListener {
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.HOME)
            triggerHapticFeedback()
        }

        binding.keypadDown.setOnClickListener {
            triggerHapticFeedback()
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.DOWN)
        }

        binding.keypadUp.setOnClickListener {
            triggerHapticFeedback()
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.UP)
        }

        binding.keypadRight.setOnClickListener {
            triggerHapticFeedback()
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.RIGHT)
        }

        binding.keypadLeft.setOnClickListener {
            triggerHapticFeedback()
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.LEFT)
        }

        binding.selectKeypad.setOnClickListener {
            triggerHapticFeedback()
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.SELECT)
        }

        binding.keypadTvguide.setOnClickListener {
            triggerHapticFeedback()
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.TV_GUIDE)
        }

        binding.keypadBack.setOnClickListener {
            triggerHapticFeedback()
            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.BACK)
        }
    }

    override fun onStop() {
        lifecycleScope.launch(Dispatchers.IO) {
            Log.e("onstreamclient", "onStop is called")
//            mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.STOP)
            mainActivityViewModel.disconnect()
        }
        super.onStop()
    }

    private fun triggerHapticFeedback(time: Long = 50) {
        // Check if the vibrator is available
        if (vibrator.hasVibrator()) {
            // You can specify the duration in milliseconds
            vibrator.vibrate(time) // Vibrate for 50 milliseconds
        }
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

    private fun powerOff() {
        triggerHapticFeedback(200)
        mainActivityViewModel.sendEventToOnStreamApp(KeyEventName.STOP)
        binding.ipTv.visibility = View.VISIBLE
        binding.button.setImageResource(R.drawable.power_off)
    }

    private fun powerOn() {
        triggerHapticFeedback(200)
        binding.button.setImageResource(R.drawable.power_on)
        binding.ipTv.clearFocus()
        minimizeKeyboard()
        binding.ipTv.visibility = View.INVISIBLE
    }

    private fun minimizeKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.ipTv.windowToken, 0)
    }

}