package by.litvin.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import by.litvin.R;
import by.litvin.adapter.CharactersRecyclerViewAdapter;
import by.litvin.adapter.RelatedItemRecyclerViewAdapter;
import by.litvin.constant.LinkType;
import by.litvin.databinding.ActivityCharacterDetailBinding;
import by.litvin.model.ApiResponse;
import by.litvin.model.Character;
import by.litvin.model.Link;
import by.litvin.model.RelatedItem;
import by.litvin.model.ResponseData;
import by.litvin.service.MarvelApiService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


public class CharacterDetailActivity extends AppCompatActivity {

    private MarvelApiService marvelApiService;

    public CharacterDetailActivity() {
        marvelApiService = new MarvelApiService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCharacterDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_character_detail);

        Toolbar toolbar = findViewById(R.id.character_image_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Character character = getIntent().getParcelableExtra(CharactersRecyclerViewAdapter.CHARACTER);
        binding.setCharacter(character);

        populateRelatedItems(character);
        populateRelatedLinks(character);
    }

    //TODO rewrite links to data binding and add additional links
    private void populateRelatedLinks(Character character) {
        TextView textView = findViewById(R.id.wiki_page_link);
        textView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            for (Link link : character.getLinks()) {
                if (link.getType().equals(LinkType.WIKI_PAGE)) {
                    intent.setData(Uri.parse(link.getUrl()));
                    startActivity(intent);
                }
            }
        });
    }

    private void populateRelatedItems(Character character) {
        int characterId = character.getId();

        Observable<ApiResponse<RelatedItem>> comicsForCharacter = marvelApiService.getComicsForCharacter(characterId);
        RelatedItemRecyclerViewAdapter comicsRecyclerViewAdapter = createRecyclerViewWithAdapter(R.id.comics_recycler_view);
        subscribeRelatedItems(comicsForCharacter,
                comicsRecyclerViewAdapter::addRelatedItems,
                Throwable::printStackTrace,
                handleRequestProgress(R.id.comics_progress_bar, R.id.comics_recycler_view));

        Observable<ApiResponse<RelatedItem>> seriesForCharacter = marvelApiService.getSeriesForCharacter(characterId);
        RelatedItemRecyclerViewAdapter seriesRecyclerViewAdapter = createRecyclerViewWithAdapter(R.id.series_recycler_view);
        subscribeRelatedItems(seriesForCharacter,
                seriesRecyclerViewAdapter::addRelatedItems,
                Throwable::printStackTrace,
                handleRequestProgress(R.id.series_progress_bar, R.id.series_recycler_view));

        Observable<ApiResponse<RelatedItem>> eventsForCharacter = marvelApiService.getEventsForCharacter(characterId);
        RelatedItemRecyclerViewAdapter eventsRecyclerViewAdapter = createRecyclerViewWithAdapter(R.id.events_recycler_view);
        subscribeRelatedItems(eventsForCharacter,
                eventsRecyclerViewAdapter::addRelatedItems,
                Throwable::printStackTrace,
                handleRequestProgress(R.id.events_progress_bar, R.id.events_recycler_view));
    }

    public RelatedItemRecyclerViewAdapter createRecyclerViewWithAdapter(@IdRes int id) {
        RecyclerView recyclerView = findViewById(id);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        new LinearSnapHelper().attachToRecyclerView(recyclerView);
        RelatedItemRecyclerViewAdapter recyclerViewAdapter = new RelatedItemRecyclerViewAdapter(this);
        recyclerView.setAdapter(recyclerViewAdapter);
        return recyclerViewAdapter;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Action handleRequestProgress(@IdRes int progressBarId, @IdRes int recyclerViewId) {
        return () -> {
            ProgressBar progressBar = findViewById(progressBarId);
            progressBar.setVisibility(View.INVISIBLE);
        };
    }

    private void subscribeRelatedItems(Observable<ApiResponse<RelatedItem>> observable,
                                       Consumer<List<RelatedItem>> onNext,
                                       Consumer<Throwable> onError,
                                       Action onComplete) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ApiResponse::getData)
                .map(ResponseData::getResults)
                .subscribe(onNext, onError, onComplete);
    }

}
