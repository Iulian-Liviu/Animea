package dev.julian.animea.ui.views

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.julian.animea.data.WaifuImageResponse
import dev.julian.animea.network.WaifuServiceInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WaifusScreenViewModel() : ViewModel() {
    val repository: RandomImageRepository = RandomImageRepository()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData("")
    val ErrorMessage: LiveData<String> = _errorMessage

    private val _images = MutableLiveData<List<WaifuImageResponse.Image>>(emptyList())
    val Images: LiveData<List<WaifuImageResponse.Image>> = _images


    fun loadImages() {
        if (_isLoading.value != true) {
            viewModelScope.launch {
                repository.getRandomImages(
                    { _isLoading.postValue(true) },
                    { error -> _isLoading.postValue(false); _errorMessage.postValue(error) },
                    { _isLoading.postValue(false) })
                    .collect { response ->
                        withContext(Dispatchers.Main) {
                            val currentImages = _images.value ?: emptyList()
                            _images.postValue(currentImages + response.images)
                            delay(1000) // Add a delay of 1000 milliseconds here
                            _isLoading.postValue(false)
                        }
                    }

            }
        }
    }
}

