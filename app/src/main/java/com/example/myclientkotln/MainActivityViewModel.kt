package com.example.myclientkotln

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket


enum class KeyEventName {
    UP, DOWN, RIGHT, LEFT, CAST, TV_GUIDE, STOP, BACK, SELECT
}

class MainActivityViewModel : ViewModel() {
    private var socket: Socket? = null
    private var dataOutputStream: PrintWriter? = null

    fun initSocket(
        ip: String,
        messageCallback: (Boolean) -> Unit
    ) {
        Log.d("onstream", "ip : $ip")
        try {
            socket = Socket(InetAddress.getByName(ip), 8080)
            dataOutputStream = PrintWriter(socket!!.getOutputStream(), true)
            messageCallback(true)
        } catch (e: Exception) {
            Log.e("onstreamclient", "catch........${e.message}")
        }
        Log.e("onstreamclient", ("initsocket $socket..."))
    }


    fun disconnect() {
        dataOutputStream?.close()
        socket?.close()
    }

    fun sendEventToOnStreamApp(keyEventName: KeyEventName) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                try {
                    Log.e("onstreamclient", ("writing ${keyEventName.name}"))
                    dataOutputStream!!.println(keyEventName.name)
                    Log.e("onstreamclient", ("dataoutput ..."))
                } catch (e: IOException) {
                    Log.e("onstreamclient", ("catch ..."))
                    e.printStackTrace()
                    try {
                        dataOutputStream!!.close()
                        Log.e("onstreamclient", ("after close ..."))
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    try {
                        dataOutputStream!!.close()
                        Log.e("onstreamclient", ("after close ..."))
                    } catch (ex: IOException) {
                        ex.printStackTrace()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}