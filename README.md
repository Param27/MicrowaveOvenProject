To keep things testable and maintainable, I followed Clean Architecture — separating UI, business rules, and data layers. 
I wrote unit tests to verify heater control logic using a fake microwave, and ensured coroutine-based event handling works as expected using Kotlin’s test toolkit. 
This let me validate the system independently of the UI, and confidently build on top with Jetpack Compose.

Technologies Used

- Kotlin 1.9.0
- Jetpack Compose 1.5.1
- Coroutines & SharedFlow
- Android Clean Architecture
