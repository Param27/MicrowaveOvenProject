package com.tokheim.microwaveovenproject.interfaces

// INTERFACE FROM GIVEN
import kotlinx.coroutines.flow.SharedFlow

interface IMicrowave {
    fun turnOnHeater()
    fun turnOffHeater()
    fun isDoorOpen(): Boolean
    val doorStatusChanged: SharedFlow<Boolean>
    val startButtonPressed: SharedFlow<Unit>
}

