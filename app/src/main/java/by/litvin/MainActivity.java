package by.litvin;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import by.litvin.adapter.CharactersRecyclerViewAdapter;
import by.litvin.callback.ItemTouchHelperCallback;
import by.litvin.model.Character;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private CharactersRecyclerViewAdapter charactersRecyclerViewAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_recycler_view);
        setSupportActionBar(toolbar);

        charactersRecyclerViewAdapter = new CharactersRecyclerViewAdapter(this);
        recyclerView = findViewById(R.id.characters_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(charactersRecyclerViewAdapter);

        ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelperCallback(charactersRecyclerViewAdapter, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView navHeaderLogo = headerView.findViewById(R.id.nav_logo_header);
        Glide.with(this).load(R.drawable.marvel_logo)
                .apply(new RequestOptions().fitCenter())
                .into(navHeaderLogo);

        //TODO test data, remove later
        List<Character> characterList = new ArrayList<>();
        Character character1 = new Character();
        character1.setName(getResources().getString(R.string.test_character_1_name));
        character1.setDescription(getResources().getString(R.string.test_character_1_description));

        Character character2 = new Character();
        character2.setName(getResources().getString(R.string.test_character_2_name));
        character2.setDescription(getResources().getString(R.string.test_character_2_description));

        characterList.add(character1);
        characterList.add(character2);
        charactersRecyclerViewAdapter.setCharacterItems(characterList);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}
