package com.example.hskflashcards

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hskflashcards.ui.theme.HSKFlashcardsTheme
import com.opencsv.CSVReader
import java.io.InputStreamReader
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HSKFlashcardsTheme {
                FlashcardApp()
            }
        }
    }
}

data class Flashcard(
    val character: String,
    val pinyin: String,
    val type: String,
    val englishTranslation: String
)

@Composable
fun FlashcardApp() {
    val context = LocalContext.current
    val flashcards = remember { loadFlashcardsFromCSV(context) }
    var currentIndex by remember { mutableStateOf(0) }
    var tapCount by remember { mutableStateOf(0) }

    // Maintain a list to keep track of shuffled indices and a queue for presentation order
    var shuffledIndices by remember { mutableStateOf(mutableListOf<Int>()) }
    var queue by remember { mutableStateOf(mutableListOf<Int>()) }

    // Track whether translation should be visible
    var showTranslation by remember { mutableStateOf(false) }
    Surface(
        color = Color(0xFFFFE4C4),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp)
                    .fillMaxHeight()
                    .clickable {
                        tapCount++
                        if (tapCount == 1) {
                            // On first tap, show the translation
                            showTranslation = true
                        } else if (tapCount == 2) {
                            // On second tap, reset tapCount and get the next flashcard
                            tapCount = 0
                            showTranslation = false

                            // Use the new function for random index counting
                            currentIndex =
                                getRandomShuffledIndex(flashcards.size, shuffledIndices, queue)
                        }
                    },
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Show flashcard content only when translation is not visible
                if (!showTranslation) {
                    Text(text = flashcards[currentIndex].character,
                        style = TextStyle(fontSize = 180.sp, fontWeight = FontWeight.Bold),
                        color = Color(0xFF3E2723),
                        modifier = Modifier.padding(vertical = 186.dp))
                    Text(text =flashcards[currentIndex].pinyin,
                        style = TextStyle(fontSize = 56.sp, fontWeight = FontWeight.Light),
                        color = Color(0xFF3E2723))
                    Text(text ="(${flashcards[currentIndex].type})",
                        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.ExtraLight),
                        color = Color(0xFF3E2723))
                } else {
                    Text(
                        text = flashcards[currentIndex].englishTranslation,
                        style = TextStyle(fontSize = 65.sp, fontWeight = FontWeight.Normal),
                        color = Color(0xFF3E2723),
                        modifier = Modifier.padding(vertical = 186.dp)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        // Use the new function for random index counting
                        currentIndex =
                            getRandomShuffledIndex(flashcards.size, shuffledIndices, queue)
                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                    IconButton(onClick = {
                        // Use the new function for random index counting
                        currentIndex =
                            getRandomShuffledIndex(flashcards.size, shuffledIndices, queue)
                    }) {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    }
}

fun loadFlashcardsFromCSV(context: Context): List<Flashcard> {
    try {
        val inputStream = context.resources.openRawResource(R.raw.flashcards)
        val reader = CSVReader(InputStreamReader(inputStream))

        val flashcardsList = mutableListOf<Flashcard>()

        reader.readAll().forEach { line ->
            if (line.size >= 4) {
                val flashcard = Flashcard(line[0], line[1], line[2], line[3])
                flashcardsList.add(flashcard)
            }
        }

        reader.close()
        return flashcardsList
    } catch (e: Exception) {
        Log.e("FlashcardApp", "Error loading flashcards", e)
        return emptyList()
    }
}

@Preview(showBackground = true)
@Composable
fun FlashcardAppPreview() {
    HSKFlashcardsTheme {
        FlashcardApp()
    }
}
