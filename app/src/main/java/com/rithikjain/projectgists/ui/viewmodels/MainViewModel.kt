package com.rithikjain.projectgists.ui.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rithikjain.projectgists.api.WebClient
import com.rithikjain.projectgists.models.ApiResult
import com.rithikjain.projectgists.models.GistResponse
import com.rithikjain.projectgists.models.Loading
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val webClient: WebClient) : ViewModel() {
  var gists: ApiResult<List<GistResponse>> by mutableStateOf(Loading())
    private set

  fun getGists(token: String) = viewModelScope.launch(Dispatchers.IO) {
    gists = Loading()
    gists = webClient.getGists(token)
  }
}