package io.github.selcukdinc.blinkoverinternet

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.selcukdinc.blinkoverinternet.Utils.LEDViewModel
import io.github.selcukdinc.blinkoverinternet.Utils.WebSocketManager
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {


    private val webSocketManager = WebSocketManager()

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        webSocketManager.connect()


        setContent {
            val ledViewModel: LEDViewModel = viewModel()

            LEDControlScreen(
                webSocketManager = webSocketManager,
                ledViewModel = ledViewModel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketManager.close()
    }
}


@Composable
fun LEDControlScreen(webSocketManager: WebSocketManager,
                     ledViewModel: LEDViewModel) {

    var localLed by remember { mutableStateOf("LED_OFF") }
    var localLedBool by remember { mutableStateOf(false) } // Butonların Aktif olup olmamasını
    var btnBgColor by remember { mutableStateOf(Color.Red) }




    LaunchedEffect (webSocketManager.ledStatus){
        if (webSocketManager.getLEDStatus() == "LED_ON_AND") {
            localLed = "LED Açık"
            localLedBool = true
            btnBgColor = Color(0xFF3FEB59)
        }else if(webSocketManager.getLEDStatus() == "LED_OFF_AND"){
            localLed = "LED Kapalı"
            localLedBool = false
            btnBgColor = Color(0xFFEB3F3F)
            //0xFFEB3F3F
        }
    }

    LaunchedEffect(webSocketManager.sensorData) {

    }

    // UI bileşenleri ile LED durumunu göster

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Sensor Data: ${webSocketManager.sensorData}")
        Button(
            onClick = {
                if(localLedBool){
                    webSocketManager.sendMessage("LED_OFF")
                }else{
                    webSocketManager.sendMessage("LED_ON")
                }
            },
            colors = ButtonColors(containerColor = btnBgColor, contentColor = Color.White, disabledContainerColor = btnBgColor, disabledContentColor = btnBgColor)

        ) {
            if(localLedBool){
                Text("LED Açık")
            }else{
                Text("LED Kapalı")
            }
        }
        /*
        Text(text = "LED Durumu: $localLed", modifier = Modifier.padding(8.dp))

        // "LED Aç" butonu
        Button(
            onClick = {
                webSocketManager.sendMessage("LED_ON")
                updateLedString()
                Log.d("LED Status", "Current LED Status_OnButton: ${webSocketManager.getLEDStatus()}")
                      },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "LED Aç")

        }

        // "LED Kapat" butonu
        Button(
            onClick = {
                webSocketManager.sendMessage("LED_OFF")
                updateLedString()
                Log.d("LED Status", "Current LED Status_OffButton: ${webSocketManager.getLEDStatus()}")
                      },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "LED Kapat")
        }
         */
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    //LEDControlScreen()
}