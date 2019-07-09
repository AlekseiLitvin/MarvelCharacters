package by.litvin.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import by.litvin.R;
import by.litvin.adapter.FavCharactersRecyclerViewAdapter;
import by.litvin.model.Character;
import by.litvin.viewmodel.CharacterViewModel;

public class FavCharacterActivity extends AppCompatActivity {

    private CharacterViewModel characterViewModel;
    private RecyclerView recyclerView;
    private FavCharactersRecyclerViewAdapter favCharactersRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_character);

        recyclerView = findViewById(R.id.favorite_characters_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favCharactersRecyclerViewAdapter = new FavCharactersRecyclerViewAdapter();
        recyclerView.setAdapter(favCharactersRecyclerViewAdapter);

        characterViewModel = ViewModelProviders.of(this).get(CharacterViewModel.class);
        characterViewModel.getAllCharacters().observe(this, favCharactersRecyclerViewAdapter::submitList);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Character character = favCharactersRecyclerViewAdapter.getCharacterAt(viewHolder.getAdapterPosition());
                characterViewModel.delete(character);
                Snackbar.make(recyclerView.getRootView(), "Character has been deleted", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", view -> characterViewModel.insert(character))
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
    }
}
