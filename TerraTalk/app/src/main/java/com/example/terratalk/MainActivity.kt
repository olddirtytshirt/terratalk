package com.example.terratalk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.terratalk.ui.theme.TerraTalkTheme
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TerraTalkTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            parseIrishTimes()
        }
    }

}

@Composable
fun Greeting(name: String) {
    Text(
            text = "Hello $name!",
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TerraTalkTheme {
        Greeting("Android")
    }
}

fun parseIrishTimes(){
    val url = "https://www.irishtimes.com/environment/climate-crisis/"
    val doc: Document = Jsoup.connect(url).get()
    val articleElements: List<Element> = doc.select("article.custom-flex-promo")
    for (article in articleElements) {
        val titleElement = article.selectFirst("h2.primary-font__PrimaryFontStyles-ybxuz7-0.jGeesm.headline_font_md_sm.font_bold.text_decoration_none.color_custom_black_1 a")
        val title = titleElement?.text() ?: "No title found"
        val link = titleElement?.attr("href") ?: "No link found"

        println("Title: $title")
        println("Link: $link")
        println("-----------------")}
}