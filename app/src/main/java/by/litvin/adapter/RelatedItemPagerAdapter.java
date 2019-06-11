package by.litvin.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import by.litvin.R;
import by.litvin.model.Image;
import by.litvin.model.RelatedItem;

public class RelatedItemPagerAdapter extends PagerAdapter {

    private Context context;
    private List<RelatedItem> relatedItems;

    public RelatedItemPagerAdapter(Context context, List<RelatedItem> relatedItems) {
        this.context = context;
        this.relatedItems = relatedItems;
    }

    @Override
    public int getCount() {
        return relatedItems.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LinearLayout linearLayout =
                (LinearLayout) LayoutInflater.from(context).inflate(R.layout.related_item_big, container, false);

        RelatedItem relatedItem = relatedItems.get(position);

        TextView bigRelatedItemTitle = linearLayout.findViewById(R.id.big_related_item_title);
        bigRelatedItemTitle.setText(relatedItem.getTitle());

        Image characterThumbnail = relatedItem.getThumbnail();
        String imageUrl = String.format("%s.%s", characterThumbnail.getPath(), characterThumbnail.getExtension());
        ImageView bigRelatedItemImage = linearLayout.findViewById(R.id.big_related_item_image);
        Glide.with(context)
                .load(imageUrl)
                .into(bigRelatedItemImage);

        container.addView(linearLayout);
        return linearLayout;
    }
}
