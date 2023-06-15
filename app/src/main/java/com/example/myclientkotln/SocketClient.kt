import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class SocketClient(
    private val serverIp: String,
    private val serverPort: Int,
    private val messageCallback: (String) -> Unit
) {

    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: PrintWriter

    fun connect() {
        try {
            Log.d("", "connecting")
            socket = Socket(serverIp, serverPort)
            Log.d("", "connected")
            reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            writer = PrintWriter(socket.getOutputStream(), true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun send(message: String) {
            Log.d("","sending")
            writer.println(message)
    }

    fun startListeningForData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                while (true) {
                    val receivedMessage = reader.readLine()
                    if (receivedMessage != null) {
                        messageCallback(receivedMessage)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun disconnect() {
        try {
            writer.close()
            reader.close()
            socket.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
