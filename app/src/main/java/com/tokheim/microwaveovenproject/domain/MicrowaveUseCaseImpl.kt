package com.tokheim.microwaveovenproject.domain

import com.tokheim.microwaveovenproject.interfaces.IMicrowave


// IMPLEMENTATION OF USE CASE
open class MicrowaveUseCaseImpl(
    private val microwave: IMicrowave
) : MicrowaveUseCase {

    private var state = MicrowaveState()

    override suspend fun handleStartButtonPressed() {
        if (!state.isDoorOpen) {
            microwave.turnOnHeater()
            state = state.copy(isHeaterOn = true)
        }
    }

    override suspend fun handleDoorStatusChanged(isOpen: Boolean) {
        state = state.copy(isDoorOpen = isOpen)
        if (isOpen && state.isHeaterOn) {
            microwave.turnOffHeater()
            state = state.copy(isHeaterOn = false)
        }
    }
}
