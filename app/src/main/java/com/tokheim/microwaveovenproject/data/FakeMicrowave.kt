package com.tokheim.microwaveovenproject.data

// FAKE IMPLEMENTATION FOR TESTING
import com.tokheim.microwaveovenproject.interfaces.IMicrowave
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class FakeMicrowave : IMicrowave {
    private var heaterOn = false
    private var doorOpen = false

    override fun turnOnHeater() {
        println("[Microwave] Heater ON")
        heaterOn = true
    }

    override fun turnOffHeater() {
        println("[Microwave] Heater OFF")
        heaterOn = false
    }

    override fun isDoorOpen(): Boolean = doorOpen

    private val _doorStatusChanged = MutableSharedFlow<Boolean>()
    override val doorStatusChanged: SharedFlow<Boolean> = _doorStatusChanged

    private val _startButtonPressed = MutableSharedFlow<Unit>()
    override val startButtonPressed: SharedFlow<Unit> = _startButtonPressed

    suspend fun simulateDoorChange(isOpen: Boolean) {
        doorOpen = isOpen
        _doorStatusChanged.emit(isOpen)
    }

    suspend fun simulateStartButtonPress() {
        _startButtonPressed.emit(Unit)
    }
}
