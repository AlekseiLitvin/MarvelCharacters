package by.litvin.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import by.litvin.R;
import by.litvin.adapter.FavCharactersRecyclerViewAdapter;
import by.litvin.viewmodel.CharacterViewModel;

public class FavCharacterActivity extends AppCompatActivity {

    private CharacterViewModel characterViewModel;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_character);

        recyclerView = findViewById(R.id.favorite_characters_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FavCharactersRecyclerViewAdapter favCharactersRecyclerViewAdapter = new FavCharactersRecyclerViewAdapter();
        recyclerView.setAdapter(favCharactersRecyclerViewAdapter);

        characterViewModel = ViewModelProviders.of(this).get(CharacterViewModel.class);
        characterViewModel.getAllCharacters().observe(this, favCharactersRecyclerViewAdapter::submitList);
    }
}
