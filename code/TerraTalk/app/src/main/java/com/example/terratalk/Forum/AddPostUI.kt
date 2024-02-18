package com.example.terratalk.Forum

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.terratalk.Screen
import com.example.terratalk.ui.BottomNavigation
import com.example.terratalk.ui.PageBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPost(
    viewModel: ForumViewModel,
    navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    val options = listOf("recipe", "science", "politics", "event", "travel", "gardening", "diy", "other")

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options[0]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { navController.navigate(Screen.ForumPage.route) },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                Icons.Filled.ArrowBackIosNew,
                                contentDescription = "go back button",
                                modifier = Modifier.size(25.dp)
                            )
                        }
                        Text(
                            text = "//add post",
                            fontWeight = FontWeight.Bold,
                            fontSize = 25.sp
                        )
                    }
                }
            )
        },
        // other content
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(start = 20.dp, end = 20.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("title") },
                maxLines =   3,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("content") },
                maxLines =   13,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(25.dp),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically

            ) {

                Text(text = "select post tag: ")

                Spacer(modifier = Modifier.width(10.dp))

                OutlinedButton(
                    onClick = {
                        expanded = !expanded
                    },
                    modifier = Modifier
                ) {
                    Text(
                        text = selectedOption, // Use the selectedOption variable here
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Icon(
                        Icons.Outlined.ArrowDropDown, contentDescription = "dropdown button"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOption = option
                                expanded = false
                            },
                            text = { Text(option) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(15.dp))

            OutlinedButton(
                onClick = {
                    viewModel.createPost(title, content, selectedOption)
                    navController.navigate(Screen.ForumPage.route)
                },
                modifier = Modifier
            ) {
                Text(
                    text = "add post",
                    fontSize =  20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}


