package by.litvin.di.component;

import by.litvin.api.MarvelApi;
import by.litvin.di.module.RetrofitModule;
import by.litvin.service.MarvelApiService;
import dagger.Component;

@Component(modules = RetrofitModule.class)
public interface MarvelApiServiceComponent {

    MarvelApi marvelApi();

    void inject(MarvelApiService marvelApiService);

}
