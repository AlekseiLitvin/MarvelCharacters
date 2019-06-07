package by.litvin.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import by.litvin.R;
import by.litvin.adapter.CharactersRecyclerViewAdapter;
import by.litvin.adapter.RelatedItemRecyclerViewAdapter;
import by.litvin.api.MarvelApi;
import by.litvin.model.Character;
import by.litvin.model.Image;
import by.litvin.service.MarvelApiService;


public class CharacterDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MarvelApiService marvelApiService = new MarvelApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        Character character = getIntent().getParcelableExtra(CharactersRecyclerViewAdapter.CHARACTER);
        Image characterThumbnail = character.getThumbnail();
        String imageUrl = String.format("%s.%s", characterThumbnail.getPath(), characterThumbnail.getExtension());
        ImageView characterImage = findViewById(R.id.big_character_image);

        Glide.with(this)
                .load(imageUrl)
                .into(characterImage);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.character_detail_toolbar);
        collapsingToolbarLayout.setTitle(character.getName());

        recyclerView = findViewById(R.id.comics_recycler_view);
        RelatedItemRecyclerViewAdapter relatedItemRecyclerViewAdapter = new RelatedItemRecyclerViewAdapter(this);
        recyclerView.setAdapter(relatedItemRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



    }
}
