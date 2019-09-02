package com.hong.ui.activity;


import com.hong.AppData;
import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerActivityComponent;
import com.hong.inject.module.ActivityModule;
import com.hong.mvp.model.sgcs.GxEntity;
import com.hong.mvp.model.sgcs.RbEntity;
import com.hong.mvp.model.sgcs.SgcsReturn;
import com.hong.mvp.presenter.AddSgcsPresenter;
import com.hong.ui.activity.base.BaseActivity;
import com.hong.mvp.contract.IAddSgcsContract.View;
import com.hong.util.NullHelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.InputType;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;


public class AddSgcsActivity extends BaseActivity<AddSgcsPresenter> implements View,OnClickListener  {

    private static final String TAG = AddSgcsActivity.class.getSimpleName()+"-------";

    public void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).activityModule(new ActivityModule(getActivity())).build().inject(this);
    }


    protected int getContentView() {
        return R.layout.activity_addsgcs;
    }


    //定义控件开始

    @BindView(R.id.btn_save)
    Button btn_save;
    //定义控件结束


    //自定义变量开始
    List<GxEntity> li = null;
    private List<RbEntity> entityList = new ArrayList<>();
    private RbEntity rbEntity = new RbEntity();
    String pdid,spid,did;
    //自定义变量结束

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btn_save.setOnClickListener(this);
        initData();
    }

    private void initData(){
        Intent in = getIntent();
        did = in.getStringExtra("did");
        String orderClasses = in.getStringExtra("orderClasses");
        String reportTime = in.getStringExtra("reportTime");
        String value = in.getStringExtra("value");

        String[] split = value.split("\\.");
        if(split.length>0){
            spid = split[0];
            pdid = split[1];
            rbEntity.setDid(did);
            rbEntity.setOrder_classes(orderClasses);
            rbEntity.setReport_time(reportTime);
            rbEntity.setSpid(Integer.parseInt(spid));
            Log.i(TAG, "initData: _________pdid"+this.pdid+"//"+this.spid);
            mPresenter.getSgcsDesc(did,orderClasses,reportTime,spid,pdid);
        }else{
            return;
        }
    }


    @Override
    public void onClick(android.view.View view) {
        switch (view.getId()){
            case R.id.btn_save:
                LinearLayout ll = (LinearLayout) findViewById(R.id.father);
                Log.i(TAG, "onClick: 点击一下"+ll.getChildCount());
                for (int i = 0; i < (ll.getChildCount()-1); i++) {
                    android.view.View l1 = ll.getChildAt(i);
                    if(l1 instanceof  LinearLayout){
                        LinearLayout l2 = (LinearLayout) l1;
                        RbEntity rb = new RbEntity();
                        EditText et1 =(EditText) l2.getChildAt(0);
                        EditText et2 =(EditText) l2.getChildAt(1);
                        EditText et3 =(EditText) l2.getChildAt(2);
                        EditText et4 =(EditText) l2.getChildAt(3);
//                        System.out.println(et1.getText()+"///"+et2.getText()+"///"+et3.getText()+"///"+et4.getText());

                        int i1 = Integer.parseInt(et4.getText() + "");
                        rb.setPro_param_id(i1);
                        rb.setParamname(et1.getText()+"");
                        rb.setContent(et2.getText()+"");
                        rb.setUnit(et3.getText()+"");
                        entityList.add(rb);
                       /* for (int j = 0; j < l2.getChildCount(); j++) {
                            android.view.View childAt = l2.getChildAt(j);
                            if(childAt instanceof EditText){
                                EditText et = (EditText) childAt;
                                System.out.println(et.getText()+"...."+i+"////"+j);
                            }
                        }*/
                    }
                }
                rbEntity.setEntityList(entityList);
                rbEntity.setOilfield(AppData.INSTANCE.getLoggedUser().getOilfield());
                rbEntity.setCreate_user(AppData.INSTANCE.getLoggedUser().getId());
                //施工参数保存设置pdid
                rbEntity.setPdid(Integer.parseInt(this.pdid));
                rbEntity.setDid(did);
                rbEntity.setSpid(Integer.parseInt(this.spid));
                Log.i(TAG, "initData: ______3333333333___pdid"+this.pdid+"//"+this.spid);
                mPresenter.savePPData(rbEntity);

//                for (RbEntity r : entityList){
//                    System.out.println(r.getPro_param_id()+"///"+r.getParamname()+"//"+r.getContent()+"///"+r.getUnit()+"___________-niupi");
//                }

                break;
        }



    }

    /**
     * 渲染此Activity的布局
     */
    @Override
    public void renderSgcsActivity(SgcsReturn body) {
        LinearLayout ll = (LinearLayout) findViewById(R.id.father);
        li = body.getEntity_param_list();
        //那一群下拉框啥的
        for (GxEntity g : li){
            android.view.View inflate1 = getLayoutInflater().inflate(R.layout.layout_item_sgcs, ll, false);
            ll.addView(inflate1);

            TextView tv = (TextView)inflate1.findViewById(R.id.key);
            tv.setText(g.getParam());

            TextView pro_param_id = (TextView)inflate1.findViewById(R.id.pro_param_id);
            pro_param_id.setText(g.getId()+"");
            System.out.println("____________"+g.getId());

            if(g.getDatatype().equals("list")){
                EditText val = (EditText)inflate1.findViewById(R.id.valueEt);
                val.setVisibility(android.view.View.VISIBLE);
                val.clearFocus();
                val.setInputType(InputType.TYPE_NULL);
                val.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(android.view.View view) {
                        show(val,g.getTempdata());
                    }
                });

            }else{
                EditText val = (EditText)inflate1.findViewById(R.id.valueEt);
                val.setVisibility(android.view.View.VISIBLE);
                val.setText(g.getParamdata());
            }

            EditText unit = (EditText)inflate1.findViewById(R.id.unit);
            unit.setText(NullHelper.isNull(g.getUnits()));
        }

        //施工内容
        android.view.View inflate1 = getLayoutInflater().inflate(R.layout.layout_item_sgcs, ll, false);
        ll.addView(inflate1);
        TextView tv = (TextView)inflate1.findViewById(R.id.key);
        tv.setText("施工内容: ");
        tv.setVisibility(android.view.View.GONE);
        EditText tv1 = (EditText)inflate1.findViewById(R.id.valueEt);
        tv1.setVisibility(android.view.View.GONE);
        EditText tv3 = (EditText)inflate1.findViewById(R.id.unit);
        tv3.setVisibility(android.view.View.GONE);

        LinearLayout linearLayout = (LinearLayout)inflate1.findViewById(R.id.contentLayout);
        linearLayout.setVisibility(android.view.View.GONE);
        TextView con = (TextView)inflate1.findViewById(R.id.content);
        con.setText(body.getEntity().getBuildcontentTMP());
    }

    @Override
    public void isOver(HashMap<String, String> hash) {
        String body = hash.get("code");
        String msg = hash.get("msg");
        System.out.println("___________"+msg);
        if (body.equals("1")||body=="1"){
            Intent intent=new Intent();
            intent.putExtra("data",msg);
            setResult(RESULT_OK,intent);
            finish();
        }else{
            Toast.makeText(this,"保存失败!",Toast.LENGTH_LONG).show();
        }
    }

    private void show(EditText et, String li){
        SinglePicker sin2;
        sin2 = new SinglePicker(this, li.split(","));
        sin2.setItemWidth(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        sin2.setCanLoop(false);
        sin2.setOnItemPickListener(new OnItemPickListener() {
            public void onItemPicked(int i, Object o) {
                et.setText(o+"");
            }
        });
        sin2.show();
    }
}
