package com.jchan.skindemo.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jchan.skindemo.R;
import com.jchan.skindemo.databinding.DialogSelectSkinBinding;
import com.jchan.skindemo.databinding.ItemSkinBinding;
import com.jchan.skindemo.entity.Skin;
import com.jchan.skinlib.manager.SkinManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @create time: 2021/8/6
 * @author: JChan
 * @description:
 */
public class SelectSkinDialog extends DialogFragment {
    private DialogSelectSkinBinding mBinding;
    private List<Skin> mSkinList = new ArrayList<>();
    private SelectAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_select_skin, container, false);
        mBinding = DataBindingUtil.bind(view);
        initData();
        initView();
        return view;
    }

    private void initData() {
        mSkinList.add(new Skin("主题1",null,R.color.color_d4237a));
        mSkinList.add(new Skin("主题2","/sdcard/skin_1.apk",R.color.color_1afa29));
        mSkinList.add(new Skin("主题3","/sdcard/skin.apk",R.color.color_1296db));
    }

    private void initView() {
        mAdapter = new SelectAdapter();
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        mBinding.recyclerView.setLayoutManager(manager);
        mBinding.recyclerView.setAdapter(mAdapter);

    }

    class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_skin, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SelectSkinDialog.SelectAdapter.ViewHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return mSkinList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private ItemSkinBinding binding;

            public ViewHolder(View itemView) {
                super(itemView);
                binding = DataBindingUtil.bind(itemView);
            }

            public void bind(int position) {
                Skin skin = mSkinList.get(position);
                binding.tvTitle.setText(skin.getTitle());
                binding.ivBg.setImageResource(skin.getResId());
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SkinManager.getInstance().loadSkin(skin.getPath());
                        dismiss();
                    }
                });
            }
        }
    }
}
