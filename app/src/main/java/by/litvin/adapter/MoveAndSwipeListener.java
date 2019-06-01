package by.litvin.adapter;

public interface MoveAndSwipeListener {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemSwipe(int position); //TODO change method name, not only remove will be available

}
