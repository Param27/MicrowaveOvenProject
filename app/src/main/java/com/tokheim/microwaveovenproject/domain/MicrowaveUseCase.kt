package com.tokheim.microwaveovenproject.domain

// USE CASE
interface MicrowaveUseCase {
    suspend fun handleStartButtonPressed()
    suspend fun handleDoorStatusChanged(isOpen: Boolean)
}