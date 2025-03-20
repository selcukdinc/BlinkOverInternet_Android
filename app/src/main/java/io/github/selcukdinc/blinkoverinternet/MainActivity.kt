package io.github.selcukdinc.blinkoverinternet

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
//            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
//                Button(onClick = { webSocketManager.sendMessage("LED_ON") }) {
//
//                    Text("LED_ON")
//                }
//                Spacer(modifier =
//                Modifier.height(16.dp))
//                Button(onClick = { webSocketManager.sendMessage("LED_OFF") }) {
//                    Text("LED_OFF")
//                }
//            }
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
    // ViewModel'ı al

    webSocketManager.setOnLEDStatusUpdateListener { status ->
        ledViewModel.updateLEDStatus(status)
    }

    // LED durumu üzerinde gözlem yap
    val ledStatus = ledViewModel.ledStatus

    // UI bileşenleri ile LED durumunu göster
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "LED Durumu: ${ledStatus.value}", modifier = Modifier.padding(8.dp))

        // LED durumuna göre bir icon veya renk değiştirme
        if (ledStatus.value == "LED_ON_AND") {
            // LED açık ise, yeşil renkte bir icon veya metin
            Text(text = "LED Açık", modifier = Modifier.padding(8.dp))
        } else {
            // LED kapalı ise, kırmızı renkte bir icon veya metin
            Text(text = "LED Kapalı", modifier = Modifier.padding(8.dp))
        }

        // "LED Aç" butonu
        Button(
            onClick = {
                webSocketManager.sendMessage("LED_ON")
                //ledViewModel.updateLEDStatus(webSocketManager.getLEDStatus())
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
                //ledViewModel.updateLEDStatus(webSocketManager.getLEDStatus())
                Log.d("LED Status", "Current LED Status_OffButton: ${webSocketManager.getLEDStatus()}")
                      },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = "LED Kapat")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    //LEDControlScreen()
}