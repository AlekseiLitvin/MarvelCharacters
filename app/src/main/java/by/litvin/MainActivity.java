package by.litvin;

import android.content.Intent;
import android.os.Bundle;
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

import javax.inject.Inject;

import by.litvin.activity.FavCharacterActivity;
import by.litvin.adapter.CharactersRecyclerViewAdapter;
import by.litvin.callback.ItemTouchHelperCallback;
import by.litvin.di.component.DaggerMarvelApiServiceComponent;
import by.litvin.model.Character;
import by.litvin.service.MarvelApiService;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.functions.Functions;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //TODO move properties to separate file
    private static final int DEFAULT_OFFSET = 10;
    private int offset = 0;
    private boolean isLoading = false;

    private DrawerLayout drawerLayout;

    @Inject
    CharactersRecyclerViewAdapter charactersRecyclerViewAdapter;
    @Inject
    MarvelApiService marvelApiService; //TODO Inject using dagger

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            //TODO add condition to load characters erliers (eg +3 or +4 )
            if (!isLoading && linearLayoutManager.getItemCount() == (linearLayoutManager.findLastVisibleItemPosition() + 1)) {
                isLoading = true;
                offset += DEFAULT_OFFSET;
                charactersRecyclerViewAdapter.addNullDataForProgressBar();

                Consumer<List<Character>> onNext = response -> {
                    charactersRecyclerViewAdapter.removeNullDataForProgressBar();
                    charactersRecyclerViewAdapter.setCharacterItems(response);
                };
                marvelApiService.populateCharactersRecyclerView(
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

        DaggerMainActivityComponent.builder()
                .marvelApiServiceComponent(DaggerMarvelApiServiceComponent.create())
                .build()
                .injectMainActivity(this);

        RecyclerView recyclerView = findViewById(R.id.characters_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(charactersRecyclerViewAdapter);
        recyclerView.addOnScrollListener(scrollListener);

        ItemTouchHelper.Callback itemTouchHelperCallback = new ItemTouchHelperCallback(charactersRecyclerViewAdapter, this);
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
                offset,
                response -> charactersRecyclerViewAdapter.setCharacterItems(response),
                Throwable::printStackTrace,
                Functions.EMPTY_ACTION);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.characters_swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.google_blue, R.color.google_green, R.color.google_red, R.color.google_yellow);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            offset += DEFAULT_OFFSET;
            marvelApiService.populateCharactersRecyclerView(
                    offset,
                    response -> charactersRecyclerViewAdapter.setCharacterItems(response),
                    Throwable::printStackTrace,
                    () -> swipeRefreshLayout.setRefreshing(false));
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.favorite_characters:
                Intent intent = new Intent(this, FavCharacterActivity.class);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
