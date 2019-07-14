package by.litvin.callback;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import by.litvin.R;
import by.litvin.activity.PreferencesActivity;
import by.litvin.adapter.CharactersRecyclerViewAdapter;
import by.litvin.listeners.MoveAndSwipeListener;
import by.litvin.model.Character;
import by.litvin.viewmodel.CharacterViewModel;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private MoveAndSwipeListener moveAndSwipeListener;
    private boolean isVibrationEventOccurred = false;

    public ItemTouchHelperCallback(MoveAndSwipeListener moveAndSwipeListener) {
        this.moveAndSwipeListener = moveAndSwipeListener;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            if (viewHolder.getItemViewType() == CharactersRecyclerViewAdapter.TYPE_NORMAL) {
                return makeMovementFlags(0, ItemTouchHelper.END);
            }
        }
        return 0;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return moveAndSwipeListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        moveAndSwipeListener.onItemSwipe(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(@NonNull Canvas canvas,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY,
                            int actionState,
                            boolean isCurrentlyActive) {
        View itemView = viewHolder.itemView;
        Context context = recyclerView.getContext();
        boolean isSwipingRight = dX > 0;

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            initVibrationEvent(dX, itemView);
            int iconWidth =  itemView.getHeight() / 3;
            if (isSwipingRight) {
                ColorDrawable background = new ColorDrawable(context.getResources().getColor(R.color.backgroundColor));
                background.setBounds(0, itemView.getTop(), (int) (itemView.getLeft() + dX), itemView.getBottom());
                background.draw(canvas);

                Drawable favouriteIcon = addCharacterToFavourite(dX, itemView, viewHolder);
                favouriteIcon.setBounds(
                         itemView.getLeft() + iconWidth,
                         itemView.getTop() + iconWidth,
                         itemView.getLeft() + 2 * iconWidth,
                         itemView.getBottom() - iconWidth);
                favouriteIcon.draw(canvas);
            }
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private Drawable addCharacterToFavourite(float dX, View itemView, RecyclerView.ViewHolder viewHolder) {
        Context context = itemView.getContext();
        if (dX > getFavouriteTriggerLength(itemView)) {
            insertFavouriteCharacter(viewHolder, context);
            return ContextCompat.getDrawable(context, R.drawable.ic_star_on);
        }
        return ContextCompat.getDrawable(context, R.drawable.ic_star_off);
    }

    private void insertFavouriteCharacter(RecyclerView.ViewHolder viewHolder, Context context) {
        List<Character> characters = ((CharactersRecyclerViewAdapter) moveAndSwipeListener).getCharacters();
        Character character = characters.get(viewHolder.getAdapterPosition());
        CharacterViewModel characterViewModel = ViewModelProviders.of((FragmentActivity) context).get(CharacterViewModel.class);
        if (!character.isFavourite()) {
            characterViewModel.insert(character);
            character.setFavourite(true);
            Toast.makeText(context, context.getString(R.string.character_saved), Toast.LENGTH_SHORT).show();
        }
    }

    private void initVibrationEvent(float dX, View itemView) {
        if (dX > getFavouriteTriggerLength(itemView) && !isVibrationEventOccurred) {
            isVibrationEventOccurred = true;
            Vibrator vibrator = (Vibrator) itemView.getContext().getSystemService(Context.VIBRATOR_SERVICE);
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
            int vibrationLength = Integer.parseInt(defaultSharedPreferences.getString(PreferencesActivity.VIBRATION_LENGTH, PreferencesActivity.DEFAULT_VIBRATION_LENGTH));
            vibrator.vibrate(vibrationLength);
        }
        if (dX == 0) {
            isVibrationEventOccurred = false;
        }
    }

    private float getFavouriteTriggerLength(View itemView) {
        return (float) itemView.getWidth() / 4;
    }

}
