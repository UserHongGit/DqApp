package com.hong.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.hong.mvp.contract.IAddTxtContract.View;
import com.hong.AppData;
import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerActivityComponent;
import com.hong.inject.module.ActivityModule;
import com.hong.mvp.model.AddModel;
import com.hong.mvp.presenter.AddTxtPresenter;
import com.hong.ui.activity.base.BaseActivity;
import com.hong.util.StringUtils;
import com.hong.util.ToastUtils;
import com.hong.util.xunfei.MyRecognizerDialogListener;

import butterknife.BindView;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;
import cn.addapp.pickers.picker.TimePicker;

import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddTxtActivity  extends BaseActivity<AddTxtPresenter> implements View, OnClickListener, android.view.View.OnLongClickListener {

    private static final String TAG = AddTxtActivity.class.getSimpleName()+"-------";
    private int WHAT_SELECT_DATA = 99;

    /* access modifiers changed from: protected */
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).activityModule(new ActivityModule(getActivity())).build().inject(this);
    }


    protected int getContentView() {
        return R.layout.activity_addtxt;
    }


    //定义控件开始
    @BindView(R.id.et_input)
    EditText et_input;
    @BindView(R.id.btn_insert)
    Button btn_insert;
    @BindView(R.id.sggxAddEt1)
    TextView sggxAddEt1;
    @BindView(R.id.sggxAddEt2)
    TextView sggxAddEt2;
    @BindView(R.id.sfwcEt)
    TextView sfwcEt;
    @BindView(R.id.sjEt1)
    TextView sjEt1;
    @BindView(R.id.sjEt2)
    TextView sjEt2;

    //定义控件结束

    //自定义变量开始
    private String did = "";
    private String orderClasses = "";
    private String reportTime = "";
    //施工工序下拉框2的key值
    List<String> sggxEt2Key = new ArrayList<>();
    //施工工序下拉框2的value值
    List<String> sggxEt2Value = new ArrayList<>();
    //对应下拉框2的pdid
    private String tmpPdid;
    //自定义变量结束

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();


    }

    private void initData(){
        Intent in = getIntent();
        did = in.getStringExtra("did");
        orderClasses = in.getStringExtra("orderClasses");
        reportTime = in.getStringExtra("reportTime");
        sggxAddEt1.setOnClickListener(this);
        sggxAddEt2.setOnClickListener(this);
        sfwcEt.setOnClickListener(this);
        sjEt1.setOnClickListener(this);
        sjEt2.setOnClickListener(this);
        btn_insert.setOnClickListener(this);
        et_input.setOnLongClickListener(this);
    }

    @Override
    public void onClick(android.view.View view) {
        SinglePicker sin2;
        switch (view.getId()){
            case R.id.sggxAddEt1:
                sin2 = new SinglePicker(AddTxtActivity.this,new String[]{"定制工序","附加工序"} );
                sin2.setItemWidth(300);
                sin2.setCanLoop(false);
                sin2.setOnItemPickListener(new OnItemPickListener() {
                    @Override
                    public void onItemPicked(int i, Object o) {
                        sggxAddEt1.setText(o+"");
                        sggxEt2Key.clear();
                        sggxAddEt2.setText("");
                        mPresenter.getCs1(did,sggxAddEt1.getText()+"", AppData.INSTANCE.getLoggedUser().getOilfield());
                    }
                });
                sin2.show();
                break;
            case R.id.sggxAddEt2:
                if(sggxEt2Key.
                        size() <= 0){
                    ToastUtils.show("请选择措施一后再选择措施二");
                    return;
                }
                sin2 = new SinglePicker(AddTxtActivity.this,sggxEt2Key);
                sin2.setItemWidth(300);
                sin2.setCanLoop(false);
                sin2.setOnItemPickListener(new OnItemPickListener() {
                    @Override
                    public void onItemPicked(int i, Object o) {
                        sggxAddEt2.setText(o+"");
                        tmpPdid = sggxEt2Value.get(i);
                        ToastUtils.show("文本对应的value____"+sggxEt2Value.get(i));
                        Intent intent = new Intent(AddTxtActivity.this, AddSgcsActivity.class);
                        intent.putExtra("did",did);
                        intent.putExtra("orderClasses",orderClasses);
                        intent.putExtra("reportTime",reportTime);
                        intent.putExtra("value",sggxEt2Value.get(i));
                        startActivityForResult(intent,WHAT_SELECT_DATA);

                    }
                });
                sin2.show();
                break;
            case R.id.sfwcEt:
                sin2 = new SinglePicker(AddTxtActivity.this,new String[]{"是","否"});
                sin2.setItemWidth(300);
                sin2.setCanLoop(false);
                sin2.setOnItemPickListener(new OnItemPickListener() {
                    @Override
                    public void onItemPicked(int i, Object o) {
                        sfwcEt.setText(o+"");
                    }
                });
                sin2.show();
                break;
            case R.id.sjEt1:
                showHourPick(sjEt1);
                break;
            case R.id.sjEt2:
                showHourPick(sjEt2);
                break;
            case R.id.btn_insert:
                checkBlank();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //接受保存的数据刷新列表
        if(requestCode == WHAT_SELECT_DATA && resultCode==RESULT_OK){
            String msg = (String)data.getSerializableExtra("data");
            et_input.setText(msg);
        }
    }

    /*
    是否全部填写
     */
    private void checkBlank(){
        if(StringUtils.isBlank(sggxAddEt1.getText()+"")
           ||StringUtils.isBlank(sggxAddEt2.getText()+"")
           ||StringUtils.isBlank(sfwcEt.getText()+"")
           ||StringUtils.isBlank(sjEt1.getText()+"")
           ||StringUtils.isBlank(sjEt2.getText()+"")
           ||StringUtils.isBlank(et_input.getText()+"")
         ){
            Toast.makeText(this,"请全部填写后再保存!",Toast.LENGTH_LONG).show();
            return;
        }else{
            saveData();
        }
    }

    /**
     * 保存Item
     */
    public void saveData(){

        String content = et_input.getText().toString();
        AddModel model = new AddModel();
        model.setContent(content);
        model.setCs1(sggxAddEt1.getText()+"");
        model.setCs2(sggxAddEt2.getText()+"");
        model.setSj1(sjEt1.getText()+"");
        model.setSj2(sjEt2.getText()+"");
        model.setIsOver(sfwcEt.getText()+"");
        model.setPdid(tmpPdid);
        Intent intent=new Intent();
        intent.putExtra("data",model);
        setResult(RESULT_OK,intent);
        finish();
    }


    /**
     * 显示时间
     * @param tv
     */
    private void showHourPick(TextView tv){
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        if(orderClasses.equals("零")){
            picker.setRangeStart(00, 0);//09:00
            picker.setRangeEnd(06, 0);//18:30
        }else if(orderClasses.equals("八")){
            picker.setRangeStart(06, 0);//09:00
            picker.setRangeEnd(16, 0);//18:30
        }else if (orderClasses.equals("四")){
            picker.setRangeStart(16, 0);//09:00
            picker.setRangeEnd(23, 0);//18:30
        }
        picker.setTopLineVisible(false);
        picker.setLineVisible(false);
        picker.setCanLoop(false);
        picker.setWheelModeEnable(false);
        picker.setWeightEnable(true);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                tv.setText(hour+":"+minute);
            }
        });
        picker.show();
    }

    /**
     * 渲染措施下拉框2
     */
    @Override
    public void renderCs(ArrayList<HashMap<String, String>> li) {

        for (HashMap<String,String> x : li){
            Log.i("______", "hash____"+x);
            sggxEt2Key.add(x.get("key"));
            sggxEt2Value.add(x.get("value"));
        }
    }

    @Override
    public boolean onLongClick(android.view.View view) {
        EditText ett = (EditText)view;
        MyRecognizerDialogListener.startSpeechDialog(this,ett);
        return true;
    }
}
