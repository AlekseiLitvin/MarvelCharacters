package by.litvin.di.module;

import by.litvin.adapter.CharactersRecyclerViewAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class CharactersRecyclerViewAdapterModule {

    @Provides
    //TODO rework scope
//    @PerActivity
    public CharactersRecyclerViewAdapter charactersRecyclerViewAdapter() {
        return new CharactersRecyclerViewAdapter();
    }

}
