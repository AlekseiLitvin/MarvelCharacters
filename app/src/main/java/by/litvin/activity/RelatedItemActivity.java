package by.litvin.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import by.litvin.R;
import by.litvin.adapter.RelatedItemPagerAdapter;
import by.litvin.adapter.RelatedItemRecyclerViewAdapter;
import by.litvin.model.RelatedItem;

public class RelatedItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_item);

        int position = getIntent().getIntExtra(RelatedItemRecyclerViewAdapter.POSITION, 0);
        ArrayList<RelatedItem> relatedItems =
                getIntent().getParcelableArrayListExtra(RelatedItemRecyclerViewAdapter.RELATED_ITEMS);


        ViewPager viewPager = findViewById(R.id.related_item_view_pager);
        viewPager.setAdapter(new RelatedItemPagerAdapter(relatedItems));
        viewPager.setCurrentItem(position);
    }

    public void closeBigRelatedItem(View view) {
        onBackPressed();
    }
}
