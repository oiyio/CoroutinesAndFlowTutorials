/*
 * Copyright 2016-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

// This file was automatically generated from flow.md by Knit tool. Do not edit.
package com.smarttoolfactory.tutorial1_1coroutinesbasics.playground

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main() = runBlocking<Unit> {
    val nums = (1..3).asFlow().onEach {
        println("1st flow. value : $it")
        delay(100)
    } // numbers 1..3 every 100 ms
    val strs = flowOf("one", "two", "three").onEach {
        println("2nd flow. value : $it")
        delay(4000)
    } // strings every 4000 ms

    val startTime = System.currentTimeMillis() // remember the start time

    nums.zip(strs) { a, b -> "$a -> $b" } // compose a single string with "zip"
        .map {
            println("Applying map for : $it")
            it
        }
        .collect { value -> // collect and print 
            println("$value at ${System.currentTimeMillis() - startTime} ms from start")
        }
}
