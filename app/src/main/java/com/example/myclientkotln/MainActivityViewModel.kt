package com.example.myclientkotln

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataOutputStream
import java.io.IOException
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket


enum class KeyEventName {
    UP, DOWN, RIGHT, LEFT, CAST, TV_GUIDE
}

class MainActivityViewModel : ViewModel() {
    private var socket: Socket? = null
    private var dataOutputStream: PrintWriter? = null
    private val IP = "192.168.1.105"

    fun initSocket() {
        socket = Socket(InetAddress.getByName(IP), 8080)
        dataOutputStream = PrintWriter(socket!!.getOutputStream(), true)
        Log.e("onstreamclient", ("initsocket $socket..."))
    }


    fun disconnect(){
        socket?.close()
    }

    fun sendEventToOnStreamApp(keyEventName: KeyEventName) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.e("onstreamclient", ("socket check $socket..."))
                try {
                    Log.e("onstreamclient", ("writing ${keyEventName.ordinal}"))
                    dataOutputStream!!.println(keyEventName.ordinal)
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