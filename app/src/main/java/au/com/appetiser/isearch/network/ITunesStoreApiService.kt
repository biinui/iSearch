package au.com.appetiser.isearch.network

import au.com.appetiser.isearch.network.model.MovieListResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://itunes.apple.com/"

private const val TERM    = "term"
private const val COUNTRY = "country"
private const val MEDIA   = "media"

private val moshi = Moshi.Builder()
                         .add(KotlinJsonAdapterFactory())
                         .build()

private val retrofit = Retrofit.Builder()
                               .addConverterFactory(MoshiConverterFactory.create(moshi))
                               .addCallAdapterFactory(CoroutineCallAdapterFactory())
                               .client(ITunesStoreHttpClient.getClient())
                               .baseUrl(BASE_URL)
                               .build()

interface ITunesStoreApiService {

    @GET("search")
    suspend fun get(
        @Query(TERM   ) term   : String,
        @Query(COUNTRY) country: String,
        @Query(MEDIA  ) media  : String,
    ): MovieListResponse

}

object ITunesStoreApi {
    val retrofitService: ITunesStoreApiService by lazy {
        retrofit.create(ITunesStoreApiService::class.java)
    }
}