package com.smarttoolfactory.tutorial2_1flowbasics.flow

import com.smarttoolfactory.tutorial2_1flowbasics.AssertionException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class FlowTestObserver<T>(
    private val coroutineScope: CoroutineScope,
    private val flow: Flow<T>
) {

    private val testValues = mutableListOf<T>()

    private val job: Job = coroutineScope.launch {
        flow.collect { testValues.add(it) }
    }

    fun assertNoValues(): FlowTestObserver<T> {
        if (testValues.isNotEmpty()) throw AssertionException(
            "Assertion error with actual size ${testValues.size}"
        )
        return this
    }

    fun assertValueCount(count: Int): FlowTestObserver<T> {
        if (count < 0) throw AssertionException(
            "Assertion error! value count cannot be smaller than zero"
        )
        if (count != testValues.size) throw AssertionException(
            "Assertion error! with expected $count while actual ${testValues.size}"
        )
        return this
    }

    fun assertValues(vararg predicates: T): FlowTestObserver<T> {
        if (!testValues.containsAll(predicates.asList())) throw  AssertionException("Assertion error some ")
        return this
    }

//    fun assertValues(predicate: List<T>.() -> Boolean): TestObserver<T> {
//        testValues.predicate()
//        return this
//    }
//
//    fun values(predicate: List<T>.() -> Unit): TestObserver<T> {
//        testValues.predicate()
//        return this
//    }

    fun assertValues(predicate: (List<T>) -> Boolean): FlowTestObserver<T> {
        predicate(testValues)
        return this
    }

    fun values(predicate: (List<T>) -> Unit): FlowTestObserver<T> {
        predicate(testValues)
        return this
    }

    fun values(): List<T> {
        return testValues
    }

    fun dispose() {
        job.cancel()
    }

}

fun <T> Flow<T>.test(scope: CoroutineScope): FlowTestObserver<T> {
    return FlowTestObserver(scope, this)
}