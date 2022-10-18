package com.greenstory.foreststory.utility.`interface`.viewmodelevent

import kotlinx.coroutines.flow.FlowCollector

interface MutableEventFlow<T> : EventFlow<T>, FlowCollector<T>
