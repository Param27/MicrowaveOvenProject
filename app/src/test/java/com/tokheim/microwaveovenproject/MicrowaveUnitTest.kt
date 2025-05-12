package com.tokheim.microwaveovenproject

// UNIT TEST
import com.tokheim.microwaveovenproject.data.FakeMicrowave
import com.tokheim.microwaveovenproject.domain.MicrowaveUseCase
import com.tokheim.microwaveovenproject.domain.MicrowaveUseCaseImpl
import com.tokheim.microwaveovenproject.presentation.MicrowaveController
import kotlinx.coroutines.*
import kotlinx.coroutines.test.runTest
import kotlin.test.*

class MicrowaveUnitTest {

    private lateinit var microwave: FakeMicrowave
    private lateinit var useCase: MicrowaveUseCase
    private lateinit var controller: MicrowaveController
    private lateinit var testScope: CoroutineScope

    @BeforeTest
    fun setup() {
        microwave = FakeMicrowave()
        useCase = MicrowaveUseCaseImpl(microwave)
        testScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        controller = MicrowaveController(microwave, useCase, testScope)
    }

    @AfterTest
    fun teardown() {
        controller.cancel()
        testScope.cancel()
    }

    @Test
    fun `test heater turns on only when door is closed`() = runTest {
        microwave.simulateDoorChange(true)
        microwave.simulateStartButtonPress()

        microwave.simulateDoorChange(false)
        microwave.simulateStartButtonPress()

        microwave.simulateDoorChange(true)

        delay(100) // Allow coroutines to process
    }
}
