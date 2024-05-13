package com.dear.http.util


import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStateAtLeast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.ConcurrentHashMap

sealed class Event {
    data class ShowInit(val msg: String) : Event()
}

object FlowEventBus {

    private val flowEvents = ConcurrentHashMap<String, MutableSharedFlow<Event>>()

    fun getFlow(key: String): MutableSharedFlow<Event> {
        return flowEvents[key] ?: MutableSharedFlow<Event>().also {
            flowEvents[key] = it
        }
    }

    fun post(event: Event, wait: Long = 0) {
        MainScope().launch {
            delay(wait)
            getFlow(event.javaClass.simpleName).emit(event)
        }
    }

    inline fun <reified T : Event> observe(
        lifecycleOwner: LifecycleOwner,
        minState: Lifecycle.State = Lifecycle.State.CREATED,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        crossinline onReceived: (T) -> Unit
    ) = lifecycleOwner.lifecycleScope.launch(dispatcher) {
        getFlow(T::class.java.simpleName).collect { event ->
            lifecycleOwner.lifecycle.whenStateAtLeast(minState) {
                if (event is T) {
                    onReceived(event)
                }
            }
        }
    }

}
