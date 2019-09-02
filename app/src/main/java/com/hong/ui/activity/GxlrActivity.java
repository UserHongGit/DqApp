package com.hong.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import cn.addapp.pickers.common.LineConfig;
import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.picker.SinglePicker;
import cn.addapp.pickers.picker.TimePicker;
import cn.addapp.pickers.picker.TimePicker.OnTimePickListener;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.hong.AppData;
import com.hong.R;
import com.hong.inject.component.AppComponent;
import com.hong.inject.component.DaggerActivityComponent;
import com.hong.inject.module.ActivityModule;
import com.hong.mvp.contract.IGxlrContract.View;
import com.hong.mvp.model.AddModel;
import com.hong.mvp.model.ReportWork;
import com.hong.mvp.presenter.GxlrPresenter;
import com.hong.ui.activity.base.BaseActivity;
import com.hong.ui.adapter.MyAdapter;
import com.hong.util.CalendarUtil;
import com.hong.util.JsonParser;
import com.hong.util.StringUtils;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class GxlrActivity extends BaseActivity<GxlrPresenter> implements View, OnClickListener, OnLongClickListener {
    private static final String TAG = "GxlrActivity______";
    private static final int WHAT_SELECT_DATA = 10;

    private HashMap<String, String> mIatResults = new LinkedHashMap();
    private RecognizerListener mRecoListener = new RecognizerListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.e(GxlrActivity.TAG, results.getResultString());
            System.out.println(results.getResultString());
            GxlrActivity.this.showTip(results.getResultString());
        }

        public void onError(SpeechError error) {
            GxlrActivity.this.showTip(error.getPlainDescription(true));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("error.getPlainDescription(true)==");
            stringBuilder.append(error.getPlainDescription(true));
            Log.e(GxlrActivity.TAG, stringBuilder.toString());
        }

        public void onBeginOfSpeech() {
            GxlrActivity.this.showTip(" 开始录音 ");
        }

        public void onVolumeChanged(int volume, byte[] data) {
            GxlrActivity.this.showTip(" 声音改变了 ");
        }

        public void onEndOfSpeech() {
            GxlrActivity.this.showTip(" 结束录音 ");
        }

        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };

    class MyInitListener implements InitListener {
        MyInitListener() {


        }

        public void onInit(int code) {
            if (code != 0) {
                GxlrActivity.this.showTip("初始化失败 ");
            }
        }
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {
        MyRecognizerDialogListener() {
        }

        public void onResult(RecognizerResult results, boolean isLast) {
            String result = results.getResultString();
            GxlrActivity.this.showTip(result);
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" 没有解析的 :");
            stringBuilder.append(result);
            printStream.println(stringBuilder.toString());
            String text = JsonParser.parseIatResult(result);
            PrintStream printStream2 = System.out;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(" 解析后的 :");
            stringBuilder2.append(text);
            printStream2.println(stringBuilder2.toString());
//            GxlrActivity.this.et_input.append(text);
            String sn = null;
            try {
                sn = new JSONObject(results.getResultString()).optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            GxlrActivity.this.mIatResults.put(sn, text);
            StringBuffer resultBuffer = new StringBuffer();
            Iterator it = GxlrActivity.this.mIatResults.keySet().iterator();
            while (true) {
                boolean hasNext = it.hasNext();
                String str = GxlrActivity.TAG;
                if (hasNext) {
                    String key = (String) it.next();
                    resultBuffer.append((String) GxlrActivity.this.mIatResults.get(key));
                    StringBuilder stringBuilder3 = new StringBuilder();
                    stringBuilder3.append("onResult: ");
                    stringBuilder3.append((String) GxlrActivity.this.mIatResults.get(key));
                    Log.d(str, stringBuilder3.toString());
                } else {
                    StringBuilder stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("onResult: _________");
                    stringBuilder4.append(resultBuffer.toString());
                    Log.d(str, stringBuilder4.toString());
//                    GxlrActivity.this.et_input.setSelection(GxlrActivity.this.et_input.length());
                    return;
                }
            }
        }

        public void onError(SpeechError speechError) {
            PrintStream printStream = System.out;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(speechError.getErrorCode());
            stringBuilder.append("////");
            stringBuilder.append(speechError.getErrorDescription());
            printStream.println(stringBuilder.toString());
            if (speechError.getErrorCode() == ErrorCode.MSP_ERROR_NO_DATA) {
                GxlrActivity.this.showTip("没说话");
            }
        }
    }

    class MySynthesizerListener implements SynthesizerListener {
        MySynthesizerListener() {
        }

        public void onSpeakBegin() {
            GxlrActivity.this.showTip(" 开始播放 ");
        }

        public void onSpeakPaused() {
            GxlrActivity.this.showTip(" 暂停播放 ");
        }

        public void onSpeakResumed() {
            GxlrActivity.this.showTip(" 继续播放 ");
        }

        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        public void onCompleted(SpeechError error) {
            if (error == null) {
                GxlrActivity.this.showTip("播放完成 ");
            } else if (error != null) {
                GxlrActivity.this.showTip(error.getPlainDescription(true));
            }
        }

        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    }

    /* access modifiers changed from: protected */
    public void setupActivityComponent(AppComponent appComponent) {
        Log.i(TAG, "setupActivityComponent: 老哥注一下子______");
        DaggerActivityComponent.builder().appComponent(appComponent).activityModule(new ActivityModule(getActivity())).build().inject(this);
    }


    //增删行开始
    private RecyclerView mRecyclerView;
    private Context mContext = GxlrActivity.this;
    //改变集合数据类型,对象
    private List<AddModel> mStrList;

    private MyAdapter mMyAdapter;
    private int clickTime = 0;
    //增删行结束

    //定义控件开始
    @BindView(R.id.headTitle)
    TextView titleTv;
    @BindView(R.id.sgdwEv)
    TextView sgdwEv;
    @BindView(R.id.zzbmEv)
    TextView zzbmEv;
    @BindView(R.id.sbrqEt)
    TextView sbrqEt;
    @BindView(R.id.bcEt)
    TextView bcEt;
    @BindView(R.id.kgrqEt)
    TextView kgrqEt;
    @BindView(R.id.zyxmEt)
    EditText zyxmEt;
    @BindView(R.id.sgjdEt)
    EditText sgjdEt;
    @BindView(R.id.hdEt)
    EditText hdEt;
    @BindView(R.id.cwEt)
    EditText cwEt;
    @BindView(R.id.chEt)
    EditText chEt;
    @BindView(R.id.xybgxEt)
    EditText xybgxEt;
    @BindView(R.id.jlEt)
    EditText jlEt;
    @BindView(R.id.bzEt)
    EditText bzEt;
    @BindView(R.id.dbgbEt)
    EditText dbgbEt;
    @BindView(R.id.lryEt)
    EditText lryEt;




    @BindView(R.id.sfwjEt)
    TextView sfwjEt;
    @BindView(R.id.addTxt)
    FloatingActionButton addTxt;
    @BindView(R.id.btn_save)
    Button btn_save;

    //定义控件结束\

    //自定义需要变量开始
    private String did = "";
    private  String orderClasses = "";
    private  String reportTime = "";
    private ReportWork report;

    //自定义需要变量结束

    @Override
    public void renderBaseData(ReportWork report) {
        this.report = report;
        Log.i(TAG, "renderBaseData: +++++++++++++++"+report.getBanbaoType());
        titleTv.setText(report.getWellCommonName()+" 作业班报表(修井)");
        sgdwEv.setText(report.getTeamName());
        zzbmEv.setText(report.getIntelligenceCode());
        sbrqEt.setText(report.getReportTime3());
        bcEt.setText(report.getOrderClasses());
        zyxmEt.setText(report.getWorkBrief());
        kgrqEt.setText(report.getWorkDate3());
        sgjdEt.setText(report.getConstructInterval());
        hdEt.setText(report.getDensityStraturm());
        cwEt.setText(report.getStratigraphicPosition());
        chEt.setText(report.getLevelNumber());
    }

    @Override
    public void saveOk(HashMap<String, String> map) {
        if(map.get("code").equals("0")){
            Toast.makeText(this,map.get("msg"),Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"保存成功!",Toast.LENGTH_LONG).show();
            finish();
        }
    }


    /* access modifiers changed from: protected */
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: _____");
        initRow();
        initView();
        initData();
        initSpeech();
    }

    private void initData() {
        Intent in = getIntent();
        did = in.getStringExtra("did");
        String banbaoType = in.getStringExtra("banbaoType");
        String oilfield = in.getStringExtra("oilfield");
        reportTime = in.getStringExtra("reportTime");
        orderClasses = in.getStringExtra("orderClasses");
        ((GxlrPresenter) this.mPresenter).getBaseData(did,banbaoType,reportTime,orderClasses);
    }

    private void initView() {
//        this.btn_startspeech = (Button) findViewById(R.id.btn_startspeech);
//        this.btn_startspeektext = (Button) findViewById(R.id.btn_startspeektext);
//        this.btn_startspeektext.setOnClickListener(this);
        sfwjEt.setOnClickListener(this);
        addTxt.setOnClickListener(this);
        btn_save.setOnClickListener(this);



    }

    private void initSpeech() {
        SpeechUtility.createUtility(this, "appid=5cbec4d8");
    }

    private void initRow(){
        mStrList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

         mRecyclerView.setLayoutManager(linearLayoutManager);
        mMyAdapter = new MyAdapter(mStrList);
        // 设置item及item中控件的点击事件
        mMyAdapter.setOnItemClickListener(onItemClickListener);
        mRecyclerView.setAdapter(mMyAdapter);
    }


    /**
     * 按钮点击事件
     */
    public void onClick(android.view.View v) {
        SinglePicker sin2;
        switch (v.getId()) {
            case R.id.btn_startspeektext /*2131296326*/:
                speekText();
                return;
//            case R.id.et_input /*2131296371*/:
//                startSpeechDialog();
//                return;
            case R.id.sfwjEt:
                sin2 = new SinglePicker(GxlrActivity.this,new String[]{"是","否"} );
                sin2.setItemWidth(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                sin2.setCanLoop(false);
                sin2.setOnItemPickListener(new OnItemPickListener() {
                    @Override
                    public void onItemPicked(int i, Object o) {
                        sfwjEt.setText(o+"");
                    }
                });
                sin2.show();
                break;
            case R.id.addTxt:
                Intent i = new Intent(this, AddTxtActivity.class);
                i.putExtra("did",did);
                i.putExtra("orderClasses",orderClasses);
                i.putExtra("reportTime",reportTime);
                startActivityForResult(i,WHAT_SELECT_DATA);
                break;
            case R.id.btn_save:
                Log.i(TAG, "onClick:保存全部数据__"+mStrList);
                boolean ok  = checkBlank();
                if(!ok){
                    Toast.makeText(this,"请全部填写后再保存!",Toast.LENGTH_LONG).show();
                    return;
                }else{
                    List<ReportWork> entity_list = new ArrayList<>();
                    for(AddModel ad : mStrList){
                        ReportWork wokr = new ReportWork();
                        wokr.setGxType(ad.getCs1());
                        String[] split = ad.getPdid().split("\\.");
                        String spidV,pdidV;
                        spidV = split[0];
                        pdidV = split[1];
                        wokr.setSpid(Integer.parseInt(spidV));
                        wokr.setPdid(Integer.parseInt(pdidV));
                        wokr.setSpid_pdid(ad.getPdid());
                        wokr.setWorkContent(ad.getContent());
                        wokr.setIsComplete(ad.getIsOver());
                        String[] split1 = ad.getSj1().split(":");
                        wokr.setBeginTimeHour(Integer.parseInt(split1[0]));
                        wokr.setBeginTimeMinute(Integer.parseInt(split1[1]));
                        String[] split2 = ad.getSj2().split(":");
                        wokr.setEndTimeHour(Integer.parseInt(split2[0]));
                        wokr.setEndTimeMinute(Integer.parseInt(split2[1]));

                        entity_list.add(wokr);
                    }
                    ReportWork re = new ReportWork();
                    re.setEntity_list(entity_list);
                    re.setDid(did);
                    re.setWellCommonName(report.getWellCommonName());
                    re.setIntelligenceCode(report.getIntelligenceCode());
                    re.setReportTime3(report.getReportTime3());
                    re.setOrderClasses(report.getOrderClasses());
                    re.setTeamName(report.getTeamName());
                    re.setBanbaoType(report.getBanbaoType());
                    re.setWorkBrief(zyxmEt.getText()+"");
                    re.setWorkDate3(kgrqEt.getText()+"");
                    re.setNextCircuit(xybgxEt.getText()+"");
                    re.setConstructInterval(sgjdEt.getText()+"");
                    re.setDensityStraturm(hdEt.getText()+"");
                    re.setStratigraphicPosition(cwEt.getText()+"");
                    re.setLevelNumber(chEt.getText()+"");
                    re.setRecordName(jlEt.getText()+"");
                    re.setClassMonitor(bzEt.getText()+"");
                    re.setWatchCadre(dbgbEt.getText()+"");
                    re.setRedactionName(lryEt.getText()+"");
                    re.setCompleteJudgement(sfwjEt.getText()+"");
                    re.setOilfield(AppData.INSTANCE.getLoggedUser().getOilfield());
                    re.setCreateUser(AppData.INSTANCE.getLoggedUser().getId());
                    Log.i(TAG, "onClick: ____"+bzEt.getText());
                    mPresenter.sgbbSave(re);
                    break;
                }
            default:
                return;
        }
    }

    private boolean checkBlank() {
        boolean ok = false;
        if(StringUtils.isBlank(zyxmEt.getText()+"")
            ||StringUtils.isBlank(sgjdEt.getText()+"")
            ||StringUtils.isBlank(hdEt.getText()+"")
            ||StringUtils.isBlank(cwEt.getText()+"")
            ||StringUtils.isBlank(chEt.getText()+"")
            ||StringUtils.isBlank(xybgxEt.getText()+"")
            ||StringUtils.isBlank(sfwjEt.getText()+"")
            ||StringUtils.isBlank(jlEt.getText()+"")
            ||StringUtils.isBlank(bzEt.getText()+"")
            ||StringUtils.isBlank(dbgbEt.getText()+"")
            ||StringUtils.isBlank(lryEt.getText()+"")
        ){

        }else{
            ok = true;
        }
        return ok;

    }

    /**
     * Item点击监听
     */
    private MyAdapter.OnItemClickListener onItemClickListener = new MyAdapter.OnItemClickListener() {
        SinglePicker sin2;
        @Override
        public void onItemClick(int position, android.view.View v) {
            switch (v.getId()) {
                case R.id.add:
//                    clickTime++;
//                    Toast.makeText(mContext, "增:" + position, Toast.LENGTH_SHORT).show();
//                    mStrList.add(position + 1, "增" + clickTime);
//                    mMyAdapter.notifyDataSetChanged();
//                    Log.i(TAG, "mStrList:" + mStrList.toString());
                    break;

                case R.id.del:
                    Toast.makeText(mContext, "删:" + position, Toast.LENGTH_SHORT).show();
                    mStrList.remove(position);
                    mMyAdapter.notifyDataSetChanged();
                    break;

                case R.id.sfwcEt:
                    //d都弄到第二个界面AddTxtActivity?是的
//                    sin2 = new SinglePicker(GxlrActivity.this,new String[]{"是","否"} );
//                    sin2.setItemWidth(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
//                    sin2.setCanLoop(false);
//                    sin2.setOnItemPickListener(new OnItemPickListener() {
//                        @Override
//                        public void onItemPicked(int i, Object o) {
//                            Log.i(TAG, "Item点击监听__onItemPicked: _________");
//                            //这个地方可以获取到他的值,但是获取不到实际的控件对象
//
//
//
//                        }
//                    });
//                    sin2.show();
                    break;

            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //接受保存的数据刷新列表
        if(requestCode == WHAT_SELECT_DATA && resultCode==RESULT_OK){
            AddModel model = (AddModel) data.getSerializableExtra("data");
            mStrList.add(model);
            mMyAdapter.notifyDataSetChanged();

        }
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

    private void speekText() {
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(this, null);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "vixyun");
        mTts.setParameter(SpeechConstant.SPEED, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "80");
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm");
//        mTts.startSpeaking(this.et_input.getText().toString(), new MySynthesizerListener());
    }

    public boolean onLongClick(android.view.View v) {
        startSpeechDialog();
        return true;
    }

    private void startSpeechDialog() {
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        mDialog.setParameter("language", "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        mDialog.setListener(new MyRecognizerDialogListener());
        mDialog.show();
        ((TextView) mDialog.getWindow().getDecorView().findViewWithTag("textlink")).setText("");
    }

    private void startSpeech() {
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(this, null);
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter("language", "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        mIat.startListening(this.mRecoListener);
    }

    private void showTip(String data) {
    }

    /* access modifiers changed from: protected */
    public int getContentView() {
        return R.layout.activity_gxlr;
    }
}
