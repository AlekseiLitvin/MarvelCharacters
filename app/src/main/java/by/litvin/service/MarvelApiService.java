package by.litvin.service;

import java.util.List;

import by.litvin.model.Character;
import by.litvin.model.RelatedItem;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public interface MarvelApiService {
    void populateCharactersRecyclerView(int offset,
                                        Consumer<List<Character>> onNext,
                                        Consumer<Throwable> onError,
                                        Action onComplete);

    void populateComicsRecyclerView(int characterId,
                                    Consumer<List<RelatedItem>> onNext,
                                    Consumer<Throwable> onError);

    void populateSeriesRecyclerView(int characterId,
                                    Consumer<List<RelatedItem>> onNext,
                                    Consumer<Throwable> onError);

    void populateEventsRecyclerView(int characterId,
                                    Consumer<List<RelatedItem>> onNext,
                                    Consumer<Throwable> onError);
}
