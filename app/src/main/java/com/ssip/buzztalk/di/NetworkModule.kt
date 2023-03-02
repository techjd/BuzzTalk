package com.ssip.buzztalk.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.ssip.buzztalk.R
import com.ssip.buzztalk.api.*
import com.ssip.buzztalk.utils.Constants.CHAT_SERVER_URL
import com.ssip.buzztalk.utils.Constants.DEVELOPMENT_BASE_URL
import com.ssip.buzztalk.utils.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://192.168.68.163:5500/api/")
    }

    @Provides
    @Singleton
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(BODY)
        return loggingInterceptor
    }

    @Provides
    @Singleton
    fun providesSocket(): Socket {
        return IO.socket("http://192.168.68.163:5500/").connect()
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    fun providesAuthAPI(retrofitBuilder: Retrofit.Builder, loggingInterceptor: HttpLoggingInterceptor): AuthAPI {
        val client: OkHttpClient = OkHttpClient.Builder()
          .addInterceptor(loggingInterceptor)
          .build()
        return retrofitBuilder.client(client).build().create(AuthAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): UserAPI {
        return retrofitBuilder.build().create(UserAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesChatAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): ChatAPI {
        return retrofitBuilder.client(okHttpClient).build().create(ChatAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesPostAPI(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): PostAPI {
        return retrofitBuilder.client(okHttpClient).build().create(PostAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesCompanyAPI(retrofitBuilder: Builder, okHttpClient: OkHttpClient): CompanyAPI {
        return retrofitBuilder.client(okHttpClient).build().create(CompanyAPI::class.java)
    }

    @Provides
    @Singleton
    fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ): RequestManager {
        return Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                .placeholder(R.drawable.ic_launcher_foreground)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
        )
    }

    @Provides
    @Singleton
    fun providesNetworkConnection(
        @ApplicationContext context: Context
    ): NetworkManager {
        return NetworkManager(context)
    }
}