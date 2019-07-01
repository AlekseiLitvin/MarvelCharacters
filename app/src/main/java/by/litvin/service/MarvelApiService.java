package by.litvin.service;

import java.util.List;

import javax.inject.Inject;

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

    //TODO move properties to separate file
    private static final int LOADED_CHARACTERS_NUMBER = 10;

    //TODO inject with Dagger
    private MarvelApi marvelApi;

    private String timestamp = String.valueOf(System.currentTimeMillis());
    private String hash = HashCalculator.calculate(timestamp, MarvelApi.PRIVATE_KEY, MarvelApi.PUBLIC_KEY);

    @Inject
    public MarvelApiService(MarvelApi marvelApi) {
        this.marvelApi = marvelApi;
    }

    public void populateCharactersRecyclerView(int offset,
                                               Consumer<List<Character>> onNext,
                                               Consumer<Throwable> onError,
                                               Action onComplete) {
        marvelApi.getAllCharacters(LOADED_CHARACTERS_NUMBER, offset, timestamp, MarvelApi.PUBLIC_KEY, hash)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(characterApiResponse -> characterApiResponse.getData())
                .map(ResponseData::getResults)
                .subscribe(onNext, onError, onComplete); //TODO add error handling logic
    }


    public void populateComicsRecyclerView(int characterId,
                                           Consumer<List<RelatedItem>> onNext,
                                           Consumer<Throwable> onError) {
        Observable<ApiResponse<RelatedItem>> comicsWithCharacter =
                marvelApi.getComicsWithCharacter(characterId, timestamp, MarvelApi.PUBLIC_KEY, hash);
        subscribeRelatedItems(comicsWithCharacter, onNext, onError);
    }

    public void populateSeriesRecyclerView(int characterId,
                                           Consumer<List<RelatedItem>> onNext,
                                           Consumer<Throwable> onError) {
        Observable<ApiResponse<RelatedItem>> seriesWithCharacter =
                marvelApi.getSeriesWithCharacter(characterId, timestamp, MarvelApi.PUBLIC_KEY, hash);
        subscribeRelatedItems(seriesWithCharacter, onNext, onError);
    }

    public void populateEventsRecyclerView(int characterId,
                                           Consumer<List<RelatedItem>> onNext,
                                           Consumer<Throwable> onError) {
        Observable<ApiResponse<RelatedItem>> eventsWithCharacter =
                marvelApi.getEventsWithCharacter(characterId, timestamp, MarvelApi.PUBLIC_KEY, hash);
        subscribeRelatedItems(eventsWithCharacter, onNext, onError);
    }

    private void subscribeRelatedItems(Observable<ApiResponse<RelatedItem>> observable,
                                       Consumer<List<RelatedItem>> onNext,
                                       Consumer<Throwable> onError) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ApiResponse::getData)
                .map(ResponseData::getResults)
                .subscribe(onNext, onError);
    }


}
