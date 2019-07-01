package by.litvin.di.module;

import by.litvin.api.MarvelApi;
import by.litvin.service.MarvelApiService;
import dagger.Module;
import dagger.Provides;

@Module
public class MarvelApiServiceModule {

    @Provides
    public MarvelApiService marvelApiService(MarvelApi marvelApi) {
        return new MarvelApiService(marvelApi);
    }

}
