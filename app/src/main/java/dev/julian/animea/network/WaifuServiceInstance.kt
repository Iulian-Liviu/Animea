package dev.julian.animea.network

import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WaifuServiceInstance {
    private var _waifuServiceInterface: WaifuServiceInterface? = null;

    fun getInstance() : WaifuServiceInterface? {
        if (_waifuServiceInterface == null){
            _waifuServiceInterface =
                Retrofit.Builder()
                    .baseUrl("https://api.waifu.im/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
                    .build()
                    .create(WaifuServiceInterface::class.java)
            return _waifuServiceInterface;
        }
        return _waifuServiceInterface;
    }
}