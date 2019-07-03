package by.litvin.di.component;

import by.litvin.activity.FavCharacterActivity;
import by.litvin.adapter.FavCharactersRecyclerViewAdapter;
import by.litvin.di.module.FavCharacterActivityModule;
import dagger.Component;

@Component(modules = FavCharacterActivityModule.class)
public interface FavCharactersActivityComponent {

    FavCharactersRecyclerViewAdapter adapter();

    void inject(FavCharacterActivity favCharacterActivity);

}
