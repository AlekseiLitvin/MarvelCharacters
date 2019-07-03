package by.litvin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

import by.litvin.R;
import by.litvin.databinding.RelatedItemBigBinding;
import by.litvin.model.RelatedItem;

public class RelatedItemPagerAdapter extends PagerAdapter {

    private List<RelatedItem> relatedItems;

    public RelatedItemPagerAdapter(List<RelatedItem> relatedItems) {
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
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        RelatedItemBigBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.related_item_big, container, false);

        binding.setPosition(position);
        binding.setRelatedItem(relatedItems.get(position));

        container.addView(binding.getRoot());
        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }
}
