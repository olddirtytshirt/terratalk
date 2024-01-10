package com.example.terratalk.Webscrapping

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

suspend fun parseIrishTimes(): List<Pair<String, String>> {
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

suspend fun parseIndependent(): List<Pair<String, String>> {
    val url = "https://www.independent.ie/tag/environment"
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

suspend fun parseTheJournal(): List<Pair<String, String>> {
    val url = "https://www.thejournal.ie/climate-change/news/"
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
