package by.litvin.callback;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Vibrator;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import by.litvin.R;
import by.litvin.adapter.CharactersRecyclerViewAdapter;
import by.litvin.listeners.MoveAndSwipeListener;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private MoveAndSwipeListener moveAndSwipeListener;
    private Context context;
    private boolean isVibrationEventOccurred = false;

    public ItemTouchHelperCallback(MoveAndSwipeListener moveAndSwipeListener, Context context) {
        this.moveAndSwipeListener = moveAndSwipeListener;
        this.context = context;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            if (viewHolder.getItemViewType() == CharactersRecyclerViewAdapter.TYPE_NORMAL) {
                final int dragFlags = 0; //TODO implement drag?
                final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        }
        return 0;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        //TODO think if move up and down will be supported
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
        Paint paint = new Paint();
        View itemView = viewHolder.itemView;
        Bitmap icon;
        boolean isSwipingRight = dX > 0;

        initVibrationEvent(dX, itemView);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float iconWidth = (float) itemView.getHeight() / 3;
            if (isSwipingRight) {
                paint.setColor(ContextCompat.getColor(context, R.color.favorite_character_color));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                canvas.drawRect(background, paint);
                icon = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_menu_search);
                RectF iconLocation = new RectF(
                        (float) itemView.getLeft() + iconWidth,
                        (float) itemView.getTop() + iconWidth,
                        (float) itemView.getLeft() + 2 * iconWidth,
                        (float) itemView.getBottom() - iconWidth);
                canvas.drawBitmap(icon, null, iconLocation, paint);
            } else {
                paint.setColor(ContextCompat.getColor(context, R.color.TBD_color)); //TODO change color when functionality will be defined
                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                canvas.drawRect(background, paint);
                icon = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.star_big_on);
                RectF iconLocation = new RectF(
                        (float) itemView.getRight() - 2 * iconWidth,
                        (float) itemView.getTop() + iconWidth,
                        (float) itemView.getRight() - iconWidth,
                        (float) itemView.getBottom() - iconWidth);
                canvas.drawBitmap(icon, null, iconLocation, paint);
            }
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    private void initVibrationEvent(float dX, View itemView) {
        float vibrationTriggerLength = (float) itemView.getWidth() / 3;
        if (dX > vibrationTriggerLength && !isVibrationEventOccurred) {
            isVibrationEventOccurred = true;
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(30); //TODO move to properties file
        }
        if (dX == 0) {
            isVibrationEventOccurred = false;
        }
    }
}
