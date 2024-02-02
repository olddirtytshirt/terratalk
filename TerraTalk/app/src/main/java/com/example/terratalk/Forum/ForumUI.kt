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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.database.FirebaseDatabase



@Composable
fun Post (postViewModel: PostViewModel){
    val title = remember{postViewModel.title.value}
    val content = remember {postViewModel.content.value}

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
                onValueChange = {postViewModel.setTitle(it)},
            )

            OutlinedTextField(
                label = { Text("Content")},
                value = content,
                onValueChange = {postViewModel.setContent(it)},
            )

            Button(onClick = { /*CreatePost(title, content)
        */ }) {
                postViewModel.createPost(FirebaseDatabase.getInstance())
                Text("Make Post")
            }
        }
    }
}