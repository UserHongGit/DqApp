package com.hong.util.xunfei;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;

public class MySynthesizerListener implements SynthesizerListener {

    private Context context;
    private String TAG = "=======";

    private void showTip(Context context, String data) {
        this.context = context;
        Toast.makeText(context, data, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSpeakBegin() {
        showTip(context," 开始播放 ");
    }

    @Override
    public void onSpeakPaused() {
        showTip(context," 暂停播放 ");
    }

    @Override
    public void onSpeakResumed() {
        showTip(context," 继续播放 ");
    }

    @Override
    public void onBufferProgress(int percent, int beginPos, int endPos,
                                 String info) {
        // 合成进度
    }

    @Override
    public void onSpeakProgress(int percent, int beginPos, int endPos) {
        // 播放进度
    }

    @Override
    public void onCompleted(SpeechError error) {
        if (error == null) {
            showTip(context,"播放完成 ");
        } else if (error != null) {
            showTip(context,error.getPlainDescription(true));
        }
    }

    @Override
    public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        Log.i(TAG, "onEvent: 这还得了");
        // 以下代码用于获取与云端的会话 id，当业务出错时将会话 id提供给技术支持人员，可用于查询会话日志，定位出错原因
        // 若使用本地能力，会话 id为null
        //if (SpeechEvent.EVENT_SESSION_ID == eventType) {
        //     String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
        //     Log.d(TAG, "session id =" + sid);
        //}
    }
}
