package com.tokheim.microwaveovenproject.domain

//  ENTITY
data class MicrowaveState(
    val isDoorOpen: Boolean = false,
    val isHeaterOn: Boolean = false
)
