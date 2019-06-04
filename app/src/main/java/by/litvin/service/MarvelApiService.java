package by.litvin.service;

import java.util.List;

import by.litvin.adapter.CharactersRecyclerViewAdapter;
import by.litvin.api.MarvelApi;
import by.litvin.model.Character;
import by.litvin.util.HashCalculator;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MarvelApiService {

    //TODO move properties to separate file
    private static final int LOADED_CHARACTERS_NUMBER = 10;

    //TODO inject with Dagger
    private MarvelApi marvelApi = MarvelApi.Factory.create(MarvelApi.BASE_URL);

    public void populateCharactersRecyclerViewAdapter(int offset,
                                                      Consumer<List<Character>> onNext,
                                                      Consumer<Throwable> onError,
                                                      Action onComplete) {

        String timestamp = String.valueOf(System.currentTimeMillis());
        String hash = HashCalculator.calculate(timestamp, MarvelApi.PRIVATE_KEY, MarvelApi.PUBLIC_KEY);

        marvelApi.getAllCharacters(LOADED_CHARACTERS_NUMBER, offset, timestamp, MarvelApi.PUBLIC_KEY, hash)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(characterApiResponse -> characterApiResponse.getData())
                .map(characterResponseData -> characterResponseData.getResults())
                .subscribe(onNext, onError, onComplete); //TODO add error handling logic
    }

}
