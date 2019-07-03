package by.litvin.di.module;

import by.litvin.adapter.FavCharactersRecyclerViewAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class FavCharacterActivityModule {

    @Provides
    public FavCharactersRecyclerViewAdapter getAdapter() {
        return new FavCharactersRecyclerViewAdapter();
    }

}
