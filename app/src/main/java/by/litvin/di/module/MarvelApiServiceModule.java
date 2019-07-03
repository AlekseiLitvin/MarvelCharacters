package by.litvin.di.module;

import by.litvin.api.MarvelApi;
import by.litvin.service.MarvelApiService;
import by.litvin.service.MarvelApiServiceImpl;
import dagger.Module;
import dagger.Provides;

@Module(includes = RetrofitModule.class)
public class MarvelApiServiceModule {

    @Provides
    public MarvelApiService marvelApiService(MarvelApi marvelApi) {
        return new MarvelApiServiceImpl(marvelApi);
    }

}
