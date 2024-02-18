package com.example.terratalk.Forum


import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.terratalk.Screen
import com.example.terratalk.ui.BottomNavigation
import com.example.terratalk.ui.PageBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumPage(
    viewModel: ForumViewModel,
    navController: NavController
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "//forum",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                ) },
                actions = {
                    IconButton(onClick = {navController.navigate(Screen.AddPost.route)}) {
                        Icon(
                            Icons.Filled.Add,
                            contentDescription = "add post",
                            modifier = Modifier
                                .size(40.dp)
                        )
                    }
                }

            )
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
