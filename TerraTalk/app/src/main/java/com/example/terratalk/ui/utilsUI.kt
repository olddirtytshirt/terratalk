package com.example.terratalk.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isUnspecified
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.terratalk.BottomNavItem
import androidx.compose.material3.Text as Text


//file containing multiple functions/composables used in multiple screens
//to avoid repeating code in each screen

/*
CODE REFERENCES:

AutoResizedText() implementation from video link below
https://www.youtube.com/watch?v=ntlyrFw0F9U

 */


//function to auto resize font size based on screen width amount
@Composable
fun AutoResizedText(
    text: String,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    modifier: Modifier = Modifier,
    color: Color = style.color,
) {
    var shouldDraw by remember {
        mutableStateOf(false)
    }

    var resizedTextStyle by remember { mutableStateOf(style) }
    var defaultFontSize = MaterialTheme.typography.bodyLarge.fontSize

    Text(
        text = text,
        color = color,
        modifier = modifier.drawWithContent {
            if(shouldDraw) {
                drawContent()
            }
        },
        softWrap = false,
        fontWeight = FontWeight.Bold,
        style = resizedTextStyle,
        onTextLayout = {result ->
            if(result.didOverflowWidth) {
                if(style.fontSize.isUnspecified) {
                    resizedTextStyle = resizedTextStyle.copy(
                        fontSize = defaultFontSize
                    )
                }
                resizedTextStyle = resizedTextStyle.copy(
                    fontSize = resizedTextStyle.fontSize * 0.95
                )
            } else {
                shouldDraw = true

            }
        }
    )
}


//function to define a standard app Input Field
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
        label = { Text(label) },
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions ?: KeyboardActions.Default,
        shape = RoundedCornerShape(30.dp),
        singleLine = true,
    )
}

//function to define the top bar in app screens
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageBar(
    title: String,
) {
    TopAppBar(
        title = { Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 25.sp,
        ) }

    )
}

//function for password field
//we can toggle visibility on or off
@Composable
fun PasswordField(
    label: String,
    value: String,
    showPassword: Boolean,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
        .fillMaxWidth()
) {

    OutlinedTextField(
        label = { Text(label) },
        value = value,
        onValueChange = onValueChanged,
        modifier = modifier,
        visualTransformation = if (showPassword) {

            VisualTransformation.None

        } else {

            PasswordVisualTransformation()

        },

        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions ?: KeyboardActions.Default,
        trailingIcon = trailingIcon,
        shape = RoundedCornerShape(30.dp),
        singleLine = true,
        //supportingText = { Text(text = "password needs to be more than 6 characters long")}
    )
}

@Composable
fun BottomNavigation(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    var selectedItem by remember { mutableStateOf(0) }

    val items = listOf(
        BottomNavItem.NewsPage,
        BottomNavItem.EventsPage,
        BottomNavItem.MapsPage,
        BottomNavItem.ForumPage,
        BottomNavItem.ProfilePage
    )

    // Determine the index of the current route
    val currentIndex = items.indexOfFirst { it.route == currentRoute }
    // Update the selected item state based on the current route
    if (currentIndex != -1) {
        selectedItem = currentIndex
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = selectedItem == index,
                onClick = {
                    selectedItem = index
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors()
            )
        }
    }
}

