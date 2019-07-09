package by.litvin.listeners;

public interface MoveAndSwipeListener {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemSwipe(int position);

}
