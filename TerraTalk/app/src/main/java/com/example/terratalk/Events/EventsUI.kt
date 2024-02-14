package com.example.terratalk.Events

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.terratalk.ui.BottomNavigation
import com.example.terratalk.ui.PageBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun EventsPage(
    viewModel: EventViewModel,
    navController: NavController,
) {
    val eventItemsState = viewModel.eventItems.observeAsState()
    val eventItems = eventItemsState.value ?: emptyList()

    var savedEventsList by remember { mutableStateOf<List<String>?>(null) }

    // Initialize savedEventsList with data from getEventsSavedForCurrentUser
    LaunchedEffect(Unit) {
        viewModel.getEventsSavedForCurrentUser { eventsList ->
            savedEventsList = eventsList
        }
    }
    //Log.d("SAVED EVENTS LIST", savedEventsList.toString())
    Scaffold(
        topBar = {
            PageBar("//events")
        },
        bottomBar = {
            BottomNavigation(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(horizontal =  20.dp)
            ) {
                items(eventItems) { event ->
                    //if savedEventsList? is empty, it will crash the app
                    //as you are trying to access a null list
                    EventsItem(
                        title = event.title,
                        location = event.location,
                        date = event.date,
                        imageURL = event.imageUrl,
                        link = event.link,
                        isSaved = savedEventsList!!.contains(event.title),
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun EventsItem(
    title: String,
    location: String,
    date: String,
    imageURL: String,
    link: String,
    isSaved: Boolean,
    viewModel: EventViewModel
) {
    //Log.d("event saved? ", title + "      " + isSaved.toString() ?: "null")
    val handler = LocalUriHandler.current
    val contrast =  1f //normal contrast
    val brightness = -80f //decrease brightness
    val colorMatrix = floatArrayOf(
        contrast,  0f,  0f,  0f, brightness,
        0f, contrast,  0f,  0f, brightness,
        0f,  0f, contrast,  0f, brightness,
        0f,  0f,  0f,  1f,  0f
    )
    //surface makes the card clickable
    //when clicked, send you to the event link in browser
    Surface(
        onClick = { handler.openUri(link)},
    ) {
        //box to align content not from top down, but bottom up
        Box(
            modifier = Modifier
                .fillMaxSize(),
            Alignment.BottomStart
        )  {
            //Log.d("EVENT USER", user.toString())
            Image(
                painter = rememberAsyncImagePainter(imageURL),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(200.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .blur(
                        radiusX = 5.dp,
                        radiusY = 5.dp,
                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                    ),
                 colorFilter = androidx.compose.ui.graphics.ColorFilter.colorMatrix(ColorMatrix(colorMatrix))
            )
            /*
             *     The Box is split into 2 sub-boxes
             *
             *     ---------------------------------  <- BOX WIDTH, ROW()
             *               |
             *     33% width |   67% width
             *     sub-box 1 |   sub-box 2
             */
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                ,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //the 33% box width
                Box(
                    modifier = Modifier
                        .weight(0.33f)
                        .fillMaxHeight()
                        .padding(top = 9.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = location,
                            color = Color.White,
                            lineHeight = 12.sp,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Light,
                        )
                    }
                }

                //the 67% box width
                Box(
                    modifier = Modifier
                        .weight(0.67f)
                        .fillMaxHeight()
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 22.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Medium,
                        )

                        Text(
                            text = date,
                            color = Color.White,
                            lineHeight = 12.sp,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light,
                        )
                    }
                }
            }

            var savedState by remember { mutableStateOf(isSaved) }

            //func to handle saving the event
            val onSaveEvent: () -> Unit = {
                viewModel.saveEvent(title)
                savedState = true
            }

            //func to handle removing the event
            val onRemoveEvent: () -> Unit = {
                viewModel.removeSaved(title)
                savedState = false
            }

            BookmarkButton(
                modifier = Modifier.align(Alignment.TopEnd).padding(5.dp),
                isSaved = savedState,
                onSaveEvent = onSaveEvent,
                onRemoveEvent = onRemoveEvent
            )

        }
    }
    //space articles apart
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun BookmarkButton(
    modifier: Modifier = Modifier,
    isSaved: Boolean,
    onSaveEvent: () -> Unit,
    onRemoveEvent: () -> Unit
) {
    IconButton(
        onClick = {
            if (isSaved) {
                onRemoveEvent()
            } else {
                onSaveEvent()
            }
        },
        modifier = modifier
    ) {
        val icon = if (isSaved) {
            Icons.Outlined.Bookmark
        } else {
            Icons.Outlined.BookmarkBorder
        }

        Icon(
            imageVector = icon,
            contentDescription = if (isSaved) "delete saved event" else "save event",
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}
