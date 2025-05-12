package com.tokheim.microwaveovenproject.presentation


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tokheim.microwaveovenproject.data.FakeMicrowave
import com.tokheim.microwaveovenproject.domain.MicrowaveUseCaseImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val microwave = FakeMicrowave()
    private val useCase = MicrowaveUseCaseImpl(microwave)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val controller = MicrowaveController(microwave, useCase, scope)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MicrowaveUI(microwave = microwave)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        controller.cancel()
    }
}

@Composable
fun MicrowaveUI(microwave: FakeMicrowave) {
    val scope = rememberCoroutineScope()
    var status by remember { mutableStateOf("Ready") }

    LaunchedEffect(Unit) {
        microwave.doorStatusChanged.collectLatest {
            status = if (it) "Door Open" else "Door Closed"
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Microwave Status: $status", fontSize = 18.sp)
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
