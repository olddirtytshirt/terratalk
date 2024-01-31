package com.example.terratalk

import com.example.terratalk.Webscrapping.parseIrishTimes
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.terratalk.ui.theme.TerraTalkTheme
import androidx.lifecycle.ViewModelProvider
import com.example.terratalk.Webscrapping.NewsPage
import com.example.terratalk.Webscrapping.NewsViewModel
import com.example.terratalk.Webscrapping.parseTheJournal
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(NewsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
       val myAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)

        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            // User is already logged in
            Log.d("Authentication", "User is already logged in. Email: ${currentUser.email}")
            // Perform any additional actions if needed
        } else {
            Log.d("Authentication", "User is not logged in")
        }

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


@Preview
@Composable

fun SignIn(

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 28.dp, top = 80.dp, end = 30.dp, bottom = 50.dp)
    ) {
        var email by remember { mutableStateOf("")}
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "//welcome to terratalk",
                fontSize = 34.sp,
                fontWeight = FontWeight.ExtraBold,
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "the next social media app for environmentalists",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = "login",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        InputField(
            label = "email",
            value = email,
            onValueChanged = { email = it }
        )

        Spacer(modifier = Modifier.height(20.dp))



    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = onValueChanged,
        label = { Text("Label") }
    )
}