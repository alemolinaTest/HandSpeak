package com.amolina.handspeak.di

import android.content.Context
import com.amolina.handspeak.ui.gestures.helpers.GestureRecognizerHelper
import com.amolina.handspeak.ui.gestures.helpers.ImageAnalyzerHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGestureRecognizerHelper(
        @ApplicationContext context: Context
    ): GestureRecognizerHelper {
        return GestureRecognizerHelper(context)
    }

    @Singleton
    @Provides
    fun provideImageAnalyzerHelper(
        gestureRecognizerHelper: GestureRecognizerHelper
    ): ImageAnalyzerHelper {
        return ImageAnalyzerHelper(gestureRecognizerHelper)
    }
}