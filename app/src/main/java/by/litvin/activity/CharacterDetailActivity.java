package by.litvin.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import by.litvin.R;
import by.litvin.adapter.CharactersRecyclerViewAdapter;
import by.litvin.model.Character;
import by.litvin.model.Image;


public class CharacterDetailActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        Character character = getIntent().getParcelableExtra(CharactersRecyclerViewAdapter.CHARACTER);
        Image characterThumbnail = character.getThumbnail();
        String imageUrl = String.format("%s.%s", characterThumbnail.getPath(), characterThumbnail.getExtension());
        ImageView characterImage = findViewById(R.id.big_character_image);
        TextView characterName = findViewById(R.id.big_character_name);


        Glide.with(this)
                .load(imageUrl)
                .into(characterImage);
        characterName.setText(character.getName());
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.character_detail_toolbar);
        collapsingToolbarLayout.setTitle(character.getName());
    }
}
