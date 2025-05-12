package com.tokheim.microwaveovenproject.data

// FAKE IMPLEMENTATION FOR TESTING
import com.tokheim.microwaveovenproject.interfaces.IMicrowave
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class FakeMicrowave : IMicrowave {

    private val _doorStatusChanged = MutableSharedFlow<Boolean>()
    private val _startButtonPressed = MutableSharedFlow<Unit>()

    private var doorOpen = false
    var isHeaterOn = false
    private set

            override fun isDoorOpen(): Boolean = doorOpen

    override fun turnOnHeater() {
        isHeaterOn = true
        println("Heater turned ON")
    }

    override fun turnOffHeater() {
        isHeaterOn = false
        println("Heater turned OFF")
    }

    override val doorStatusChanged: SharedFlow<Boolean> = _doorStatusChanged
    override val startButtonPressed: SharedFlow<Unit> = _startButtonPressed

    suspend fun simulateDoorChange(isOpen: Boolean) {
        doorOpen = isOpen
        _doorStatusChanged.emit(isOpen)
    }

    suspend fun simulateStartButtonPress() {
        _startButtonPressed.emit(Unit)
    }
}