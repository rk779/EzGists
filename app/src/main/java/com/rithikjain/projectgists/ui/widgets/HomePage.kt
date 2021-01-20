package com.rithikjain.projectgists.ui.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.rithikjain.projectgists.models.Error
import com.rithikjain.projectgists.models.GistResponse
import com.rithikjain.projectgists.models.Loading
import com.rithikjain.projectgists.models.Success
import com.rithikjain.projectgists.ui.activities.AmbientDataStore
import com.rithikjain.projectgists.ui.activities.AmbientViewModel
import com.rithikjain.projectgists.ui.themes.EzGistsTheme
import com.rithikjain.projectgists.ui.themes.vividPink
import com.rithikjain.projectgists.utils.Constants
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map


@Composable
fun HomePage() {
  val vm = AmbientViewModel.current
  val datastore = AmbientDataStore.current
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
              NetworkImage(
                url = user.photoUrl.toString(),
                modifier = Modifier.preferredSizeIn(maxHeight = 64.dp).padding(8.dp)
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
        Icon(imageVector = Icons.Filled.Add)
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