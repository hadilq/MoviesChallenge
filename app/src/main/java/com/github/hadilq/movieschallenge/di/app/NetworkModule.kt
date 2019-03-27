/***
 * Copyright 2019 Hadi Lashkari Ghouchani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * */
package com.github.hadilq.movieschallenge.di.app

import android.content.Context
import com.github.hadilq.movieschallenge.BuildConfig
import com.github.hadilq.movieschallenge.data.api.Api
import com.github.hadilq.movieschallenge.domain.repository.ApiKeyRepository
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    private val baseUrl: String = "https://api.themoviedb.org/3/"

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): Api = retrofit.create(Api::class.java)

    @Singleton
    @Provides
    fun providePicasso(context: Context): Picasso {
        val downloader = OkHttp3Downloader(context, Long.MAX_VALUE)
        val picasso = Picasso.Builder(context).downloader(downloader).build()
        Picasso.setSingletonInstance(picasso)
        return picasso
    }

    @Singleton
    @Provides
    fun provideInterceptors(apiKeyRepository: ApiKeyRepository): ArrayList<Interceptor> {

        val interceptors = arrayListOf<Interceptor>()

        val keyInterceptor = Interceptor { chain ->

            val original = chain.request()
            val originalHttpUrl = original.url()

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", apiKeyRepository.apiKey())
                .build()

            val requestBuilder = original.newBuilder()
                .url(url)

            val request = requestBuilder.build()
            return@Interceptor chain.proceed(request)
        }

        interceptors.add(keyInterceptor)
        return interceptors
    }

    @Singleton
    @Provides
    fun provideRetrofit(interceptors: ArrayList<Interceptor>): Retrofit {

        val clientBuilder = OkHttpClient.Builder()
        if (!interceptors.isEmpty()) {
            interceptors.forEach { interceptor ->
                clientBuilder.addInterceptor(interceptor)
            }
        }
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
            clientBuilder.addInterceptor(loggingInterceptor)
        }

        return Retrofit.Builder()
            .client(clientBuilder.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build()
    }
}