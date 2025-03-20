package io.github.selcukdinc.blinkoverinternet.Utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LEDViewModel : ViewModel() {
    // LED durumu: LED_ON veya LED_OFF
    private val _ledStatus = MutableLiveData<String>("LED_OFF")
    val ledStatus: MutableLiveData<String> get() = _ledStatus

    // LED durumunu güncelleyen fonksiyon
    fun updateLEDStatus(status: String) {
        _ledStatus.value = status
    }

    fun getLEDStatus(): MutableLiveData<String> {
        return _ledStatus
    }
}