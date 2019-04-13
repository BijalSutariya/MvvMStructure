package com.example.mvvmstructure.domainlayer.datasources.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvmstructure.domainlayer.model.Movie
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule constructor(var context: Context) {

    @Provides
    @Singleton
    fun getMovieDataBase(): MovieDatabase {
        return Room.databaseBuilder(context,
            MovieDatabase::class.java,"movi_database").build()
    }
    @Dao
    interface MovieDao {

        @get:Query("SELECT * from movie")
        val movieList: LiveData<List<Movie>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(movieList: List<Movie>): LongArray

        @Query("SELECT * from movie WHERE imdbID = :id")
        fun getMovie(id: String): LiveData<Movie>

        @Query("DELETE FROM movie")
        fun removeAllMovies()

    }

    @Database(entities = [ Movie::class ], version = 1, exportSchema = false)
    abstract class MovieDatabase : RoomDatabase() {
        abstract fun movieDao(): MovieDao
    }
}