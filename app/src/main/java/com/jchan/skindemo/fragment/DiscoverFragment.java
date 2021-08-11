package com.jchan.skindemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jchan.skindemo.R;
import com.jchan.skindemo.SecondActivity;

/**
 * @create time: 2021/8/6
 * @author: JChan
 * @description:
 */
public class DiscoverFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover,container,false);
        view.findViewById(R.id.tv_discover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SecondActivity.class));
            }
        });
        return view;
    }
}
