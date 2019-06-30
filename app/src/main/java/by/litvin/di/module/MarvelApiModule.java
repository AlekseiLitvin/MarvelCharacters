package by.litvin.di.module;

import by.litvin.api.MarvelApi;
import by.litvin.di.scope.PerApplication;
import dagger.Module;
import dagger.Provides;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MarvelApiModule {

    private static final String BASE_URL = "http://gateway.marvel.com/v1/public/";

    @Provides
    @PerApplication
    public MarvelApi getMarvelApi(Retrofit retrofit) {
        return retrofit.create(MarvelApi.class);
    }

    @Provides
    @PerApplication
    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @PerApplication
    public HttpLoggingInterceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

}
