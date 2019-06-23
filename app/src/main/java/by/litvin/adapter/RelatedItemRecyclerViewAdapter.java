package by.litvin.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import by.litvin.R;
import by.litvin.activity.RelatedItemActivity;
import by.litvin.databinding.RelatedEntitiesRecyclerItemBinding;
import by.litvin.model.RelatedItem;

//TODO refactor for series?
public class RelatedItemRecyclerViewAdapter extends RecyclerView.Adapter<RelatedItemRecyclerViewAdapter.RelatedItemViewHolder> {

    public static final String POSITION = "Position";
    public static final String RELATED_ITEMS = "Related item";

    //TODO change name and type
    private ArrayList<RelatedItem> relatedItems = new ArrayList<>();
    private Context context;

    public RelatedItemRecyclerViewAdapter(Context context) {
        this.context = context;
    }

    public void addRelatedItems(List<RelatedItem> comics) {
        this.relatedItems.addAll(comics);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RelatedItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RelatedEntitiesRecyclerItemBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.related_entities_recycler_item, parent, false);
        return new RelatedItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedItemViewHolder viewHolder, int position) {
        viewHolder.bind(relatedItems.get(position), position);
        ImageView relatedItemImage = viewHolder.binding.relatedItemImage;

        viewHolder.binding.getRoot().setOnClickListener(view -> {
            Intent intent = new Intent(context, RelatedItemActivity.class);
            intent.putExtra(POSITION, position);
            intent.putParcelableArrayListExtra(RELATED_ITEMS, relatedItems);
            context.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                    relatedItemImage,
                    ViewCompat.getTransitionName(relatedItemImage)).toBundle());
        });
    }

    @Override
    public int getItemCount() {
        return relatedItems.size();
    }

    public class RelatedItemViewHolder extends RecyclerView.ViewHolder {
        private RelatedEntitiesRecyclerItemBinding binding;

        public RelatedItemViewHolder(RelatedEntitiesRecyclerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(RelatedItem relatedItem, int position) {
            binding.setRelatedItem(relatedItem);
            binding.setPosition(position);
            binding.executePendingBindings();
        }
    }
}
