package by.litvin.callback;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import by.litvin.R;
import by.litvin.adapter.CharactersRecyclerViewAdapter;
import by.litvin.adapter.MoveAndSwipeListener;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private MoveAndSwipeListener moveAndSwipeListener;
    private Context context;

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
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
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
        Bitmap icon;
        boolean isSwipingRight = dX > 0;


        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            float height = (float) itemView.getBottom() - (float) itemView.getTop();
            float iconWidth = height / 3;

            if (isSwipingRight) {
                paint.setColor(ContextCompat.getColor(context, R.color.favorite_character_color));
                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                canvas.drawRect(background, paint);
                icon = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_menu_search);
                Log.i("TEST", String.format("Left %s, Right %s, Top %s, Bottom %s", itemView.getLeft(), itemView.getRight(), itemView.getTop(), itemView.getBottom()));
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
}
