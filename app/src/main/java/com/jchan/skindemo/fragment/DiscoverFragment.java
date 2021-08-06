package com.jchan.skindemo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jchan.skindemo.R;

/**
 * @create time: 2021/8/6
 * @author: JChan
 * @description:
 */
public class DiscoverFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover,container,false);
        return view;
    }
}
