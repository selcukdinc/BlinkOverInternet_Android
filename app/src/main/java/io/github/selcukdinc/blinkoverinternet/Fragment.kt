package io.github.selcukdinc.blinkoverinternet

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.selcukdinc.blinkoverinternet.Utils.LEDViewModel
import io.github.selcukdinc.blinkoverinternet.Utils.WebSocketManager

class MainActivityV2 : ComponentActivity() {

    // ViewModel'i activity'ye bağla
    private val ledViewModel: LEDViewModel by viewModels()

    private lateinit var webSocketManager: WebSocketManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Jetpack Compose ile UI'yi burada oluşturuyoruz
        setContent {
            MyApp(ledViewModel)
        }

        // WebSocket bağlantısını başlat
        webSocketManager = WebSocketManager()
        webSocketManager.connect()

        // WebSocket üzerinden gelen LED durumu mesajlarını güncelle
        webSocketManager.setOnLEDStatusUpdateListener { status ->
            ledViewModel.updateLEDStatus(status)  // ViewModel'deki LED durumunu güncelle
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketManager.close()  // WebSocket bağlantısını kapat
    }
}

@Composable
fun MyApp(ledViewModel: LEDViewModel) {
    // LED durumu değiştiğinde UI'yi güncellemek için LiveData gözlemi
    val ledStatus = ledViewModel.ledStatus

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Current LED Status: $ledStatus")
        Button(onClick = { /* Button işlevi */ }) {
            Text("Click Me")
        }
    }
}
