package com.cong.cly.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cong.cly.R;

import java.util.List;

/**
 * Created by cong on 16/1/21.
 */
public class SortAdapter extends RecyclerView.Adapter<SortAdapter.ViewHolder> {
    private List<SortModel> data;

    public SortAdapter(List<SortModel> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_leaf, parent, false);
            return new ViewHolder(view, viewType);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_azgroup, parent, false);
            return new ViewHolder(view, viewType);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SortModel model = getItemAtPosition(position);
        if (model != null) {
            if (model.isLeaf) {
                holder.nameTv.setText(model.name);
            } else {
                holder.azTv.setText(model.az);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private SortModel getItemAtPosition(int position) {
        if (data != null)
            return data.get(position);
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        SortModel model = getItemAtPosition(position);
        if (model != null) {
            return model.isLeaf ? 1 : 2;
        }
        return 1;
    }

    public int getPositionByItem(String az) {
        if (!TextUtils.isEmpty(az) && data != null) {
            for (int i = 0; i < data.size(); i++) {
                SortModel sm = data.get(i);
                if (!sm.isLeaf && az.equals(sm.az)) {
                    return i;
                }
            }
        }
        return -1;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView azTv;

        public ViewHolder(View v, int viewType) {
            super(v);
            if (viewType == 1) {
                nameTv = (TextView) v.findViewById(R.id.name_tv);
            } else {
                azTv = (TextView) v.findViewById(R.id.az_tv);
            }
        }
    }
}
