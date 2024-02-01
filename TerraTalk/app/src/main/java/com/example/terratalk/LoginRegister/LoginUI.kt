package com.example.terratalk.LoginRegister

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.terratalk.R

//validation tutorial that was followed
//https://medium.com/@jecky999/designing-a-login-screen-with-validation-using-jetpack-compose-7c7483c63c0c
@Composable
@Preview
fun SignInPreview() {
    val mockContext = LocalContext.current

    SignIn(context = mockContext)
}


@Composable
fun SignIn( context: Context

) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            // TO DO -- if app is in dark mode - black background!!!
            .background(Color.White)
            .padding(start = 30.dp, top = 10.dp, end = 30.dp, bottom = 20.dp)
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var username by remember { mutableStateOf("") }

        //login validation
        var isValid by remember { mutableStateOf(true)}

        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current


        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_1),
                contentDescription = "TerraTalk Logo",
                modifier = Modifier
                    .size(250.dp)
            )
            Text(
                text = "//welcome to terratalk",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "the next social media app for environmentalists",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

        }

        Spacer(modifier = Modifier.height(70.dp))

        Text(
            text = "already an user ?",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        InputField(
            label = "email",
            value = email,
            onValueChanged = { email = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
        )

        Spacer(modifier = Modifier.height(10.dp))

        InputField(
            label = "password",
            value = password,
            onValueChanged = { password = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                //when press the `DONE` button on keyboard,
                //loginUser is called
                //
                onDone = {
                    isValid = isValidCredentials(email, password)
                    if(isValid) {
                        loginUser(email, password)
                    }
                    //close keyboard
                    focusManager.clearFocus()
                }
            )
        )

        if(!isValid) {
            Text(
                text = "Invalid email or password",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(50.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "new around here ?",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )

            OutlinedButton(
                onClick = { /* TO DO */},
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "register",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

    }
}

//code from tutorial mentioned above
private fun isValidCredentials(email: String, password: String): Boolean {
    val emailPattern = Regex("[a-zA-Z0–9._-]+@[a-z]+\\.+[a-z]+")
    val passwordPattern = Regex("^(?=.*[0–9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
    return emailPattern.matches(email) && passwordPattern.matches(password)
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions? = null,
    modifier: Modifier = Modifier
        .fillMaxWidth()
) {

    OutlinedTextField(
        label = { Text(label)},
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions ?: KeyboardActions.Default,
        shape = RoundedCornerShape(30.dp)
    )
}

/*
        InputField(
            label = "username",
            value = username,
            onValueChanged = { username = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        InputField(
            label = "email",
            value = email,
            onValueChanged = { email = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        InputField(
            label = "password",
            value = password,
            onValueChanged = { password = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            )
        )

        TextButton(
            onClick = { registerUser(email, password, username, context) }
        ) {
            Text(
                text = "register",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            )
        }
 */