package com.sec.instagramclone.di

import com.google.firebase.auth.FirebaseAuth
import com.sec.instagramclone.data.repository.AppRepository
import com.sec.instagramclone.data.repository.AppRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideAppRepository(impl:AppRepositoryImpl): AppRepository{
        return impl
    }
}