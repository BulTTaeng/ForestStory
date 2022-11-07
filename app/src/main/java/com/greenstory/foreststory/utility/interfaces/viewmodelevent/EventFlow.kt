package com.greenstory.foreststory.utility.interfaces.viewmodelevent

import kotlinx.coroutines.flow.Flow

interface EventFlow<out T> : Flow<T> {

    companion object {

        const val DEFAULT_REPLAY: Int = 3
    }
}