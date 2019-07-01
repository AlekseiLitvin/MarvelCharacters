package by.litvin.di.component;

import by.litvin.activity.CharacterDetailActivity;
import by.litvin.service.MarvelApiService;
import dagger.Component;

@Component(dependencies = MarvelApiServiceComponent.class)
public interface CharacterDetailActivityComponent {

    MarvelApiService marvelApiService();

    void inject(CharacterDetailActivity characterDetailActivity);

}
