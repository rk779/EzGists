package com.rithikjain.projectgists.ui.widgets

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthCredential
import com.google.firebase.auth.OAuthProvider
import com.rithikjain.projectgists.ui.activities.LocalDataStore
import com.rithikjain.projectgists.ui.activities.LocalMainActivity
import com.rithikjain.projectgists.ui.themes.vividPink
import com.rithikjain.projectgists.utils.Constants
import kotlinx.coroutines.runBlocking

private lateinit var auth: FirebaseAuth

@Composable
fun LoginPage(onLoggedIn: () -> Unit) {
  auth = FirebaseAuth.getInstance()
  val activity = LocalMainActivity.current
  val dataStore = LocalDataStore.current

  Scaffold(
    topBar = {
      TopAppBar(
        { Text(text = "Login", color = vividPink) },
        backgroundColor = MaterialTheme.colors.background
      )
    },
    bodyContent = {
      Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
          modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
          onClick = handleLogin(onLoggedIn, activity, dataStore)
        ) {
          Text(text = "Login")
        }
      }
    }
  )
}

fun handleLogin(
  onLoggedIn: () -> Unit,
  activity: Activity,
  dataStore: DataStore<Preferences>
): () -> Unit = {
  val provider = OAuthProvider.newBuilder("github.com")
  provider.scopes = arrayListOf("read:user", "gist")

  val pendingResult = auth.pendingAuthResult
  if (pendingResult != null) {
    pendingResult.addOnSuccessListener {
      onLoggedIn()
    }.addOnFailureListener {
      Log.e("LOGIN", "Failure logging in", it)
    }
  } else {
    auth.startActivityForSignInWithProvider(activity, provider.build())
      .addOnSuccessListener {
        val credential = it.credential as OAuthCredential
        runBlocking {
          dataStore.edit { prefs ->
            Log.d("TOKEN", credential.accessToken)
            prefs[Constants.ACCESS_TOKEN] = credential.accessToken
          }
        }
        onLoggedIn()
      }.addOnFailureListener {
        Log.e("LOGIN", "Failure logging in", it)
      }
  }
}