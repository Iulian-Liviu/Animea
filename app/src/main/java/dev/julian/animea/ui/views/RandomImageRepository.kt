package dev.julian.animea.ui.views

import androidx.annotation.WorkerThread
import com.skydoves.sandwich.message
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import dev.julian.animea.data.WaifuImageResponse
import dev.julian.animea.network.WaifuServiceInstance
import dev.julian.animea.network.WaifuServiceInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class RandomImageRepository() {
    val apiClient = WaifuServiceInstance().getInstance()
    @WorkerThread
    fun getRandomImages(onStart: () -> Unit, onError: (String) -> Unit, onCompleted: () -> Unit) : Flow<WaifuImageResponse> =
        flow{
            apiClient?.getRandomImages()
                ?.suspendOnSuccess {
                    emit(data)
                }
                ?.suspendOnError {
                    onError(message())
                }
                ?.suspendOnException {
                    onError(message())
                }
                ?.suspendOnFailure {
                    onError(message())
                }
        }.onStart { onStart() }.onCompletion { onCompleted() }.flowOn(Dispatchers.IO)
}