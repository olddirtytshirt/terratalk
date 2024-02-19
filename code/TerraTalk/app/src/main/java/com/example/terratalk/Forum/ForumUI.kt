package com.example.terratalk.Forum


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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Comment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.ContentAlpha
import com.example.terratalk.Screen
import com.example.terratalk.models.Post
import com.example.terratalk.ui.BottomNavigation


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForumPage(
    viewModel: ForumViewModel,
    navController: NavController
){
    viewModel.fetchPosts()

    val posts = viewModel.stateForum.value.posts
    //Log.d("fetched posts", posts.toString())


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
                    .padding(horizontal =   20.dp)
            ) {
                items(posts) { post ->
                    PostItem(
                        viewModel,
                        post,
                        navController = navController,
                    )
                }
            }
        }
    }
}



@Composable
fun PostItem(
    viewModel: ForumViewModel,
    post: Post,
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = post.username!!,
                fontSize =  12.sp,
                fontWeight = FontWeight.Light
            )

            Text(
                text = post.timestamp,
                fontSize =  12.sp,
                fontWeight = FontWeight.Light
            )

            Text(
                text = post.postTag,
                fontSize =  12.sp,
                fontWeight = FontWeight.Light
            )
        }

        ClickableText(
            text = AnnotatedString(post.title!!),
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            ),
            onClick = {
                viewModel.setPostId(post.postId)
                navController.navigate(Screen.PostPage.route + "/${post.postId}")
            }

        )
        Row {
            LikeButton(post = post, viewModel = viewModel)

            Spacer(modifier = Modifier.width(5.dp))

            IconButton(onClick = {
                    viewModel.setPostId(post.postId)
                    navController.navigate(Screen.PostPage.route + "/${post.postId}")

            }) {
                Row {
                    Icon(
                        Icons.AutoMirrored.Outlined.Comment,
                        contentDescription = "comment button",
                        tint = Color.Black.copy(alpha = ContentAlpha.medium),

                    )
                    Text(post.numComments.toString())
                }
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        Divider(modifier = Modifier.height(1.dp))
    }

}


@Composable
fun LikeButton(post: Post, viewModel: ForumViewModel) {
    //check is post is already liked
    val isLiked by viewModel.isPostLiked(post.postId).observeAsState(initial = false)

    IconButton(onClick = { viewModel.toggleLikePost(post.postId) }) {
        Row {
            Icon(
                if (isLiked) Icons.Outlined.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "like button",
                tint = if (isLiked) Color.Red.copy(alpha = ContentAlpha.medium) else Color.Black.copy(alpha = ContentAlpha.medium)
            )
            //display the number of likes
            Text(post.postLikes.toString())
        }
    }
}