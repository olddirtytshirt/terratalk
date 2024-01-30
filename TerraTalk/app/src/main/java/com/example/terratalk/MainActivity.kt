package com.example.terratalk

import com.example.terratalk.Webscrapping.parseIrishTimes
import com.example.terratalk.Webscrapping.parseIndependent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.terratalk.ui.theme.TerraTalkTheme
import androidx.lifecycle.ViewModelProvider
import com.example.terratalk.Webscrapping.NewsPage
import com.example.terratalk.Webscrapping.NewsViewModel
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
            viewModel.setNewsItems(newsItems1)

        }

        setContent {
            TerraTalkTheme {
                NewsPage(viewModel)
            }
        }
    }
}


