package com.xmstr.iloveeggs.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xmstr.iloveeggs.R;

/**
 * Created by Xmstr.
 */

public class InfoFragment extends Fragment {

    public static final String TAG = "tag:info";

    private static final String ARG_TEXT = "arg_text";


    public static InfoFragment newInstance(String name) {
        InfoFragment frag = new InfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, name);
        frag.setArguments(args);
        return frag;
    }

    public InfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.info_fragmet_layout, container, false);
        return rootView;

    }
}
