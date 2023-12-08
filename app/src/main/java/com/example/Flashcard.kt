package com.example

data class Flashcard(
    val character: String,
    val pinyin: String,
    val type: String, // noun, verb, adverb, etc.
    val englishTranslation: String
)