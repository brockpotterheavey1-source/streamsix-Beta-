package com.example.network

/*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * Example Hilt Module demonstrating how to inject the dynamically discovered base URL.
 * Requires adding Hilt dependencies to build.gradle.kts.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * A simple singleton to hold the active base URL after the Splash screen discovers it.
     */
    @Singleton
    class DynamicBaseUrlProvider {
        var baseUrl: String = "https://fallback.example.com" // Default fallback
    }

    @Provides
    @Singleton
    fun provideDynamicBaseUrlProvider(): DynamicBaseUrlProvider = DynamicBaseUrlProvider()

    @Provides
    @Singleton
    fun provideDynamicUrlInterceptor(urlProvider: DynamicBaseUrlProvider): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            val dynamicUrl = urlProvider.baseUrl.toHttpUrlOrNull()
            
            if (dynamicUrl != null) {
                val newUrl = request.url.newBuilder()
                    .scheme(dynamicUrl.scheme)
                    .host(dynamicUrl.host)
                    .port(dynamicUrl.port)
                    .build()
                
                request = request.newBuilder()
                    .url(newUrl)
                    .build()
            }
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, urlProvider: DynamicBaseUrlProvider): Retrofit {
        return Retrofit.Builder()
            // The base URL must be set, but the interceptor will override it dynamically
            .baseUrl(urlProvider.baseUrl) 
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}
*/
