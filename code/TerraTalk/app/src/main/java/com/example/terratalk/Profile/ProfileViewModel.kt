package com.example.terratalk.Profile


import androidx.lifecycle.ViewModel
import com.example.terratalk.Database.updateStatus
import com.example.terratalk.Database.userRef
import com.example.terratalk.LoginRegister.auth
import com.example.terratalk.UserManager.currentUser
import com.example.terratalk.UserManager.stateUser
import com.example.terratalk.models.StatusTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProfileViewModel : ViewModel() {
    
    fun signOut() {
        if (currentUser != null) {
            //update the status to OFFLINE before signing out
            GlobalScope.launch(Dispatchers.IO) {
                updateStatus(StatusTag.OFFLINE)
                //after the status is updated, sign out the user
                withContext(Dispatchers.Main) {
                    auth.signOut()
                }
            }
        }
    }
    
}

