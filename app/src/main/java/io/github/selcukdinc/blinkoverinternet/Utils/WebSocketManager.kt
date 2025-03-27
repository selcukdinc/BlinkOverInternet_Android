package io.github.selcukdinc.blinkoverinternet.Utils

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import okhttp3.*
import io.github.selcukdinc.blinkoverinternet.BuildConfig
import org.json.JSONObject

class WebSocketManager {
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()
    val serverAddress = BuildConfig.SERVER_IP
    val serverPort = BuildConfig.SERVER_PORT

    private val serverUrl = "ws://$serverAddress:$serverPort"

    var ledStatus  by mutableStateOf("LED_OFF")
    var sensorData by mutableStateOf("")

    private var onLEDStatusUpdateListener: ((String) -> Unit)? = null

    private var onSensorDataUpdateListener: ((String) -> Unit)? = null

    fun setOnLEDStatusUpdateListener(listener: (String) -> Unit) {
        onLEDStatusUpdateListener = listener
    }

    fun connect() {
        //Log.d("WebSocket", "serverAddress is '$serverAddress' , serverPort is '$serverPort'")
        val request = Request.Builder().url(serverUrl).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket", "Connected to server")
                sendMessage("ANDROID_CONNECTED")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Received message: '$text'")

                try {
                    val jsonObject = JSONObject(text)

                    val accelX = jsonObject.getJSONObject("accelerometer").getDouble("x")
                    val accelY = jsonObject.getJSONObject("accelerometer").getDouble("y")
                    val accelZ = jsonObject.getJSONObject("accelerometer").getDouble("z")

                    val gyroX = jsonObject.getJSONObject("gyroscope").getDouble("x")
                    val gyroY = jsonObject.getJSONObject("gyroscope").getDouble("y")
                    val gyroZ = jsonObject.getJSONObject("gyroscope").getDouble("z")

                    val temperature = jsonObject.getDouble("temperature")

                    sensorData = """
                        Accel: X:$accelX, Y:$accelY, Z:$accelZ
                        Gyro: X:$gyroX, Y:$gyroY, Z:$gyroZ
                        Temp: $temperature°C
                    """.trimIndent()

                    onSensorDataUpdateListener?.invoke(sensorData)

                } catch (e: Exception) {
                    if (text == "LED_ON_AND" || text == "LED_OFF_AND") {
                        ledStatus = text
                    }
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Error: ${t.message}")
            }
        })
    }

    // LED durumu UI'yi güncelleyebilmek için
    fun updateLEDStatusOnUI(status: String) {
        // Burada UI'yi güncelleyin
        // Örneğin, LED durumunu bir TextView'de gösterebilirsiniz:
        Log.d("LED Status", "Current LED Status: $status")

        // UI'de güncellenmesi için bir callback veya LiveData kullanabilirsiniz


    }
    fun getLEDStatus(): String{
        return ledStatus
    }

    fun sendMessage(message: String) {
        Log.d("WebSocket", "Sending message: $message")
        webSocket?.send(message)
    }

    fun close() {
        webSocket?.close(1000, "Closing")
    }
}


