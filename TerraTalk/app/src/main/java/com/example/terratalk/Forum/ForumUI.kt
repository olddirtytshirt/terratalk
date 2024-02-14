package com.example.terratalk.Forum


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.terratalk.ui.BottomNavigation
import com.example.terratalk.ui.PageBar



@Composable
fun ForumPage (navController: NavController){
    Scaffold(
        topBar = {
            PageBar("//forum")
        },
        bottomBar = {
            BottomNavigation(navController)
        }
        //content
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
            ) {

            }
        }

    }
}
    /*
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

     */