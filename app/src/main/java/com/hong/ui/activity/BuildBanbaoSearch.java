package com.hong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;
import cn.addapp.pickers.picker.TimePicker;
import cn.addapp.pickers.picker.TimePicker.OnTimePickListener;
import com.hong.AppData;
import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerActivityComponent;
import com.hong.inject.module.ActivityModule;
import com.hong.mvp.contract.IBuildBanbaoSearchContract.View;
import com.hong.mvp.model.GxrbModel;
import com.hong.mvp.presenter.BuildBanbaoSearchPresenter;
import com.hong.ui.activity.base.BaseActivity;
import com.hong.util.CalendarUtil;
import com.hong.util.ToastUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BuildBanbaoSearch extends BaseActivity<BuildBanbaoSearchPresenter> implements View, OnClickListener, OnLongClickListener {
    private static final String TAG = "=========";
    @BindView(R.id.bblxEt)
    TextView bblxEt;
    @BindView(R.id.bcEt)
    TextView bcEt;
    @BindView(R.id.btn_ok)
    Button btnOk;
    String did = "";
    @BindView(R.id.jhEt)
    TextView jhEt;

    private String reportTime,orderClasses;

    /* access modifiers changed from: protected */
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).activityModule(new ActivityModule(getActivity())).build().inject(this);
    }

    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: _____");
        initView();
        initData();
    }

    private void initData() {
        ((BuildBanbaoSearchPresenter) this.mPresenter).getWell(AppData.INSTANCE.getLoggedUser().getOilfield());
    }

    private void initView() {
        this.bcEt.setOnClickListener(this);
        this.bblxEt.setOnClickListener(this);
        this.jhEt.setOnClickListener(this);
        this.btnOk.setOnClickListener(this);
        Log.i(TAG, "initView: 点击事件初始化完成");
    }

    public void onClick(android.view.View v) {
        String str = "补八点班";
        String str2 = "补零点班";
        String str3 = "补四点班";
        String str4 = "今四点班";
        SinglePicker sin2;
        switch (v.getId()) {
            case R.id.bblxEt /*2131296316*/:
                sin2 = new SinglePicker(this, new String[]{"修井", "试油", "试油(抽汲排液)", "试油(氮气排液)", "试油(连续油管氮气排液)", "试油(自喷或放喷排液)", "试油(螺杆泵排液)", "试油(射流泵排液)"});
                sin2.setItemWidth(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                sin2.setCanLoop(false);
                sin2.setOnItemPickListener(new OnItemPickListener() {
                    public void onItemPicked(int i, Object o) {
                        String str = "";
                        if (o.equals("修井")) {
                            TextView textView = BuildBanbaoSearch.this.bblxEt;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(o);
                            stringBuilder.append(str);
                            textView.setText(stringBuilder.toString());
                            return;
                        }
                        BuildBanbaoSearch.this.bblxEt.setText(str);
                        ToastUtils.show("试油暂不可用!");
                    }
                });
                sin2.show();
                break;
            case R.id.bcEt /*2131296318*/:
                sin2 = null;
                int hours = new Date().getHours();
                if (hours < 18) {
                    sin2 = new SinglePicker(this, new String[]{"昨四点班", "零点班", "八点班", "补四点班", "补零点班", "补八点班"});
                }
                if (hours >= 16 && hours <= 23) {
                    sin2 = new SinglePicker(this, new String[]{str4, str3, str2, str});
                }
                sin2.setCanLoop(false);
                sin2.setItemWidth(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                sin2.setOnItemPickListener(new OnItemPickListener() {
                    public void onItemPicked(int i, Object o) {
                        TextView textView = BuildBanbaoSearch.this.bcEt;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(o);
                        stringBuilder.append("");
                        textView.setText(stringBuilder.toString());
                    }
                });
                sin2.show();
                break;
            case R.id.btn_ok /*2131296323*/:
                String cl = bcEt.getText()+"";
                Log.i(TAG, "onClick: _________________"+cl);
                String date = "";
                if (cl.equals("零点班") || cl.equals("八点班")) {
                    str = getDate(0);
                } else if (cl.equals(str4)) {
                    cl = "四";
                    str = getDate(0);
                } else if (cl.equals(str3) || cl.equals("昨四点班")) {
                    cl = "四";
                    str = getDate(1);
                } else if (cl.equals(str)) {
                    cl = "八";
                    str = getDate(1);
                } else if (cl.equals(str2)) {
                    cl = "零";
                    str = getDate(1);
                } else {
                    ToastUtils.show("没有该班次!");
                    return;
                }
                reportTime = str;
                orderClasses = cl;
                ((BuildBanbaoSearchPresenter) this.mPresenter).isUpload(this.did, cl, str);
                break;
            case R.id.jhEt /*2131296397*/:
                ArrayList<GxrbModel> list = AppData.searchWells;
                Object[] array = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    array[i] = ((GxrbModel) list.get(i)).getWellCommonName();
                }
                SinglePicker sin = new SinglePicker(this, array);
                sin.setItemWidth(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                sin.setCanLoop(false);
                sin.setOnItemPickListener(new OnItemPickListener() {
                    public void onItemPicked(int i, Object o) {
                        BuildBanbaoSearch.this.did = ((GxrbModel) AppData.searchWells.get(i)).getDid();
                        TextView textView = BuildBanbaoSearch.this.jhEt;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(o);
                        stringBuilder.append("");
                        textView.setText(stringBuilder.toString());
                    }
                });
                sin.show();
                break;
        }
    }

    private String getDate(int date) {
        String month2;
        Date date1 = new Date(new Date().getTime() - ((long) ((((date * 24) * 60) * 60) * 1000)));
        String year = new SimpleDateFormat("yyyy").format(date1);
        String sep = "-";
        int month = date1.getMonth() + 1;
        String month22 = "";
        String day2 = "";
        int day = date1.getDate();
        String str = "0";
        String str2 = "";
        if (month < 1 || month > 9) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(month);
            stringBuilder.append(str2);
            month2 = stringBuilder.toString();
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(month);
            month2 = stringBuilder.toString();
        }
        if (day < 0 || day > 9) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(day);
            stringBuilder2.append(str2);
            month22 = stringBuilder2.toString();
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(day);
            month22 = stringBuilder2.toString();
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(year);
        stringBuilder3.append(sep);
        stringBuilder3.append(month2);
        stringBuilder3.append(sep);
        stringBuilder3.append(month22);
        return stringBuilder3.toString();
    }

    private void showTimePicker(final TextView et) {
        TimePicker timePicker1 = new TimePicker(this);
        timePicker1.setActionButtonTop(false);
        timePicker1.setTitleText((CharSequence) "请选择");
        timePicker1.setRangeStart(CalendarUtil.getHour(new Date()), CalendarUtil.getMinute(new Date()));
        timePicker1.setSelectedItem(CalendarUtil.getHour(new Date()), CalendarUtil.getMinute(new Date()));
        timePicker1.setWeightEnable(true);
        timePicker1.setWheelModeEnable(true);
        LineConfig configTime = new LineConfig();
        configTime.setColor(-16776961);
        configTime.setAlpha(120);
        configTime.setVisible(true);
        timePicker1.setLineConfig(configTime);
        timePicker1.setOnTimePickListener(new OnTimePickListener() {
            public void onTimePicked(String s, String s1) {
                TextView textView = et;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(s);
                stringBuilder.append(":");
                stringBuilder.append(s1);
                textView.setText(stringBuilder.toString());
            }
        });
        timePicker1.show();
    }

    private void showTip(String data) {
    }

    /* access modifiers changed from: protected */
    public int getContentView() {
        return R.layout.activity_banbao_search;
    }

    public boolean onLongClick(android.view.View v) {
        return false;
    }

    public void isOpenActivity(int isOpen) {
        if (isOpen > 0) {
            ToastUtils.show("不能打开啊");
            return;
        }
        Intent i = new Intent(this, GxlrActivity.class);
        i.putExtra("banbaoType", this.bblxEt.getText());
        i.putExtra("did", this.did);
        i.putExtra("oilfield", AppData.INSTANCE.getLoggedUser().getOilfield());
        i.putExtra("reportTime", reportTime);
        i.putExtra("orderClasses", orderClasses);

        startActivity(i);
    }
}
