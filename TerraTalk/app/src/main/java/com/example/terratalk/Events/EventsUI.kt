package com.example.terratalk.Events

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.terratalk.models.Events
import com.example.terratalk.ui.PageBar
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.terratalk.ui.BottomNavigation
import kotlinx.coroutines.launch


class EventViewModel : ViewModel() {
    private val eventbrite = Eventbrite()

    private val _eventsLiveData = MutableLiveData<List<Events>>()
    val eventsLiveData: LiveData<List<Events>> = _eventsLiveData

    fun searchEvents() {
        viewModelScope.launch {
            try {
                val events = eventbrite.searchEvents()
                _eventsLiveData.value = events
            } catch (e: Exception) {
                Log.e("EventViewModel", "Error fetching events", e)
            }
        }
    }
}



@Composable
fun EventsPage(
    viewModel: EventViewModel,
    navController: NavController
) {

    Scaffold(
        topBar = {
            PageBar("//events")
        },
        bottomBar = {
            BottomNavigation(navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {

            }
        }
    }
}


@Composable
fun EventItem(event: Events) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = event.title,
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 18.sp
        )
        Text(
            text = event.date,
            style = TextStyle(color = Gray),
            fontSize = 14.sp
        )

    }
}