package com.ljkj.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.ljkj.myapplication.R;
import com.ljkj.myapplication.bean.ChartDataBean;
import com.ljkj.myapplication.utils.Utils;
import com.ljkj.myapplication.view.SeekbarTouchChangeListener;
import com.ljkj.myapplication.view.TouchChangeListener;
import com.ljkj.myapplication.view.VerticalSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名：CurveEditorAdapter
 * 作者：Turbo
 * 时间： 12/29/20 10:35 AM
 * 蚁穴虽小，溃之千里。
 */
public class CurveEditorControlRVAdapter extends BaseRecyclerAdapter<ChartDataBean> {

    private Context mContext;

    private int margin;

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public CurveEditorControlRVAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.horizontal_control_item, parent, false);
        return new CurveEditorRVHolder(view);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, ChartDataBean data) {
        CurveEditorRVHolder holder = (CurveEditorRVHolder) viewHolder;
        holder.setIsRecyclable(false);
        holder.vsbContent.setProgress((int) (data.getValue() * 1.0f / max * 200));

        if (data.isFromUserTouch()) {
            holder.vsbContent.setThumb(mContext.getResources().getDrawable(R.drawable.seekbar_thumb));
        } else {
            holder.vsbContent.setThumb(mContext.getResources().getDrawable(R.drawable.seekbar_thumbed));
        }

//        holder.vsbContent.setProgress(1);
        holder.vsbContent.setTouchChangeListener(new TouchChangeListener() {
            @Override
            public void onTouchChange(int motionEvent) {
                switch (motionEvent) {
                    case MotionEvent.ACTION_DOWN:
                        CurveEditorRVAdapter.isTouch = true;
                    {
                        int height = holder.rl_parent.getBottom() - holder.rl_parent.getTop();
                        int bottomMargin = margin - Utils.dp2px(mContext, 10.5f) + Utils.dp2px(mContext, 0.5f);
                        int topMargin = (int) ((height - bottomMargin) / 21f);
                        float maxPercent = currentMax * 1.0f / (max - min);
                        float minPercent = currentMin * 1.0f / (max - min);
                        int top = (int) ((height - bottomMargin - topMargin) * (1 - maxPercent) + topMargin);
                        int bottom = (int) ((height - bottomMargin - topMargin) * minPercent + bottomMargin);
                        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) holder.viewBg.getLayoutParams();
                        layoutParams2.setMargins(0, top, 0, bottom);
                        holder.viewBg.setLayoutParams(layoutParams2);
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.vsbContent.getLayoutParams();
                        layoutParams.setMargins(0, top - 20, 0, bottom - 20);
                        holder.vsbContent.setLayoutParams(layoutParams);
                    }
                    holder.text.setVisibility(View.VISIBLE);
                    holder.viewBg.setVisibility(View.VISIBLE);
                    holder.vsbContent.setThumb(mContext.getResources().getDrawable(R.drawable.seekbar_thumb));
                    break;
                    case MotionEvent.ACTION_MOVE:
//                        if (mSeekbarTouchChangeListener != null) {
//                            mSeekbarTouchChangeListener.onTouchChange(position, holder.vsbContent.getProgress());
//                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        CurveEditorRVAdapter.isTouch = false;
                        holder.viewBg.setVisibility(View.GONE);
                        holder.text.setVisibility(View.GONE);
//                        if (mSeekbarTouchChangeListener != null) {
//                            mDatas.get(position).setValue(holder.vsbContent.getProgress());
//                            mSeekbarTouchChangeListener.onTouchChange(position, holder.vsbContent.getProgress());
//                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        holder.viewBg.setVisibility(View.GONE);
                        holder.text.setVisibility(View.GONE);
                        CurveEditorRVAdapter.isTouch = false;
//                        holder.viewBg.setVisibility(View.GONE);
                        break;
                }
            }
        });
        holder.vsbContent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mSeekbarTouchChangeListener != null) {
                    int value = (int) (progress / 200f * (currentMax - currentMin)) + currentMin;
                    mDatas.get(position).setValue(value);
                    holder.text.setText(value + "");
                    refreshMargin(holder);
                    mDatas.get(position).setFromUserTouch(true);
                    mSeekbarTouchChangeListener.onTouchChange(position, holder.vsbContent.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                holder.text.setVisibility(View.GONE);
                CurveEditorRVAdapter.isTouch = false;
            }
        });
        refreshMargin(holder);
        if (margin != 0) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.vsbContent.getLayoutParams();
            layoutParams.setMargins(0, Utils.dp2px(mContext, 2f), 0, margin - Utils.dp2px(mContext, 10.5f) - 20);
            holder.vsbContent.setLayoutParams(layoutParams);
        }
    }

    public void refreshMargin(CurveEditorRVHolder holder) {

        int height2 = holder.rl_parent.getBottom() - holder.rl_parent.getTop();
        int bottomMargin = margin - Utils.dp2px(mContext, 10.5f) + Utils.dp2px(mContext, 0.5f);
        int topMargin = (int) ((height2 - bottomMargin) / 21f);
        float maxPercent = currentMax * 1.0f / (max - min);
        int top = (int) ((height2 - bottomMargin - topMargin) * (1 - maxPercent) + topMargin);

        int height = (holder.vsbContent.getBottom() - holder.vsbContent.getTop() - 40);
        int progress = holder.vsbContent.getProgress();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.text.getLayoutParams();
        int tvHeight = holder.text.getBottom() - holder.text.getTop();
        int margin = (int) (height * 1.0f / 200f * (200 - progress) - tvHeight + top - 30);
//        Log.e("------>>","-------"+layoutParams.height+"-------"+height+"------****"+margin);
//        if (margin < 0) {
//            margin = (int) (height * 1.0f / 200f * (200 - progress) + layoutParams.height + Utils.dp2px(mContext, 4));
//        }
        layoutParams.setMargins(0, margin, 0, 0);
        holder.text.setLayoutParams(layoutParams);
    }

    public int max = 200, min = 0;
    public int currentMax = 1800, currentMin = 400;
    private SeekbarTouchChangeListener mSeekbarTouchChangeListener;

    public void setMaxMin(int max, int min) {
        this.max = max;
        this.min = min;
        currentMax = (int) (max * 0.75);
        currentMin = (int) (max * 0.11);
    }

    public void setSeekbarTouchChangeListener(SeekbarTouchChangeListener seekbarTouchChangeListener) {
        mSeekbarTouchChangeListener = seekbarTouchChangeListener;
    }

    class CurveEditorRVHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_bg)
        View viewBg;
        @BindView(R.id.text)
        TextView text;
        @BindView(R.id.rl_parent)
        RelativeLayout rl_parent;
        @BindView(R.id.vsb_content)
        VerticalSeekBar vsbContent;

        public CurveEditorRVHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
