package by.litvin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import by.litvin.activity.CharacterDetailActivity;
import by.litvin.activity.FavCharacterActivity;
import by.litvin.activity.PreferencesActivity;
import by.litvin.adapter.CharactersRecyclerViewAdapter;
import by.litvin.callback.ItemTouchHelperCallback;
import by.litvin.model.Character;
import by.litvin.service.MarvelApiService;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;

import static by.litvin.adapter.CharactersRecyclerViewAdapter.CHARACTER;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private int offset = 0;
    private int currentOffset = 0;
    private boolean isLoading = false;

    private CharactersRecyclerViewAdapter charactersRecyclerViewAdapter;
    private DrawerLayout drawerLayout;
    private MarvelApiService marvelApiService = new MarvelApiService(this);
    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (!isLoading && linearLayoutManager.getItemCount() == (linearLayoutManager.findLastVisibleItemPosition() + 3)) {
                isLoading = true;
                currentOffset += offset;
                charactersRecyclerViewAdapter.addNullDataForProgressBar();

                Consumer<List<Character>> onNext = response -> {
                    charactersRecyclerViewAdapter.removeNullDataForProgressBar();
                    charactersRecyclerViewAdapter.setCharacterItems(response);
                };
                marvelApiService.populateCharactersRecyclerView(
                        currentOffset,
                        onNext,
                        Throwable::printStackTrace, //TODO add error handling logic
                        () -> isLoading = false);
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        offset = Integer.parseInt(defaultSharedPreferences.getString(PreferencesActivity.LOADED_CHARACTERS_NUMBER, PreferencesActivity.DEFAULT_OFFSET));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_recycler_view);
        setSupportActionBar(toolbar);

        charactersRecyclerViewAdapter = new CharactersRecyclerViewAdapter();
        RecyclerView recyclerView = findViewById(R.id.characters_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(charactersRecyclerViewAdapter);
        recyclerView.addOnScrollListener(scrollListener);

        ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelperCallback(charactersRecyclerViewAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        ImageView navHeaderLogo = headerView.findViewById(R.id.nav_logo_header);
        Glide.with(this).load(R.drawable.marvel_logo)
                .apply(new RequestOptions().fitCenter())
                .into(navHeaderLogo);

        marvelApiService.populateCharactersRecyclerView(
                currentOffset,
                response -> charactersRecyclerViewAdapter.setCharacterItems(response),
                Throwable::printStackTrace,
                Functions.EMPTY_ACTION);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.characters_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.google_blue, R.color.google_green, R.color.google_red, R.color.google_yellow);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            currentOffset += offset;
            marvelApiService.populateCharactersRecyclerView(
                    currentOffset,
                    response -> charactersRecyclerViewAdapter.setCharacterItems(response),
                    Throwable::printStackTrace,
                    () -> swipeRefreshLayout.setRefreshing(false));
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = new Intent();
        switch (menuItem.getItemId()) {
            case R.id.favorite_characters:
                intent.setClass(this, FavCharacterActivity.class);
                startActivity(intent);
                break;
            case R.id.app_settings_menu:
                intent.setClass(this, PreferencesActivity.class);
                startActivity(intent);
                break;
            case R.id.random_character:
                AtomicReference<Character> character = new AtomicReference<>();
                Consumer<List<Character>> onNext = characters -> {
                    character.set(characters.get(0));
                };

                Action onComplete = () -> {
                    intent.setClass(this, CharacterDetailActivity.class);
                    intent.putExtra(CHARACTER, character.get());
                    startActivity(intent);
                };
                marvelApiService.getRandomCharacter(onNext, Throwable::printStackTrace, onComplete);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
