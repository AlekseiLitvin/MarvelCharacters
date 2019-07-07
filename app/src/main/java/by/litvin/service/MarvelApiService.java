package by.litvin.service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import by.litvin.api.MarvelApi;
import by.litvin.model.ApiResponse;
import by.litvin.model.Character;
import by.litvin.model.RelatedItem;
import by.litvin.model.ResponseData;
import by.litvin.util.HashCalculator;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MarvelApiService {

    public static final int RELATED_ITEMS_LIMIT = 5;
    //TODO move properties to separate file
    private static final int CHARACTER_LIMIT = 10;
    private static final int RANDOM_CHARACTER_LIMIT = 1;
    private MarvelApi marvelApi = MarvelApi.Factory.create(MarvelApi.BASE_URL);

    private String timestamp = String.valueOf(System.currentTimeMillis());
    private String hash = HashCalculator.calculate(timestamp, MarvelApi.PRIVATE_KEY, MarvelApi.PUBLIC_KEY);

    public void populateCharactersRecyclerView(int offset,
                                               Consumer<List<Character>> onNext,
                                               Consumer<Throwable> onError,
                                               Action onComplete) {

        getCharactersWithOffset(offset, CHARACTER_LIMIT, onNext, onError, onComplete);
    }

    public void getRandomCharacter(Consumer<List<Character>> onNext,
                                   Consumer<Throwable> onError,
                                   Action onComplete) {
        AtomicReference<Integer> total = new AtomicReference<>(0);
        Action onTotalComplete = () -> {
            int randomCharacterOffset = new Random().nextInt(total.get() + 1);
            getCharactersWithOffset(randomCharacterOffset, RANDOM_CHARACTER_LIMIT, onNext, onError, onComplete);
        };
        marvelApi.getAllCharacters(RANDOM_CHARACTER_LIMIT, 0, timestamp, MarvelApi.PUBLIC_KEY, hash)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ApiResponse::getData)
                .map(ResponseData::getTotal)
                .subscribe(result -> total.set(result), Throwable::printStackTrace, onTotalComplete);
    }


    public Observable<ApiResponse<RelatedItem>> getComicsForCharacter(int characterId) {
        return marvelApi.getComicsWithCharacter(characterId, RELATED_ITEMS_LIMIT, timestamp, MarvelApi.PUBLIC_KEY, hash);
    }

    public Observable<ApiResponse<RelatedItem>> getSeriesForCharacter(int characterId) {
        return marvelApi.getSeriesWithCharacter(characterId, RELATED_ITEMS_LIMIT, timestamp, MarvelApi.PUBLIC_KEY, hash);
    }

    public Observable<ApiResponse<RelatedItem>> getEventsForCharacter(int characterId) {
        return marvelApi.getEventsWithCharacter(characterId, RELATED_ITEMS_LIMIT, timestamp, MarvelApi.PUBLIC_KEY, hash);
    }

    private void getCharactersWithOffset(int offset,
                                         int characterLimit,
                                         Consumer<List<Character>> onNext,
                                         Consumer<Throwable> onError,
                                         Action onComplete) {
        marvelApi.getAllCharacters(characterLimit, offset, timestamp, MarvelApi.PUBLIC_KEY, hash)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ApiResponse::getData)
                .map(ResponseData::getResults)
                .subscribe(onNext, onError, onComplete); //TODO add error handling logic
    }

}
