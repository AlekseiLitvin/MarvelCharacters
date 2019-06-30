package by.litvin.di.module;

import android.content.Context;

import by.litvin.di.scope.PerApplication;
import dagger.Module;
import dagger.Provides;

@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @PerApplication
    @Provides
    public Context provideContext() {
        return context;
    }
}
