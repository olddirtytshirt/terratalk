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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.ContentAlpha
import com.example.terratalk.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostPage(
    viewModel: ForumViewModel,
    navController: NavController,
    postId: String
) {

    //Log.d("postTitle", postId)
    val posts = viewModel.stateForum.value.posts
    //Log.d("posts", posts.toString())
    val post = posts.find { it.postId == postId }
    //Log.d("post", post.toString())

    val focusManager = LocalFocusManager.current

    var comment by remember { mutableStateOf("") }

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
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color.White
            ) {
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("comment") },
                    maxLines = 2,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(

                        onSend = {
                            if (comment != "") {
                                viewModel.commentPost(postId, comment)
                            }
                            //close keyboard
                            focusManager.clearFocus()
                        },
                    ),
                    shape = RoundedCornerShape(25.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )
            }
        }
    ) { innerPadding ->
        Column {
            Surface() {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp)
                ) {
                    item {
                        if (post != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = post.username!!,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light
                                )

                                Text(
                                    text = post.timestamp,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light
                                )

                                Text(
                                    text = post.postTag,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light
                                )
                            }
                            Text(
                                text = post.title!!,
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp
                            )
                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = post.content,
                                fontSize = 16.sp,
                                lineHeight = 22.sp

                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Row() {
                                IconButton(onClick = { /*TODO*/ }) {
                                    Row()
                                    {
                                        Icon(
                                            Icons.Outlined.FavoriteBorder,
                                            contentDescription = "like button",
                                            tint = Color.Black.copy(alpha = ContentAlpha.medium),

                                            )
                                        Text(post.postLikes.toString())
                                    }

                                }
                                Spacer(modifier = Modifier.width(5.dp))

                                Row() {
                                    Icon(
                                        Icons.AutoMirrored.Outlined.Comment,
                                        contentDescription = "comment button",
                                        tint = Color.Black.copy(alpha = ContentAlpha.medium),

                                        )
                                    Text(post.numComments.toString())
                                }
                            }

                            Divider(modifier = Modifier.fillMaxWidth())


                        } else {
                            Text("there was an error fetching the events, please retry")
                        }
                    }
                    item {
                        if(post != null) {
                            for(comment in post.postComments) {
                                Text(
                                    text = comment.username,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Light

                                )

                                Text(
                                    text = comment.commentContent,
                                    fontSize = 16.sp,
                                    lineHeight = 22.sp

                                )

                                //if current logged in user is the author of the comment, show delete comment option
                                if(viewModel.currentUser!!.displayName == comment.username) {
                                    ClickableText(
                                        text = AnnotatedString("delete comment"),
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Light
                                        ),
                                        onClick = {
                                            viewModel.deleteComment(postId, comment.commentId)
                                        }
                                    )
                                }

                                Divider(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 5.dp))
                            }
                        }
                    }
                }
            }
        }

    }
}



