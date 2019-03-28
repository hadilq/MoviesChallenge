[![CircleCI](https://circleci.com/gh/hadilq/MoviesChallenge.svg?style=svg)](https://circleci.com/gh/hadilq/MoviesChallenge)

Movies Challenge
===

It's a sample app for the movies challenge. Generally, it's a one activity app that loads list of popular TV shows from 
`api.themoviedb.org` and displays them page by page.

Modules
---
This project implements the Clean Architecture. As you can read from [https://blog.cleancoder.com](http://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
the Clean Architecture has an onion shape structure like this

![clean-architecture](https://blog.cleancoder.com/uncle-bob/images/2012-08-13-the-clean-architecture/CleanArchitecture.jpg)

In this project we manage it like this

- `domain` module: includes the `Entity` and `Use Cases`.
- `data` module: includes the `Gateways`, `DB`, `External Interfaces` and `Device`.
- `presentation` includes  module: the `MVVM` architectural pattern, which is the `UI` in the diagram above.
- `app` module: integrates all classes and assemble the apk file. Mainly the modules and providers of Dagger framework live here.

By applying the DIP, dependency inversion principle, both `data` and `presentation` modules depends on the `domain` 
module as the heart of the app. Also, the `app` module have to depend on every other modules.

Domain Module
---
The only entity here is the [MovieEntity](https://github.com/hadilq/MoviesChallenge/blob/master/domain/src/main/java/com/github/hadilq/movieschallenge/domain/entity/MovieEntity.kt) 
and the only use case is [GetMovies](https://github.com/hadilq/MoviesChallenge/blob/master/domain/src/main/java/com/github/hadilq/movieschallenge/domain/usecase/GetMovies.kt).
It's implementation is like this
```kotlin
interface GetMovies {

    /**
     * Returns a stream of ResultStates to load movies.
     */
    fun loadMovies(refresh: Boolean): Flowable<ResultState<PagedList<MovieEntity>>>

    /**
     * Retries the last failed request to server.
     */
    fun retry()
}
```
Also you can find the abstraction of [MovieRepository](https://github.com/hadilq/MoviesChallenge/blob/master/domain/src/main/java/com/github/hadilq/movieschallenge/domain/repository/MovieRepository.kt) 
in this module.
```kotlin
interface MovieRepository {

    /**
     * Returns a stream of ResultStates to load movies. To delete the database, just set the [refresh] to true.
     */
    fun loadMovies(refresh: Boolean): Flowable<ResultState<PagedList<MovieEntity>>>

    /**
     * Retries the last failed request to server.
     */
    fun retry()
}
```

Data Module
---
The main class of this module is the implementation of `MovieRepository` which is [MovieRepositoryImpl](https://github.com/hadilq/MoviesChallenge/blob/master/data/src/main/java/com/github/hadilq/movieschallenge/data/repository/MovieRepositoryImpl.kt).
This class has two data sources from API and database. The API data source is [PopularMovieDataSource](https://github.com/hadilq/MoviesChallenge/blob/master/data/src/main/java/com/github/hadilq/movieschallenge/data/datasource/api/PopularMovieDataSource.kt)
and the database data source is [MovieDataSource](https://github.com/hadilq/MoviesChallenge/blob/master/data/src/main/java/com/github/hadilq/movieschallenge/data/datasource/db/MovieDataSource.kt).
These data sources are responsible to map the external data classes to the only data class of this app, which is the `MovreEntity`.
The `PopularMovieDataSource` is like this
```kotlin
interface PopularMovieDataSource {

    /**
     * Calls the API for popular movies.
     */
    fun call(page: Int): Single<MoviesList>

    data class MoviesList(
        val page: Int,
        val totalResults: Int,
        val totalPages: Int,
        val results: List<MovieEntity>
    )
}
``` 
and `MovieDataSource` is
```kotlin
interface MovieDataSource {

    /**
     * Returns the popular movies.
     */
    fun popular(): DataSource.Factory<Int, MovieEntity>

    /**
     * Returns every available movies.
     */
    fun all(): List<MovieEntity>

    /**
     * Saves the list of popular movies.
     */
    fun save(list: PopularMovieDataSource.MoviesList)

    /**
     * Deletes all the movies in the table.
     */
    fun deleteAll()
}
```

Presentation Module
---
The main classes of this module are [PopularMoviesViewModel](https://github.com/hadilq/MoviesChallenge/blob/master/presentation/src/main/java/com/github/hadilq/movieschallenge/presentation/popular/PopularMoviesViewModel.kt)
and [PopularMoviesActivity](https://github.com/hadilq/MoviesChallenge/blob/master/presentation/src/main/java/com/github/hadilq/movieschallenge/presentation/popular/PopularMoviesActivity.kt).
`PopularMoviesViewModel` is the view model of `MVVM` architectural pattern and `PopularMoviesActivity` is responsible for 
the view in this pattern.

App Module
---
The dagger modules are all here. The [AppComponent](https://github.com/hadilq/MoviesChallenge/blob/master/app/src/main/java/com/github/hadilq/movieschallenge/di/app/AppComponent.kt) is like this
```kotlin
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        ViewModelModule::class,
        PopularMoviesActivityModule::class,
        PopularMoviesModule::class
    ]
)
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}
```
Also you can find the [App](https://github.com/hadilq/MoviesChallenge/blob/master/app/src/main/java/com/github/hadilq/movieschallenge/App.kt)
in this module.
```kotlin
class App : DaggerApplication() {

    @Inject
    lateinit var applicationInjector: DispatchingAndroidInjector<App>

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = applicationInjector

    override fun onCreate() {
        DaggerAppComponent.builder().create(this).inject(this)
        super.onCreate()
    }
}
```