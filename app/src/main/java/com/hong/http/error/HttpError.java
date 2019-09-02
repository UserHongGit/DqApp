

package com.hong.http.error;

/**
 * 网络请求错误
 * Created on 2019/05/19.
 *
 * @author upc_jxzy
 */

public class HttpError extends Error {

    private int errorCode = -1;

    public HttpError(int errorCode) {
        super(HttpErrorCode.getErrorMsg(errorCode));
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
