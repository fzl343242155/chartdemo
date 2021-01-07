package com.ljkj.myapplication.activity;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ljkj.myapplication.R;
import com.ljkj.myapplication.adapter.CurveEditorControlRVAdapter;
import com.ljkj.myapplication.adapter.CurveEditorRVAdapter;
import com.ljkj.myapplication.adapter.CurveEditorRVSAdapter;
import com.ljkj.myapplication.adapter.CurveEditorlineRVAdapter;
import com.ljkj.myapplication.bean.ChartDataBean;
import com.ljkj.myapplication.bean.KeyValueBean;
import com.ljkj.myapplication.utils.Utils;
import com.ljkj.myapplication.view.CustomLineChartView;
import com.ljkj.myapplication.view.EditTextChangeListener;
import com.ljkj.myapplication.view.SeekbarTouchChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 文件名：CurveEditorActivity
 * 作者：Turbo
 * 时间： 12/31/20 10:02 AM
 * 蚁穴虽小，溃之千里。
 */
public class CurveEditorActivity extends BaseActivity {

    @BindView(R.id.iv_undo)
    ImageView ivUndo;
    @BindView(R.id.iv_synchronous)
    ImageView ivSynchronous;
    @BindView(R.id.iv_save)
    ImageView ivSave;
    @BindView(R.id.rl_close)
    RelativeLayout rlClose;
    @BindView(R.id.hlv_content_g)
    RecyclerView hlvContentG;
    @BindView(R.id.hlv_content_s)
    RecyclerView hlvContentS;
    @BindView(R.id.chsv_content)
    HorizontalScrollView chsvContent;
    @BindView(R.id.tv_line)
    TextView tvLine;
    @BindView(R.id.tv_5)
    TextView tv5;
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.ll_y)
    LinearLayout llY;
    @BindView(R.id.hlv_content_line)
    RecyclerView hlvContentLine;

    @BindView(R.id.lcv_content_1)
    CustomLineChartView lineChartView1;
    @BindView(R.id.lcv_content_2)
    CustomLineChartView lineChartView2;
    @BindView(R.id.lcv_content_3)
    CustomLineChartView blueChartView;

    @BindView(R.id.hlv_content_control)
    RecyclerView hlvContentControl;
    @BindView(R.id.chsv_content2)
    HorizontalScrollView chsvContent2;
    @BindView(R.id.tv_line2)
    TextView tvLine2;
    @BindView(R.id.tv_51)
    TextView tv51;
    @BindView(R.id.tv_41)
    TextView tv41;
    @BindView(R.id.tv_31)
    TextView tv31;
    @BindView(R.id.tv_21)
    TextView tv21;
    @BindView(R.id.tv_11)
    TextView tv11;
    @BindView(R.id.ll_y2)
    LinearLayout llY2;
    @BindView(R.id.rl_2)
    RelativeLayout rl2;

    public int max, min;
    private CurveEditorRVAdapter mInputAdapter;
    private CurveEditorRVSAdapter mTextAdapter;
    private CurveEditorControlRVAdapter mProgressAdapter;
    private CurveEditorlineRVAdapter mLineAdapter;

    private List<ChartDataBean> mControlList;
    private List<ChartDataBean> mControlListCopy = new ArrayList<>();
    private List<ChartDataBean> datas1, datas2;
    private List<KeyValueBean> mUndoList;

    private boolean mButtonSwitch = false;
    private List<String> xData;
    private Context mContext;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        hideStatusNavigationBar(this);
        setContentView(R.layout.activity_curveeditor);
        ButterKnife.bind(this);
        mContext = this;
        initViews();
    }

    private int dpToPx(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f * (dp >= 0 ? 1 : -1));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mUndoList = new ArrayList<>();
        mInputAdapter = new CurveEditorRVAdapter(mContext);
        mTextAdapter = new CurveEditorRVSAdapter(mContext);
        mProgressAdapter = new CurveEditorControlRVAdapter(mContext);
        mLineAdapter = new CurveEditorlineRVAdapter(mContext);
        hlvContentG.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        hlvContentS.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        hlvContentControl.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        hlvContentLine.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));


        xData = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            xData.add(i+"");
        }

        int dataMax = 5348;
        int v = (int) (dataMax * 1.2);
        if (v > 100000) {
            max = (int) ((v / 50000 + 1) * 50000);
        } else if (v > 10000) {
            max = (int) ((v / 5000 + 1) * 5000);
        } else if (v > 1000) {
            max = (int) ((v / 500 + 1) * 500);
        } else if (v > 100) {
            max = (int) ((v / 50 + 1) * 50);
        } else {
            max = 100;
        }
        int each = max / 5;
        tv5.setText(each * 5 + "");
        tv51.setText(each * 5 + "");
        tv4.setText(each * 4 + "");
        tv41.setText(each * 4 + "");
        tv3.setText(each * 3 + "");
        tv31.setText(each * 3 + "");
        tv2.setText(each * 2 + "");
        tv21.setText(each * 2 + "");
        tv1.setText(each + "");
        tv11.setText(each + "");
        min = 0;
        datas1 = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            ChartDataBean chartDataBean = new ChartDataBean();
            Random r = new Random();
            chartDataBean.setValue(r.nextInt(dataMax));
            datas1.add(chartDataBean);
        }

        lineChartView1.setXData(xData);
        lineChartView1.setMaxMin(max, min);
        lineChartView1.setMaxMin(max, min);
        lineChartView1.setStepSpace(51);
        lineChartView1.setPaintType(1);
        lineChartView1.setData(datas1);
        lineChartView1.setOnBottomMarginListener(new CustomLineChartView.BottomMarginListener() {
            @Override
            public void onInit(int value) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvLine.getLayoutParams();
                layoutParams.setMargins(0, 0, Utils.dp2px(CurveEditorActivity.this, 5), value - dpToPx(10.5f));
                tvLine.setLayoutParams(layoutParams);
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) llY.getLayoutParams();
                layoutParams2.setMargins(0, 0, Utils.dp2px(CurveEditorActivity.this, 1), value - dpToPx(10.5f));
                llY.setLayoutParams(layoutParams2);
                RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) tvLine2.getLayoutParams();
                layoutParams3.setMargins(0, 0, Utils.dp2px(CurveEditorActivity.this, 5), value - dpToPx(10.5f));
                tvLine2.setLayoutParams(layoutParams3);
                RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) llY2.getLayoutParams();
                layoutParams4.setMargins(0, 0, Utils.dp2px(CurveEditorActivity.this, 1), value - dpToPx(10.5f));
                llY2.setLayoutParams(layoutParams4);
                mProgressAdapter.setMargin(value);
                mProgressAdapter.notifyDataSetChanged();
            }
        });

        datas2 = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            ChartDataBean chartDataBean = new ChartDataBean();
            Random r = new Random();
            chartDataBean.setValue(r.nextInt(dataMax));
            datas2.add(chartDataBean);
        }

        mTextAdapter.addDatas(datas2);

        lineChartView2.setMaxMin(max, min);
        lineChartView2.setStepSpace(51);
        lineChartView2.setPaintType(2);
        lineChartView2.setData(datas2);

        mControlList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            ChartDataBean chartDataBean = new ChartDataBean();
            chartDataBean.setValue(max / 2);
            mControlList.add(chartDataBean);
        }

        blueChartView.setMaxMin(max, min);
        blueChartView.setLineColor(mContext.getResources().getColor(R.color._09B1FF));
        blueChartView.setStepSpace(51);
        blueChartView.setPaintType(1);
        blueChartView.setData(mControlList);

        mProgressAdapter.addDatas(mControlList);
        List<ChartDataBean> list2 = new ArrayList<>();
        for (int i = 0; i < mControlList.size(); i++) {
            list2.add(mControlList.get(i));
        }
        mLineAdapter.addDatas(list2);

        mInputAdapter.addDatas(mControlList);

        hlvContentG.setAdapter(mInputAdapter);
        hlvContentS.setAdapter(mTextAdapter);
        hlvContentControl.setAdapter(mProgressAdapter);
        hlvContentLine.setAdapter(mLineAdapter);
        mProgressAdapter.setMaxMin(max, min);

        mProgressAdapter.setSeekbarTouchChangeListener(new SeekbarTouchChangeListener() {
            @Override
            public void onTouchChange(int index, int value) {
                mInputAdapter.addDatasAll(mProgressAdapter.getDatas());
                blueChartView.setData(mProgressAdapter.getDatas());

                ivUndo.setVisibility(View.GONE);
                ivSynchronous.setVisibility(View.VISIBLE);
            }
        });

        mInputAdapter.setEditTextChangeListener(new EditTextChangeListener() {
            @Override
            public void onChange(int index, int value) {
                blueChartView.setData(mInputAdapter.getDatas());
                mProgressAdapter.addDatasAll(mInputAdapter.getDatas());
            }
        });

        ivUndo.setVisibility(View.GONE);
        ivSynchronous.setVisibility(View.VISIBLE);
        chsvContent2.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                chsvContent.scrollTo(scrollX, scrollY);
                rl2.setVisibility(scrollX == 0 ? View.GONE : View.VISIBLE);
            }
        });
        chsvContent.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                chsvContent2.scrollTo(scrollX, scrollY);
                rl2.setVisibility(scrollX == 0 ? View.GONE : View.VISIBLE);
            }
        });
        {
            //神仙代码 注释有bug????????
            mControlListCopy = new ArrayList<>();
            mControlListCopy.addAll(blueChartView.getData());
            List<ChartDataBean> data1 = new ArrayList<>();
            data1.addAll(lineChartView1.getData());
            blueChartView.setData(data1);
            mInputAdapter.addDatasAll(data1);
            mProgressAdapter.addDatasAll(data1);
            blueChartView.setData(mControlListCopy);
            mProgressAdapter.addDatasAll(mControlListCopy);
            mInputAdapter.addDatasAll(mControlListCopy);
        }
    }

    @OnClick({R.id.iv_undo, R.id.iv_synchronous, R.id.iv_save, R.id.rl_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_undo:
                ivUndo.setVisibility(View.GONE);
                ivSynchronous.setVisibility(View.VISIBLE);
                blueChartView.setData(mControlListCopy);
                mProgressAdapter.addDatasAll(mControlListCopy);
                mInputAdapter.addDatasAll(mControlListCopy);
                break;
            case R.id.iv_synchronous:
                ivUndo.setVisibility(View.VISIBLE);
                ivSynchronous.setVisibility(View.GONE);
                mControlListCopy = new ArrayList<>();
                mControlListCopy.addAll(blueChartView.getData());
                List<ChartDataBean> data1 = new ArrayList<>();
                for (int i = 0; i < lineChartView1.getData().size(); i++) {
                    ChartDataBean bean = new ChartDataBean();
                    bean.setValue(lineChartView1.getData().get(i).getValue());
                    data1.add(bean);
                }
                blueChartView.setData(data1);
                mInputAdapter.addDatasAll(data1);
                mProgressAdapter.addDatasAll(data1);
                break;
            case R.id.iv_save:
                break;
            case R.id.rl_close:
                finish();
                break;
        }
    }

    public static void hideStatusNavigationBar(Activity activity) {
        if (Build.VERSION.SDK_INT < 16) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; //hide navigationBar
            activity.getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }
    }

}
