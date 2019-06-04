package by.litvin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import by.litvin.adapter.CharactersRecyclerViewAdapter;
import by.litvin.api.MarvelApi;
import by.litvin.callback.ItemTouchHelperCallback;
import by.litvin.model.Character;
import by.litvin.service.MarvelApiService;
import by.litvin.util.HashCalculator;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //TODO move properties to separate file
    private static final int DEFAULT_OFFSET = 10;
    private int offset = 0;
    private boolean isLoading = false;

    private CharactersRecyclerViewAdapter charactersRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private MarvelApiService marvelApiService = new MarvelApiService(); //TODO Inject using dagger
    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            if (!isLoading && linearLayoutManager.getItemCount() == (linearLayoutManager.findLastVisibleItemPosition() + 1)) {
                isLoading = true;
                offset += DEFAULT_OFFSET;
                charactersRecyclerViewAdapter.addNullDataForProgressBar();

                Consumer<List<Character>> onNext = response -> {
                    charactersRecyclerViewAdapter.removeNullDataForProgressBar();
                    charactersRecyclerViewAdapter.setCharacterItems(response);
                };
                marvelApiService.populateCharactersRecyclerViewAdapter(
                        offset,
                        onNext,
                        Throwable::printStackTrace, //TODO add error handling logic
                        () -> isLoading = false);
            }
        }
    };

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
        recyclerView.addOnScrollListener(scrollListener);

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


        String timestamp = String.valueOf(System.currentTimeMillis());

        String hash = HashCalculator.calculate(timestamp, MarvelApi.PRIVATE_KEY, MarvelApi.PUBLIC_KEY);
        //TODO move api fetch to separate service

        marvelApiService.populateCharactersRecyclerViewAdapter(
                offset,
                response -> charactersRecyclerViewAdapter.setCharacterItems(response),
                Throwable::printStackTrace,
                Functions.EMPTY_ACTION);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.characters_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.google_blue, R.color.google_green, R.color.google_red, R.color.google_yellow);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            offset += DEFAULT_OFFSET;
            marvelApiService.populateCharactersRecyclerViewAdapter(
                    offset,
                    response -> charactersRecyclerViewAdapter.setCharacterItems(response),
                    Throwable::printStackTrace,
                    () -> swipeRefreshLayout.setRefreshing(false));
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //TODO implement navigation drawer selections
        return false;
    }
}
