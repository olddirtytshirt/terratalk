package com.example.terratalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.activity.compose.setContent
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.compose.material3.Text
import kotlinx.coroutines.withContext
import androidx.compose.runtime.Composable
import com.example.terratalk.ui.theme.TerraTalkTheme
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch




class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MainScope().launch {
            val newsItems = parseIrishTimes()
            viewModel.setNewsItems(newsItems)
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


private suspend fun parseIrishTimes(): List<Pair<String, String>> {
    val url = "https://www.irishtimes.com/environment/climate-crisis/"
    val doc: Document = withContext(Dispatchers.IO) {
        Jsoup.connect(url).get()
    }
    val articleElements: List<Element> = doc.select("article.custom-flex-promo")
    val newsItems = mutableListOf<Pair<String, String>>()

    for (article in articleElements) {
        val titleElement =
            article.selectFirst("h2.primary-font__PrimaryFontStyles-ybxuz7-0.jGeesm.headline_font_md_sm.font_bold.text_decoration_none.color_custom_black_1 a")
        val title = titleElement?.text() ?: "No title found"
        val link = titleElement?.attr("href") ?: "No link found"

        newsItems.add(title to link)
    }
    return newsItems
}