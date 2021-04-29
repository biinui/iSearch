package au.com.appetiser.isearch.network

import okhttp3.OkHttpClient

class ITunesStoreHttpClient : OkHttpClient() {

    companion object {

        fun getClient(): OkHttpClient {
            return Builder().addInterceptor { chain ->
                val original = chain.request()
                val url = original.url
                                  .newBuilder()
                                  .build()
                val request = original.newBuilder()
                                      .url(url)
                                      .build()
                chain.proceed(request)
            }.build()
        }

    }

}