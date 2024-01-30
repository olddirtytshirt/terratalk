package com.example.terratalk.Webscrapping

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.sp
import com.example.terratalk.R
import kotlin.reflect.KClass

class NewsViewModel : ViewModel() {
    private val _newsItems = MutableLiveData<List<Pair<String, String>>>()

    val newsItems: LiveData<List<Pair<String, String>>>
        get() = _newsItems

    fun setNewsItems(news: List<Pair<String, String>>) {
        _newsItems.value = news
    }
}
@Composable
fun NewsPage(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier, // Provide a default value for modifier
) {
    val newsItemsState = viewModel.newsItems.observeAsState(initial = emptyList())
    val newsItems = newsItemsState.value
    Scaffold(
        topBar = {
            NewsPageBar()
        }

    ) { innerPadding ->
        Column (
            modifier = modifier
                .padding(innerPadding)
        ){
            LazyColumn (
                modifier = Modifier
                    .padding(16.dp)
            ){
                //display all news articles using lazy column
                items(newsItems) { item ->
                    NewsItem(
                        title = item.first,
                        link = item.second,
                        onClick = {}
                    )
                    Divider(thickness = 0.5.dp)
                }
            }
        }
    }
}

@Composable
fun NavBar(
    //*** TO DO ***//
) {


}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsPageBar(
) {
    TopAppBar(
        title = { Text(
            text = "//news",
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp
        ) }

    )
}

@Composable
fun NewsItem(
    title: String,
    link: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth() // Set width to maximum
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Title takes up 75% of the row
            Text(
                text = title,
                modifier = Modifier
                    .weight(0.75f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Image takes up 25% of the row
            // STOCK IMAGE;
            // TO DO - CHANGE IMAGE
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier
                    .weight(0.25f)
            )
        }

        Row(
            modifier = Modifier
                .padding(start = 10.dp, bottom = 6.dp)
        ) {
            Text(text = "test1", fontSize = 10.sp)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "test2", fontSize = 10.sp)
            Spacer(modifier = Modifier.padding(5.dp))
            Text(text = "test3", fontSize = 10.sp)
        }
    }
}