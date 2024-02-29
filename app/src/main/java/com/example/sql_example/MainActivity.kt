package com.example.sql_example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sql_example.ui.theme.Sql_exampleTheme

class MainActivity : ComponentActivity() {
    val wordViewModel: WordViewModel by viewModels {
        WordViewModelFactory((application as WordsApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Sql_exampleTheme {
                val context = LocalContext.current
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    Column{
                        AppStructure()
                    }
                }
            }
        }
    }

    @Composable
    fun AppStructure(){
        val words = remember{ mutableStateListOf<Word>() }
        wordViewModel.allWords.observe(this) { wordList ->
            words.clear()
            words.addAll(wordList)
        }
        var showCompanies by remember { mutableStateOf(false) }
        Column {
            AddWord { word ->
                words.add(word)
                wordViewModel.insert(word)
            }
            Button(
                onClick = { showCompanies = !showCompanies },
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(if (showCompanies) "Hide Companies" else "Show Companies")
            }

            if (showCompanies) {
                CompanyList(words)
            }
        }
    }

    @Composable
    fun CompanyList(words: List<Word>) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Words",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (words.isNotEmpty()) {
                words.forEach { word ->
                    WordItem(word)
                }
            } else {
                Text(
                    text = "No companies available",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }

    @Composable
    fun WordItem(word: Word) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text(
                text = "Company Name: ${word.name}"
            )
        }
    }


    @Composable
    fun AddWord(onSubmit: (Word) -> Unit) {
        var wordName by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = wordName,
                onValueChange = { wordName = it },
                label = { Text("Company Name") },
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    val name = wordName
                    val word = Word(name = name)
                    onSubmit(word)
                },
                enabled = wordName.isNotBlank()
            ) {
                Text("Submit")
            }
        }
    }
}


