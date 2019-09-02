package com.hong.util.xunfei;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class MyRecognizerDialogListener implements RecognizerDialogListener {

    private static final String TAG = "====";
    // 用HashMap存储听写结果
    static HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    static TextView tv_error = null;
    static EditText et_input = null;
    static Context context = null;

    // 听写监听器
    public static RecognizerListener mRecoListener = new RecognizerListener() {
        // 听写结果回调接口 (返回Json 格式结果，用户可参见附录 13.1)；
//一般情况下会通过onResults接口多次返回结果，完整的识别内容是多次结果的累加；
//关于解析Json的代码可参见 Demo中JsonParser 类；
//isLast等于true 时会话结束。
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.e(TAG, "嗨了就嗨,,,,"+results.getResultString());
            System.out.println(results.getResultString());
            showTip(context,results.getResultString());
        }

        // 会话发生错误回调接口
        public void onError(SpeechError error) {
            /**
             * 过滤掉没有说话的错误码显示
             */
//            TextView tv_error = (TextView) mDialog.getWindow().getDecorView().findViewWithTag("errtxt");
//            if (tv_error != null && tv_error.getText().toString().indexOf("您好像没有说话哦...")!=-1) {
//                tv_error.setText("您好像没有说话哦...");
//            }
            Log.i(TAG, "onError: 草))))))))))))))))");
            showTip(context,error.getPlainDescription(false));
            // 获取错误码描述
            Log.e(TAG, "error.getPlainDescription(true)==" + error.getPlainDescription(true));
        }

        // 开始录音
        public void onBeginOfSpeech() {
            Log.i(TAG, "onBeginOfSpeech: 开始录音");
            showTip(context," 开始录音 ");
        }

        //volume 音量值0~30， data音频数据
        public void onVolumeChanged(int volume, byte[] data) {
            Log.i(TAG, "onVolumeChanged: 声音改变");
            showTip(context," 声音改变了 ");
        }

        // 结束录音
        public void onEndOfSpeech() {
            Log.i(TAG, "onEndOfSpeech: 结束录音");
            showTip(context," 结束录音 ");
        }

        // 扩展用接口
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            Log.i(TAG, "onEvent: 我去");
        }
    };

    private static void showTip(Context context, String data) {
        Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
    }


    public static void startSpeechDialog(Context con, EditText ett) {
        context = con;
        MyRecognizerDialogListener.et_input = ett;
        //1. 创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(context, new MyInitListener());

        //2. 设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");// 设置中文
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 若要将UI控件用于语义理解，必须添加以下参数设置，设置之后 onResult回调返回将是语义理解
        // 结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener(new MyRecognizerDialogListener());
        //4. 显示dialog，接收语音输入
        mDialog.show();
        ((TextView) mDialog.getWindow().getDecorView().findViewWithTag("textlink")).setOnClickListener(null);
        ((TextView) mDialog.getWindow().getDecorView().findViewWithTag("textlink")).setText("点击窗口,结束倾听");
        MyRecognizerDialogListener.tv_error = (TextView) mDialog.getWindow().getDecorView().findViewWithTag("errtxt");
    }



    /**
     * @param results
     * @param isLast  是否说完了
     */
    @Override
    public void onResult(RecognizerResult results, boolean isLast) {
        String result = results.getResultString(); //为解析的
//        showTip(result);
        System.out.println(" 没有解析的 :" + result);

        String text = JsonParser.parseIatResult(result);//解析过后的
        System.out.println(" 解析后的 :" + text);

        String sn = null;
        // 读取json结果中的 sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);//没有得到一句，添加到

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        et_input.setText(resultBuffer.toString());// 设置输入框的文本
        et_input.setSelection(et_input.length());//把光标定位末尾
    }

    @Override
    public void onError(SpeechError speechError) {
        Log.i(TAG, "onError: ______");


        if (tv_error != null && tv_error.getText().toString().indexOf("您好像没有说话哦...")!=-1) {
            tv_error.setText("您好像没有说话哦...");
        }


    }
}
