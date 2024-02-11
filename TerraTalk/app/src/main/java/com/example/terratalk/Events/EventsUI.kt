package com.example.terratalk.Events


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.terratalk.ui.BottomNavigation
import com.example.terratalk.ui.PageBar




class EventViewModel : ViewModel() {
    private val _eventItems = MutableLiveData<List<Triple<String, String, String>>>()
    val eventItems: LiveData<List<Triple<String, String, String>>>
        get() = _eventItems

    fun setEventItems(events: List<Triple<String, String, String>>) {
        _eventItems.value = events
    }

}


@Composable
fun EventsPage(
    viewModel: EventViewModel,
    navController: NavController
) {

    val eventItemsState = viewModel.eventItems.observeAsState(initial = emptyList())
    val eventItems = eventItemsState.value

    //Screen Scaffold
    //Structure:
    //topBar
    //content
    //bottomBar
    Scaffold(
        topBar = {
            PageBar("//news")
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
                //display all news articles using lazy column
                //more efficient, optimised
                items(eventItems) { item ->
                    EventsItem(
                        title = item.first,
                        link = item.second,
                        imageUrl = item.third,
                    )
                    Divider(thickness = 1.dp)
                }
            }
        }
    }
}



//function to display individual news articles
@Composable
fun EventsItem(
    title: String,
    link: String,
    imageUrl: String,
) {
    val handler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //title takes up 75% of the row
            ClickableText(
                text = AnnotatedString(title),
                modifier = Modifier
                    .weight(0.75f),
                onClick = { handler.openUri(link)}
            )

            Spacer(modifier = Modifier.width(8.dp))

            //image takes up 25% of the row
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .weight(0.25f)
                    .size(90.dp)
                    .clip(RoundedCornerShape(15.dp))
            )

        }
    }
}

