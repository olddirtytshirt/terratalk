package com.example.terratalk.LoginRegister

import android.content.Context
import androidx.navigation.NavController
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.terratalk.Screen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class AuthManagerTest {

    //create a mock Firebase Authenticator
    @Mock
    private lateinit var mockAuth: FirebaseAuth

    //create a mock User
    @Mock
    private lateinit var mockUser: FirebaseUser

    //create a mock NavController
    @Mock
    private lateinit var mockNavController: NavController

    //create a context variable
    private lateinit var context: Context

    //a set up function that is called before the @Test function
    @Suppress("DEPRECATION")
    @Before
    fun setUp() {
        //initialise mocks
        initMocks(this)
        //and app context
        context = ApplicationProvider.getApplicationContext<Context>()
    }

    //test function that goes through the login process, but fails and makes sure makeToast is called
    @Test
    fun loginUser_EmailNotVerified_ShowsToast() {
        `when`(mockAuth.signInWithEmailAndPassword(any(), any())).thenAnswer {
            val listener = it.arguments[1] as FirebaseAuth.AuthStateListener
            listener.onAuthStateChanged(mockAuth)
        }
        `when`(mockUser.isEmailVerified).thenReturn(false)
        `when`(mockAuth.currentUser).thenReturn(mockUser)

        loginUser("test@example.com", "password123", context, mockNavController)

        //verify that Toast.makeText is called with the appropriate message
        verify(mockNavController, never()).navigate(Screen.NewsPage.route)
    }
}