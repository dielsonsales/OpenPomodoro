package me.dielsonsales.app.openpomodoro.presentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import me.dielsonsales.app.openpomodoro.R;

public class HelpActivity extends FragmentActivity {

    private static final int NUM_PAGES = 3;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private Button mPreviousButton;
    private Button mNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new HelpFragmentAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("PagerView", "onPageSelected: " + position);
                if (position == 0) {
                    mPreviousButton.setVisibility(View.GONE);
                } else if (position == NUM_PAGES - 1) {
                    mNextButton.setVisibility(View.GONE);
                } else {
                    mPreviousButton.setVisibility(View.VISIBLE);
                    mNextButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mPreviousButton = (Button) findViewById(R.id.button_previous);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToPreviousPage();
            }
        });

        mNextButton = (Button) findViewById(R.id.button_next);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToNextPage();
            }
        });
    }

    /**
     * If the PageView isn't displaying the first element, slide back until the
     * first, then closes the activity.
     */
    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            scrollToPreviousPage();
        }
    }

    private void scrollToPreviousPage() {
        int currentPage = mPager.getCurrentItem();
        if (currentPage > 0) {
            mPager.setCurrentItem(currentPage - 1);
        }
    }

    private void scrollToNextPage() {
        int currentPage = mPager.getCurrentItem();
        if (currentPage < 3) {
            mPager.setCurrentItem(currentPage + 1);
        }
    }

    public static class HelpFragmentAdapter extends FragmentStatePagerAdapter {
        public HelpFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
