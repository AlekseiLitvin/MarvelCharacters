package by.litvin.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

    //TODO inject all CharacterViewModel using Dagger
    private CharacterViewModel characterViewModel;
    private RecyclerView recyclerView;
    private boolean isVibrationEventOccurred = false;
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

        //Remove copy-paste similar to ItemTouchHelperCallback#onChildDraw
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                Paint paint = new Paint();
                View itemView = viewHolder.itemView;
                boolean isSwipingRight = dX > 0;

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    initVibrationEvent(dX, itemView);
                    float iconWidth = (float) itemView.getHeight() / 3;
                    if (isSwipingRight) {
                        paint.setColor(ContextCompat.getColor(FavCharacterActivity.this, R.color.favorite_character_color)); //TODO change color when functionality will be defined
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                        canvas.drawRect(background, paint);
                        RectF iconLocation = new RectF(
                                (float) itemView.getLeft() + iconWidth,
                                (float) itemView.getTop() + iconWidth,
                                (float) itemView.getLeft() + 2 * iconWidth,
                                (float) itemView.getBottom() - iconWidth);
                        Bitmap favouriteIcon = addCharacterToFavourite(dX, itemView, viewHolder);
                        canvas.drawBitmap(favouriteIcon, null, iconLocation, paint);
                    }
                }
                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO implement revert
                Character character = favCharactersRecyclerViewAdapter.getCharacterAt(viewHolder.getAdapterPosition());
                characterViewModel.delete(character);
                //TODO implement revert using  transaction
                Snackbar.make(recyclerView.getRootView(), "Character has been deleted", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", view -> characterViewModel.insert(character))
                        .show();
            }
        }).attachToRecyclerView(recyclerView);
    }

    private Bitmap addCharacterToFavourite(float dX, View itemView, RecyclerView.ViewHolder viewHolder) {
        float favouriteEventTriggerLength = (float) itemView.getWidth() / 3;
        if (dX > favouriteEventTriggerLength) {
            return BitmapFactory.decodeResource(getResources(), android.R.drawable.star_big_on);
        }
        return BitmapFactory.decodeResource(getResources(), android.R.drawable.star_big_off);
    }

    private void initVibrationEvent(float dX, View itemView) {
        float vibrationTriggerLength = (float) itemView.getWidth() / 3;
        if (dX > vibrationTriggerLength && !isVibrationEventOccurred) {
            isVibrationEventOccurred = true;
            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(30); //TODO move to properties file
        }
        if (dX == 0) {
            isVibrationEventOccurred = false;
        }
    }
}
