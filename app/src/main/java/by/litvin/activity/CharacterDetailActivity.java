package by.litvin.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import javax.inject.Inject;

import by.litvin.R;
import by.litvin.adapter.CharactersRecyclerViewAdapter;
import by.litvin.adapter.RelatedItemRecyclerViewAdapter;
import by.litvin.constant.LinkType;
import by.litvin.di.component.DaggerCharacterDetailActivityComponent;
import by.litvin.di.component.DaggerMarvelApiServiceComponent;
import by.litvin.model.Character;
import by.litvin.model.Image;
import by.litvin.model.Link;
import by.litvin.service.MarvelApiService;


public class CharacterDetailActivity extends AppCompatActivity {

    @Inject
    MarvelApiService marvelApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        DaggerCharacterDetailActivityComponent.builder()
                .marvelApiServiceComponent(DaggerMarvelApiServiceComponent.create())
                .build()
                .inject(this);

        Character character = getIntent().getParcelableExtra(CharactersRecyclerViewAdapter.CHARACTER);

        populateToolbar(character);
        populateCharacterDescription(character);
        populateRelatedItems(character);
        populateRelatedLinks(character);

    }

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
        RecyclerView comicsRecyclerView = findViewById(R.id.comics_recycler_view);
        RelatedItemRecyclerViewAdapter comicsRecyclerViewAdapter = new RelatedItemRecyclerViewAdapter(this);
        comicsRecyclerView.setAdapter(comicsRecyclerViewAdapter);
        comicsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        marvelApiService.populateComicsRecyclerView(character.getId(),
                comicsRecyclerViewAdapter::addRelatedItems,
                Throwable::printStackTrace);

        RecyclerView seriesRecyclerView = findViewById(R.id.series_recycler_view);
        RelatedItemRecyclerViewAdapter seriesRecyclerViewAdapter = new RelatedItemRecyclerViewAdapter(this);
        seriesRecyclerView.setAdapter(seriesRecyclerViewAdapter);
        seriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        marvelApiService.populateSeriesRecyclerView(character.getId(),
                seriesRecyclerViewAdapter::addRelatedItems,
                Throwable::printStackTrace);

        RecyclerView eventsRecyclerView = findViewById(R.id.events_recycler_view);
        RelatedItemRecyclerViewAdapter eventsRecyclerViewAdapter = new RelatedItemRecyclerViewAdapter(this);
        eventsRecyclerView.setAdapter(eventsRecyclerViewAdapter);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        marvelApiService.populateEventsRecyclerView(character.getId(),
                eventsRecyclerViewAdapter::addRelatedItems,
                Throwable::printStackTrace);
    }

    private void populateCharacterDescription(Character character) {
        TextView characterDescription = findViewById(R.id.character_description);
        //TODO implement default value for TextView, if value is null
        String description = character.getDescription();
        characterDescription.setText(description.isEmpty() ?
                getResources().getString(R.string.description_is_not_available) : description);
    }

    private void populateToolbar(Character character) {
        Image characterThumbnail = character.getThumbnail();
        String imageUrl = String.format("%s.%s", characterThumbnail.getPath(), characterThumbnail.getExtension());
        ImageView characterImage = findViewById(R.id.big_character_image);

        Glide.with(this)
                .load(imageUrl)
                .into(characterImage);
        //TODO implement back button on toolbar
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.character_detail_toolbar);
        collapsingToolbarLayout.setTitle(character.getName());
    }
}
