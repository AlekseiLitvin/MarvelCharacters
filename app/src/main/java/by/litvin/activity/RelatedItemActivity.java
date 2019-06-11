package by.litvin.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import by.litvin.R;
import by.litvin.adapter.RelatedItemPagerAdapter;
import by.litvin.adapter.RelatedItemRecyclerViewAdapter;
import by.litvin.model.RelatedItem;

public class RelatedItemActivity extends AppCompatActivity {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_item);

        int position = getIntent().getIntExtra(RelatedItemRecyclerViewAdapter.POSITION, 0);
        ArrayList<RelatedItem> relatedItems =
                getIntent().getParcelableArrayListExtra(RelatedItemRecyclerViewAdapter.RELATED_ITEMS);


        viewPager = findViewById(R.id.related_item_view_pager);
        viewPager.setAdapter(new RelatedItemPagerAdapter(this, relatedItems));
        viewPager.setCurrentItem(position);
    }

    public void closeRelatedItemBig(View view) {
        //TODO implement close button
    }
}
