package com.example.terratalk.LoginRegister

import com.example.terratalk.ui.AutoResizedText
import com.example.terratalk.ui.InputField
import com.example.terratalk.ui.PasswordField

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.terratalk.R
import com.example.terratalk.Screen
import java.util.regex.Matcher
import java.util.regex.Pattern


//CODE REFERENCES:
//validation tutorial that was followed
//https://medium.com/@jecky999/designing-a-login-screen-with-validation-using-jetpack-compose-7c7483c63c0c

// password visibility icon implementation
// https://alitalhacoban.medium.com/show-hide-password-jetpack-compose-d0c4abac568f#:~:text=Hide%20the%20Text%20in%20TextField&text=visualTransformation%20%3D%20PasswordVisualTransformation()%2C,want%20for%20the%20password%20field.

@Composable
fun SignInPreview(navController: NavController) {
    val mockContext = LocalContext.current

    SignInScreen(mockContext, navController)
}


@Composable
fun SignInScreen(
    context: Context,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, top = 10.dp, end = 30.dp, bottom = 20.dp)
    ) {
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        //state variable that controls visibility when typing password or not
        var showPassword by remember { mutableStateOf(value = false) }

        //login validation
        var isValid by remember { mutableStateOf(true)}

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

            AutoResizedText(
                text = "//welcome to terratalk",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "the next social media app for environmentalists",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

        }

        Spacer(modifier = Modifier.height(50.dp))

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


        PasswordField(
            label = "password",
            value = password,
            showPassword,
            onValueChanged = { password = it },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                //when press the `DONE` button on keyboard,
                //loginUser is called
                //
                onDone = {
                    isValid = isValidEmail(email) && isValidPassword(password)
                    if(isValid) {
                        loginUser(email, password, context, navController)
                    }
                    //close keyboard
                    focusManager.clearFocus()
                },
            ),

            // password visibility icon implementation
            // https://alitalhacoban.medium.com/show-hide-password-jetpack-compose-d0c4abac568f#:~:text=Hide%20the%20Text%20in%20TextField&text=visualTransformation%20%3D%20PasswordVisualTransformation()%2C,want%20for%20the%20password%20field.
            trailingIcon = {
                if (showPassword) {
                    IconButton(onClick = { showPassword = false }) {
                        Icon(
                            imageVector = Icons.Filled.Visibility,
                            contentDescription = "hide_password"
                        )
                    }
                } else {
                    IconButton(
                        onClick = { showPassword = true }) {
                        Icon(
                            imageVector = Icons.Filled.VisibilityOff,
                            contentDescription = "hide_password"
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            //when button clicked, registerUser
            onClick = {
                isValid = isValidEmail(email) && isValidPassword(password)
                if(isValid) {
                    loginUser(email, password, context, navController)
                }
            },

            ) {
            Text(
                text = "login",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        if(!isValid) {
            Text(
                text = "Invalid email or password",
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

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

            Button(
                onClick = {
                    navController.navigate(Screen.RegisterPreview.route)
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(
                    text = "sign up",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

    }
}

fun isValidEmail(email: String): Boolean {
    val EMAIL_PATTERN = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
    val p: Pattern = Pattern.compile(EMAIL_PATTERN)
    val matcher: Matcher = p.matcher(email)
    return matcher.matches()
}


// Here is validating password
fun isValidPassword(pass: String?): Boolean {
    return if (pass != null && pass.length > 5) {
        true
    } else false
}

