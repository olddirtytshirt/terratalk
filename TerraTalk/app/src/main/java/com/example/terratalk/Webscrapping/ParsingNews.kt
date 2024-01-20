package com.example.terratalk.Webscrapping

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


suspend fun parseIrishTimes(): List<Pair<String, String>> {
    val url = "https://www.irishtimes.com/environment/climate-crisis/" // Replace with the actual URL
    val doc: Document = withContext(Dispatchers.IO) {
        Jsoup.connect(url).get()
    }
    val articleElements: List<Element> = doc.select("h2.primary-font__PrimaryFontStyles-ybxuz7-0 a")

    val newsItems = mutableListOf<Pair<String, String>>()

    for (article in articleElements) {
        val title = article.text() ?: "No title found"
        val link = article.attr("href") ?: "No link found"

        newsItems.add(title to link)
    }
    return newsItems
}



suspend fun parseIndependent(): List<Pair<String, String>> {
    val url = "https://www.independent.ie/tag/environment"
    val doc: Document = withContext(Dispatchers.IO) {
        Jsoup.connect(url).get()
    }
    val articleElements: List<Element> = doc.select("div h5[data-testid=title] span")
    val newsItems = mutableListOf<Pair<String, String>>()


    for (article in articleElements) {
        val title = article.text() ?: "No title found"
        val link = ""

        newsItems.add(title to link)
    }
    return newsItems
}




suspend fun parseTheJournal(): List<Pair<String, String>> {
    val url = "https://www.thejournal.ie/climate-change/news/"
    val doc: Document = withContext(Dispatchers.IO) {
        Jsoup.connect(url).get()
    }
    val articleElements: List<Element> = doc.select("div.article-redesign")

    val newsItems = mutableListOf<Pair<String, String>>()

    for (article in articleElements) {
        val titleElement = article.selectFirst("div.title-redesign")
        val title = titleElement?.text() ?: "No title found"

        val linkElement = article.selectFirst("a.link-overlay-redesign")
        val link = linkElement?.attr("href") ?: "No link found"


        newsItems.add(title to link)
    }
    return newsItems
}
