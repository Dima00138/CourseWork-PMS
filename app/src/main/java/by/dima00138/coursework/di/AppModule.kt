package by.dima00138.coursework.di

import android.content.Context
import by.dima00138.coursework.services.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebase(@ApplicationContext context : Context) : Firebase {
        return Firebase(context = context)
    }
}