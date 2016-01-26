package me.dielsonsales.app.openpomodoro.presentation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.dielsonsales.app.openpomodoro.R;

public class ScreenSlidePageFragment extends Fragment {

    private int mPageNumber;
    private static String ARG_PAGE_NUMBER = "page_number";

    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(ARG_PAGE_NUMBER, pageNumber);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE_NUMBER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_screen_slide_1, container, false);
    }
}
