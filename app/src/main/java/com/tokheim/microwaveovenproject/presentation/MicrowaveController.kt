package com.tokheim.microwaveovenproject.presentation

import com.tokheim.microwaveovenproject.domain.MicrowaveUseCase
import com.tokheim.microwaveovenproject.interfaces.IMicrowave
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MicrowaveController(
    private val microwave: IMicrowave,
    private val useCase: MicrowaveUseCase,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default) // Default scope
) {
    private var heatingJob: Job? = null

    init {
        observeEvents()
    }

    private fun observeEvents() {
        scope.launch {
            microwave.doorStatusChanged.collectLatest {
                useCase.handleDoorStatusChanged(it)
                if (it) cancelHeatingTimer() // Door opened — stop timer
            }
        }

        scope.launch {
            microwave.startButtonPressed.collectLatest {
                cancelHeatingTimer() // Cancel any existing run
                heatingJob = scope.launch {
                    useCase.handleStartButtonPressed()
                }
            }
        }
    }

    private fun cancelHeatingTimer() {
        heatingJob?.cancel()
        heatingJob = null
    }

    fun cancel() {
        scope.cancel()
    }
}
