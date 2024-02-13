package com.example.terratalk.Events

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


@Composable
fun EventsPage(
    viewModel: EventViewModel,
    navController: NavController
) {
    val eventItemsState = viewModel.eventItems.observeAsState()
    val eventItems = eventItemsState.value ?: emptyList()

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
                    EventsItem(
                        title = event.title,
                        location = event.location,
                        date = event.date,
                        imageURL = event.imageUrl,
                        link = event.link
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
    link: String
) {
    val handler = LocalUriHandler.current
    var isSaved by remember { mutableStateOf(false) }

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

                //
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
            IconButton(
                onClick = { /* Handle click */ },
                modifier = Modifier.align(Alignment.TopEnd).padding(5.dp)
            ) {
                if(!isSaved) {
                    Icon(
                        imageVector = Icons.Outlined.BookmarkBorder, // Replace with your desired icon
                        contentDescription = "save event",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Bookmark, // Replace with your desired icon
                        contentDescription = "event saved",
                        tint = Color.White,
                        modifier = Modifier
                            .size(30.dp)
                    )
                }
            }
        }
    }
    //space articles apart
    Spacer(modifier = Modifier.height(10.dp))
}
