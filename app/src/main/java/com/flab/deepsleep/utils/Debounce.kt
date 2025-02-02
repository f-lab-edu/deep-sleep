package com.flab.deepsleep.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object Debounce {
    fun <T> debounce(
        timeMillis: Long = 300L,
        coroutineScope: CoroutineScope,
        block: suspend (T) -> Unit
    ): (T) -> Unit {
        var debounceJob: Job? = null
        return { param: T ->
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch {
                delay(timeMillis)
                block(param)
            }
        }
    }
}