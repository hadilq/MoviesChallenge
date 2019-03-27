package com.github.hadilq.movieschallenge

import android.content.Context
import android.content.Intent
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.github.hadilq.movieschallenge.data.api.Api
import com.github.hadilq.movieschallenge.data.api.dto.MovieDto
import com.github.hadilq.movieschallenge.data.api.dto.PopularDto
import com.github.hadilq.movieschallenge.di.PopularMoviesActivityModule
import com.github.hadilq.movieschallenge.di.PopularMoviesModule
import com.github.hadilq.movieschallenge.di.app.AppComponent
import com.github.hadilq.movieschallenge.di.app.AppModule
import com.github.hadilq.movieschallenge.di.app.DatabaseModule
import com.github.hadilq.movieschallenge.di.viewmodel.ViewModelModule
import com.github.hadilq.movieschallenge.domain.repository.MovieRepository
import com.github.hadilq.movieschallenge.presentation.popular.PopularMoviesActivity
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.squareup.picasso.Picasso
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import javax.inject.Inject
import javax.inject.Singleton

@RunWith(AndroidJUnit4::class)
class PopularMoviesActivityTest {

    @get:Rule
    var activityRule: ActivityTestRule<PopularMoviesActivity> = ActivityTestRule(
        PopularMoviesActivity::class.java,
        true, // initialTouchMode
        false
    )

    private lateinit var repository: MovieRepository
    private lateinit var app: App

    @Inject
    lateinit var api: Api

    @Before
    @Test
    fun setup() {
        repository = mock()
        app = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as App

        val appComponent = DaggerPopularMoviesActivityTest_TestAppComponent
            .builder()
            .networkModule(TestNetworkModule())
            .application(app)
            .build()
        appComponent.inject(app)
        appComponent.inject(this)
    }

    @Test
    fun launchActivity() {
        val dto = PopularDto(1, 1, 1, ArrayList<MovieDto>().apply {
            add(MovieDto(0, "One", "one", 8f, 12, "/dlkf", "/aslk", 4f))
        })
        `when`(api.getPopular(any(), any())).doReturn(Single.just(dto))

        activityRule.launchActivity(Intent())

        onView(withId(R.id.titleView)).check(matches(withText("one")))
    }

    @Singleton
    @Component(
        modules = [
            AndroidSupportInjectionModule::class,
            AppModule::class,
            TestNetworkModule::class,
            DatabaseModule::class,
            ViewModelModule::class,
            PopularMoviesActivityModule::class,
            PopularMoviesModule::class
        ]
    )
    interface TestAppComponent : AppComponent {
        @Component.Builder
        interface Builder {
            @BindsInstance
            fun networkModule(module: TestNetworkModule): Builder

            @BindsInstance
            fun application(app: App): Builder

            fun build(): TestAppComponent
        }

        fun inject(testClass: PopularMoviesActivityTest)
    }

    @Module
    class TestNetworkModule {

        private val api: Api = mock()

        @Singleton
        @Provides
        fun provideApi(): Api = api

        @Singleton
        @Provides
        fun providePicasso(context: Context): Picasso = Picasso.Builder(context).build()
    }
}