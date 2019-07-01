package by.litvin.di.component;

import by.litvin.MainActivity;
import by.litvin.adapter.CharactersRecyclerViewAdapter;
import by.litvin.di.module.CharactersRecyclerViewAdapterModule;
import by.litvin.di.scope.PerActivity;
import by.litvin.service.MarvelApiService;
import dagger.Component;

@PerActivity
@Component(modules = {CharactersRecyclerViewAdapterModule.class}, dependencies = MarvelApiServiceComponent.class)
public interface MainActivityComponent {

    MarvelApiService marvelApiService();

    CharactersRecyclerViewAdapter recyclerViewAdapter();

    void injectMainActivity(MainActivity mainActivity);

}
