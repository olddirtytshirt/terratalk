package com.example.terratalk.Webscrapping

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.sp





class NewsViewModel : ViewModel() {
    private val _newsItems = MutableLiveData<List<Pair<String, String>>>()

    val newsItems: LiveData<List<Pair<String, String>>>
        get() = _newsItems

    fun setNewsItems(news: List<Pair<String, String>>) {
        _newsItems.value = news
    }
}

@Composable
fun NewsList(viewModel: NewsViewModel) {
    val newsItemsState = viewModel.newsItems.observeAsState(initial = emptyList())
    val newsItems = newsItemsState.value

    Column{
        Text(
            text = "The Irish Times "
            // Add other styling properties as needed
        )

    }

    Spacer(modifier = Modifier.height(30.dp))



    LazyColumn {
        items(newsItems) { newsItem ->
            NewsItem(title = newsItem.first, link = newsItem.second)
        }
    }
}

@Composable
fun NewsItem(title: String, link: String) {
    Text(text = title)
}