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

import android.app.Application
import android.content.Context
import com.github.hadilq.movieschallenge.App
import com.github.hadilq.movieschallenge.data.repository.ApiKeyRepositoryImpl
import com.github.hadilq.movieschallenge.domain.repository.ApiKeyRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideApplication(app: App): Application = app

    @Singleton
    @Provides
    fun provideAppContext(app: App): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideApiKeyRepository(): ApiKeyRepository = ApiKeyRepositoryImpl()
}