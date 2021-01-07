package com.ljkj.myapplication.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.ljkj.myapplication.R;
import com.ljkj.myapplication.bean.ChartDataBean;
import com.ljkj.myapplication.view.EditTextChangeListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名：CurveEditorAdapter
 * 作者：Turbo
 * 时间： 12/29/20 10:35 AM
 * 蚁穴虽小，溃之千里。
 */
public class CurveEditorRVAdapter extends BaseRecyclerAdapter<ChartDataBean> {
    public static boolean isTouch = false;

    private Context mContext;

    public CurveEditorRVAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.horizontal_list_item, parent, false);
        return new CurveEditorRVHolder(view);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int position, ChartDataBean data) {
        CurveEditorRVHolder holder = (CurveEditorRVHolder) viewHolder;
        holder.etContent.setText(data.getValue() + "");

        if (holder.etContent.getTag() instanceof TextWatcher) {
            holder.etContent.removeTextChangedListener((TextWatcher) holder.etContent.getTag());
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mEditTextChangeListener != null) {
                    String content = holder.etContent.getText().toString().trim();
                    if ("".equals(content)) {
                        Toast.makeText(mContext, "数值不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if (isTouch) return;
                        mDatas.get(position).setValue(Integer.parseInt(content));
                        mEditTextChangeListener.onChange(0, 0);
                    }
                }
            }
        };

        holder.etContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //只有获取到焦点时才添加监听器
                if (hasFocus) {
                    holder.etContent.addTextChangedListener(textWatcher);
                    holder.etContent.setTag(textWatcher);
                }
            }
        });
    }

    private EditTextChangeListener mEditTextChangeListener;

    public void setEditTextChangeListener(EditTextChangeListener editTextChangeListener) {
        mEditTextChangeListener = editTextChangeListener;
    }

    class CurveEditorRVHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.et_content)
        EditText etContent;

        public CurveEditorRVHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
