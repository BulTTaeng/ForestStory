package com.greenstory.foreststory.utility.interfaces.viewmodelevent

import kotlinx.coroutines.flow.FlowCollector

interface MutableEventFlow<T> : EventFlow<T>, FlowCollector<T>
