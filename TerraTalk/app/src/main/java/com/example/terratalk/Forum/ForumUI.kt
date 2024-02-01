package com.example.terratalk.Forum

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun Post (
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, top = 10.dp, end = 30.dp, bottom = 20.dp)
            .background(color = Color.White)

    ) {

        Column (
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                label = { Text("title")},
                value = title,
                onValueChange = { title = it },
            )

            OutlinedTextField(
                label = { Text("Content")},
                value = content,
                onValueChange = { content = it },
            )

            Button(onClick = { /*CreatePost(title, content)
        */ }) {
                Text("Make Post")
            }
        }
    }
}