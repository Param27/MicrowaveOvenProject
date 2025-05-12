package com.tokheim.microwaveovenproject.presentation

//  PRESENTATION LAYER (MicrowaveController

import com.tokheim.microwaveovenproject.domain.MicrowaveUseCase
import com.tokheim.microwaveovenproject.interfaces.IMicrowave
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MicrowaveController(
    private val microwave: IMicrowave,
    private val useCase: MicrowaveUseCase,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default) // Default scope
) {
    init {
        observeEvents()
    }

    private fun observeEvents() {
        scope.launch {
            microwave.doorStatusChanged.collectLatest {
                useCase.handleDoorStatusChanged(it)
            }
        }

        scope.launch {
            microwave.startButtonPressed.collectLatest {
                useCase.handleStartButtonPressed()
            }
        }
    }

    // provide a cancel method for manual lifecycle management
    fun cancel() {
        scope.cancel()
    }
}
