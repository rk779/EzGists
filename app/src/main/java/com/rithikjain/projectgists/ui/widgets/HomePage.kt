package com.rithikjain.projectgists.ui.widgets

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSizeIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.FirebaseAuth
import com.rithikjain.projectgists.models.Error
import com.rithikjain.projectgists.models.GistResponse
import com.rithikjain.projectgists.models.Loading
import com.rithikjain.projectgists.models.Success
import com.rithikjain.projectgists.ui.activities.LocalDataStore
import com.rithikjain.projectgists.ui.activities.LocalViewModel
import com.rithikjain.projectgists.ui.themes.EzGistsTheme
import com.rithikjain.projectgists.ui.themes.vividPink
import com.rithikjain.projectgists.utils.Constants
import dev.chrisbanes.accompanist.coil.CoilImage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

@Composable
fun HomePage() {
  val vm = LocalViewModel.current
  val datastore = LocalDataStore.current
  val user = FirebaseAuth.getInstance().currentUser

  LaunchedEffect(null) {
    datastore.data.map { it[Constants.ACCESS_TOKEN].toString() }.collect { token ->
      vm.getGists("token $token")
    }

  }

  Scaffold(
    topBar = {
      TopAppBar(
        {
          Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .padding(end = 8.dp)
          ) {
            Text(text = "My Gists", color = vividPink)
            if (user != null) {
              CoilImage(
                data = user.photoUrl.toString(),
                fadeIn = true,
                requestBuilder = { transformations(CircleCropTransformation()) },
                loading = {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                },
                error = {
                  Image(imageVector = Icons.Outlined.AccountCircle, contentDescription = null)
                },
                modifier = Modifier.preferredSizeIn(maxHeight = 64.dp).padding(8.dp),
                contentDescription = null
              )
            }
          }
        },
        backgroundColor = MaterialTheme.colors.background,
      )
    },
    bodyContent = {
      when (vm.gists) {
        is Success -> {
          LazyColumn {
            items((vm.gists as Success<List<GistResponse>>).data) { item ->
              GistItem(
                title = item.files.entries.first().value.filename,
                body = item.description
              )
            }
          }
        }

        is Loading -> {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
              modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
              color = vividPink
            )
          }
        }

        is Error -> {
          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
              modifier = Modifier.wrapContentWidth(Alignment.CenterHorizontally),
              text = "Something went wrong!"
            )
          }
        }
      }
    },
    floatingActionButton = {
      FloatingActionButton(onClick = {}, backgroundColor = MaterialTheme.colors.primary) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = null)
      }
    }
  )
}

@Preview
@Composable
fun PreviewApp() {
  EzGistsTheme {
    HomePage()
  }
}