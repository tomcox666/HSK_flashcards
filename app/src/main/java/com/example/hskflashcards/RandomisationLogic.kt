package com.example.hskflashcards

import kotlin.random.Random

fun getRandomShuffledIndex(size: Int, shuffledIndices: MutableList<Int>, queue: MutableList<Int>): Int {
    // If the queue is empty, repopulate it with shuffled indices
    if (queue.isEmpty()) {
        shuffledIndices.clear()
        shuffledIndices.addAll(0 until size)
        shuffledIndices.shuffle()
        queue.addAll(shuffledIndices)
    }

    // Get the next index from the queue
    return queue.removeAt(0)


}