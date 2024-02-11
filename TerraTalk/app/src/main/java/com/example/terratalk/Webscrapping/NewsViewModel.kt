package com.example.terratalk.Webscrapping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class NewsViewModel : ViewModel() {
    private val _newsItems = MutableLiveData<List<Triple<String, String, String>>>()
    val newsItems: LiveData<List<Triple<String, String, String>>>
        get() = _newsItems

    fun setNewsItems(news: List<Triple<String, String, String>>) {
        _newsItems.value = news
    }

    suspend fun parseIrishTimes(): List<Triple<String, String, String>> {
        //url where we take news from
        val url = "https://www.irishtimes.com/environment/climate-crisis/"

        val doc: Document = withContext(Dispatchers.IO) {
            Jsoup.connect(url).get()
        }

        val parentContainer = doc.select(".list-item")

        val newsItems = mutableListOf<Triple<String, String, String>>()

        for (container in parentContainer) {
            val titleElement = container.selectFirst(".results-list--headline-container h2")
            val title = titleElement?.text() ?: "No title found"
            val link = "https://www.irishtimes.com" + container.selectFirst(".results-list--headline-container a")?.attr("href") ?: "No link found"
            val pictureElement = container.selectFirst(".results-list--image-container picture")
            val imageUrl = pictureElement?.select("source")?.firstOrNull()?.attr("srcset") ?: "No image found"
            newsItems.add(Triple(title, link, imageUrl))
        }
        return newsItems
    }

    suspend fun parseIndependent(): List<Triple<String, String, String>> {
        val url = "https://www.independent.ie/tag/environment"
        val doc: Document = withContext(Dispatchers.IO) {
            Jsoup.connect(url).get()
        }
        val articleElements: List<Element> = doc.select("div h5[data-testid=title] span")
        val newsItems = mutableListOf<Triple<String, String, String>>()


        for (article in articleElements) {
            val title = article.text() ?: "No title found"
            val link = ""
            val imageElement = article.parent()?.select("img")?.first()
            val imageUrl = imageElement?.attr("src") ?: "No image found"

            newsItems.add(Triple(title, link, imageUrl))
        }
        return newsItems
    }




    suspend fun parseTheJournal(): List<Triple<String, String, String>> {
        val url = "https://www.thejournal.ie/climate-change/news/"
        val doc: Document = withContext(Dispatchers.IO) {
            Jsoup.connect(url).get()
        }
        val articleElements: List<Element> = doc.select("div.article-redesign")

        val newsItems = mutableListOf<Triple<String, String, String>>()

        for (article in articleElements) {
            val titleElement = article.selectFirst("div.title-redesign")
            val title = titleElement?.text() ?: "No title found"

            val linkElement = article.selectFirst("a.link-overlay-redesign")
            val link = linkElement?.attr("href") ?: "No link found"
            val imageElement = article.parent()?.select("img")?.first()
            val imageUrl = imageElement?.attr("src") ?: "No image found"

            newsItems.add(Triple(title, link, imageUrl))
        }
        return newsItems
    }
}
