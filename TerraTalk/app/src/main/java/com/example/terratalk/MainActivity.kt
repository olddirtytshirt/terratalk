package com.example.terratalk

import com.example.terratalk.Webscrapping.parseIrishTimes
import com.example.terratalk.Webscrapping.parseIndependent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.activity.compose.setContent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.terratalk.ui.theme.TerraTalkTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import com.example.terratalk.Webscrapping.parseTheJournal
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch




class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainScope().launch {
            val newsItems1 = parseIrishTimes()
            val newsItems2 = parseTheJournal()
            val newsItems3 = parseIndependent()
            viewModel.setNewsItems(newsItems1)
            viewModel.setNewsItems(newsItems2)
            viewModel.setNewsItems(newsItems3)
        }

        setContent {
            TerraTalkTheme {
                NewsList(viewModel)
            }
        }
    }
}

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

    // Retrieve the value from the state
    val newsItems = newsItemsState.value

    // LazyColumn and item list
    LazyColumn {
        items(newsItems) { newsItem ->
            NewsItem(title = newsItem.first, link = newsItem.second)
        }
    }
}

@Composable
fun NewsItem(title: String, link: String) {
    // Display news item content
    Text(text = title)
}


