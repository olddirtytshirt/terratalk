package com.example.terratalk.Forum


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.terratalk.models.User
import com.example.terratalk.ui.BottomNavigation
import com.example.terratalk.ui.PageBar
import com.google.firebase.database.FirebaseDatabase


class PostViewModel : ViewModel() {
    val user = User()

    private val title1 = mutableStateOf("")
    val title: State<String> = title1

    private val content1 = mutableStateOf("")
    val content: State<String> = content1

    fun setTitle(newTitle: String) {
        title1.value = newTitle
    }

    fun setContent(newContent: String) {
        content1.value = newContent
    }

    fun createPost(database: FirebaseDatabase){
        val username = user.username
        val userId = user.userId
        val content = content1.value
        val title = title1.value
        user.createPost(username, content, database, title)

    }

}



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