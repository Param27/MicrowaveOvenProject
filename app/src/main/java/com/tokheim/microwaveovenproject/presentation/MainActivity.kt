package com.tokheim.microwaveovenproject.presentation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tokheim.microwaveovenproject.data.FakeMicrowave
import com.tokheim.microwaveovenproject.domain.MicrowaveUseCaseImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow


class MainActivity : ComponentActivity() {

    private val microwave = FakeMicrowave()
    private val heaterStatus = MutableStateFlow(false)

    private val useCase = object : MicrowaveUseCaseImpl(microwave) {
        override suspend fun handleStartButtonPressed() {
            if (!microwave.isDoorOpen()) {
                microwave.turnOnHeater()
                heaterStatus.emit(true)
                delay(60_000L) // 1 minute heating duration
                microwave.turnOffHeater()
                heaterStatus.emit(false)
            }
        }

        override suspend fun handleDoorStatusChanged(isOpen: Boolean) {
            super.handleDoorStatusChanged(isOpen)
            if (isOpen) heaterStatus.emit(false)
        }
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val controller = MicrowaveController(microwave, useCase, scope)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MicrowaveUI(microwave = microwave, heaterStatus = heaterStatus)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.cancel()
    }
}

@Composable
fun MicrowaveUI(microwave: FakeMicrowave, heaterStatus: MutableStateFlow<Boolean>) {
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf("Ready") }
    val isHeaterOn by heaterStatus.collectAsState(initial = false)

    LaunchedEffect(Unit) {
        microwave.doorStatusChanged.collectLatest {
            status = if (it) "Door Open" else "Door Closed"
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Microwave Status: $status", fontSize = 18.sp)
        Text("Heater: ${if (isHeaterOn) "ON" else "OFF"}", fontSize = 16.sp, modifier = Modifier.padding(top = 8.dp))
        Spacer(Modifier.height(16.dp))
        Button(onClick = { scope.launch { microwave.simulateDoorChange(true) } }) {
            Text("Open Door")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { scope.launch { microwave.simulateDoorChange(false) } }) {
            Text("Close Door")
        }
        Spacer(Modifier.height(8.dp))
        Button(onClick = { scope.launch { microwave.simulateStartButtonPress() } }) {
            Text("Start")
        }
    }
}
