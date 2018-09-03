package com.ad.tootls;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import org.xutils.common.Callback;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.Map;

public class CallBackUtils {

    private static final String TAG = "CallBackUtils";
    private static CallBackUtils callBackUtils;

    private static final int mTimeOut = 1000 * 120;

    private ProgressDialog mDialog;

    public static CallBackUtils getInstance() {
        if (callBackUtils == null) {
            synchronized (CallBackUtils.class) {
                if (callBackUtils == null) {
                    callBackUtils = new CallBackUtils();
                }
            }
        }
        return callBackUtils;
    }

    private CallBackUtils() {
    }

    public void initString(String url, final CallBackUtils.CommonCallback callback) {
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);
        params.setConnectTimeout(mTimeOut);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                callback.onFinish(s, null);
            }

            @Override
            public void onError(Throwable ex, boolean b) {
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String errorResult = httpEx.getResult();
                    String responseMsg = httpEx.getMessage();
                    callback.onFinish(null, responseMsg);
                } else {
                    callback.onFinish(null, ex.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 下载文件
     *
     * @param url
     * @param path
     * @param callback
     */
    public void initFile(String url, final Context context, String path, final CallBackUtils.CommonCallback callback) {
        RequestParams params = new RequestParams(url);
        params.setAutoResume(true);        // 是否使用断点下载
        params.setAutoRename(false);    // 下载后是否根据文件的属性自动命名
        params.setSaveFilePath(path);    // 文件的本地保存路径
        params.setCancelFast(true);
        params.setAsJsonContent(true);
        params.setConnectTimeout(mTimeOut);

        x.http().get(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
                Logger.d(TAG, "开始下载");
                mDialog = new ProgressDialog(context);
                mDialog.setCancelable(false);
                mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置为水平进行条
                mDialog.setMessage("正在下载中...");
                mDialog.setProgress(0);

                mDialog.show();
            }

            @Override
            public void onLoading(long arg0, long arg1, boolean arg2) {
                mDialog.setMax((int) arg0);
                mDialog.setProgress((int) arg1);
            }

            @Override
            public void onSuccess(File arg0) {
                if (mDialog != null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean b) {
                if (ex instanceof HttpException) { // 网络错误
                    HttpException httpEx = (HttpException) ex;
                    int responseCode = httpEx.getCode();
                    String errorResult = httpEx.getResult();
                    String responseMsg = httpEx.getMessage();
                    ToastUtils.showShortToast(context, responseMsg);
                } else {
                    ToastUtils.showShortToast(context, ex.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException arg0) {
            }

            @Override
            public void onFinished() {
            }

        });
    }

    public interface CommonCallback {
        void onFinish(String result, String msg);
    }

    /**
     * 拼接get请求的url请求地址
     */
    public static String getRqstUrl(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder(url);
        boolean isFirst = true;
        for (String key : params.keySet()) {
            if (key != null && params.get(key) != null) {
                if (isFirst) {
                    isFirst = false;
                    builder.append("?");
                } else {
                    builder.append("&");
                }
                builder.append(key)
                        .append("=")
                        .append(params.get(key));
            }
        }
        return builder.toString();
    }
}
