package com.example.terratalk.Profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.wear.compose.material.LocalContentAlpha
import com.example.terratalk.Database
import com.example.terratalk.Database.getLikedPostsForCurrentUser
import com.example.terratalk.Events.EventViewModel
import com.example.terratalk.Forum.ForumViewModel
import com.example.terratalk.R
import com.example.terratalk.Screen
import com.example.terratalk.UserManager.currentUser
import com.example.terratalk.ui.BottomNavigation
import com.example.terratalk.ui.PageBar

@Composable
fun ProfilePage(
    viewModel: ProfileViewModel,
    eventViewModel : EventViewModel,
    navController: NavController
) {

    Scaffold(
        topBar = {
            PageBar("//profile")
        },
        bottomBar = {
            BottomNavigation(navController)
        }
        //content
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 5.dp)
        ) {
            if(currentUser != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_1),
                        contentDescription = "TerraTalk Logo",
                        modifier = Modifier
                            .weight(0.2f)
                    )
                    Column {
                        Text(
                            text = "hi, " + currentUser!!.displayName,
                            fontSize =  18.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "you have been a user since " + viewModel.stateUser.value.dateCreated,
                            style = MaterialTheme.typography.bodyMedium
                        )

                    }
                }

            } else {
                Text(
                    text = "hi",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            ExpandableSavedEvents(eventViewModel, navController)

            Spacer(modifier = Modifier.height(20.dp))

            ExpandableLikedPosts()

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Logout,
                    contentDescription = "sign out icon",
                )
                Spacer(modifier = Modifier.width(8.dp))

                //set clickable text color as per system light/dark mode settings
                val color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                ClickableText(
                    text = AnnotatedString("sign out"),
                    style = TextStyle(
                        fontSize =  18.sp,
                        fontWeight = FontWeight.Medium,
                        color = color

                    ),
                    onClick = {
                        viewModel.signOut()
                        navController.navigate(Screen.SignInPreview.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ExpandableSavedEvents(
    eventViewModel: EventViewModel,
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }

    val eventItems by eventViewModel.eventItems.observeAsState(initial = emptyList())
    //Log.d("eventItems", eventItems.toString())

    var savedEventsList by remember { mutableStateOf<List<String>?>(null) }
    //Log.d("savedEventList", savedEventsList.toString())

    //add a flag that indicates for how long savedEvents are fetched from the database
    val isLoadingSavedEvents = remember { mutableStateOf(true) }

    val handler = LocalUriHandler.current

    //initialize savedEventsList with data from getEventsSavedForCurrentUser
    //effect is recomposed everytime ProfilePage is called.
    LaunchedEffect(navController.currentBackStackEntryAsState().value) {
        Database.getEventsSavedForCurrentUser { eventsList ->
            savedEventsList = eventsList
            isLoadingSavedEvents.value = false
        }
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Outlined.Bookmark,
                contentDescription = "bookmarked events",
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "saved events",
                fontSize =  18.sp,
                fontWeight = FontWeight.Medium
            )

            if(expanded) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowUp,
                    contentDescription = "dropdown arrow up",
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "dropdown arrow down",
                )
            }

        }

        if (expanded) {
            LazyColumn {
                items(eventItems) { event ->
                    val isSavedEvent = savedEventsList?.contains(event.title) ?: false

                    if (isSavedEvent) {
                        ClickableText(
                            text = AnnotatedString(event.title),
                            onClick = { handler.openUri(event.link) },
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableLikedPosts(
) {
    var expanded by remember { mutableStateOf(false) }

    var likedPosts by remember { mutableStateOf(emptyList<String>()) }
    /*

    Log.d("posts", posts.toString())

     */

    //cll the function with a callback
    getLikedPostsForCurrentUser { eventsList ->
        if(eventsList != null) {
            likedPosts = eventsList
        }
    }

    Log.d("likedPosts", likedPosts.toString())

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { expanded = !expanded }
        ) {
            Icon(
                imageVector = Icons.Outlined.Favorite,
                contentDescription = "liked posts",
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "liked posts",
                fontSize =  18.sp,
                fontWeight = FontWeight.Medium
            )

            if(expanded) {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowUp,
                    contentDescription = "dropdown arrow up",
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = "dropdown arrow down",
                )
            }

        }

        if (expanded) {
            if(!likedPosts.equals(emptyList<String>())) {
                LazyColumn {
                    items(likedPosts) {post ->
                        //val isLikedPost = likedPosts.contains(post.title)

                        Text(
                            text = AnnotatedString(post),
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium

                        )
                    }

                }
            } else {
                Text(
                    "quite empty here",
                    style = MaterialTheme.typography.bodyMedium

                )
            }
        }

    }
}

