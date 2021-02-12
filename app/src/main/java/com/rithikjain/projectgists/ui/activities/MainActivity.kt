package com.rithikjain.projectgists.ui.activities

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigate
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.rithikjain.projectgists.ui.themes.EzGistsTheme
import com.rithikjain.projectgists.ui.viewmodels.MainViewModel
import com.rithikjain.projectgists.ui.widgets.HomePage
import com.rithikjain.projectgists.ui.widgets.LoginPage
import dagger.hilt.android.AndroidEntryPoint

val LocalMainActivity = compositionLocalOf<Activity> { error("Needs to be provided") }
val LocalDataStore = compositionLocalOf<DataStore<Preferences>> { error("Needs to be provided") }
val LocalViewModel = compositionLocalOf<MainViewModel> { error("Needs to be provided") }

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val viewModel by viewModels<MainViewModel>()

    setContent {
      Providers(
        LocalMainActivity provides this,
        LocalDataStore provides LocalContext.current.createDataStore(name = "prefs"),
        LocalViewModel provides viewModel
      ) {
        EzGistsTheme {
          EzGistsApp()
        }
      }
    }
  }
}

@Composable
fun EzGistsApp() {
  val navController = rememberNavController()
  val startDestination = if (FirebaseAuth.getInstance().currentUser != null) "home" else "login"
  NavHost(navController = navController, startDestination = startDestination) {
    composable("login") { LoginPage(onLoggedIn = { navController.navigate("home") }) }
    composable("home") { HomePage() }
  }
}

@Preview
@Composable
fun PreviewApp() {
  EzGistsTheme {
    HomePage()
  }
}