package com.ljkj.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.ljkj.myapplication.R;
import com.ljkj.myapplication.bean.ChartDataBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名：CurveEditorAdapter
 * 作者：Turbo
 * 时间： 12/29/20 10:35 AM
 * 蚁穴虽小，溃之千里。
 */
public class CurveEditorRVSAdapter extends BaseRecyclerAdapter<ChartDataBean> {

    private Context mContext;

    public CurveEditorRVSAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.horizontal_list_s_item, parent, false);
        return new CurveEditorRVHolder(view);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, ChartDataBean data) {
        CurveEditorRVHolder holder = (CurveEditorRVHolder) viewHolder;
        holder.tvContent.setText(data.getValue() + "");
    }

    class CurveEditorRVHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_content)
        TextView tvContent;

        public CurveEditorRVHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
