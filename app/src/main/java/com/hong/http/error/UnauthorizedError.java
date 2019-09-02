

package com.hong.http.error;

/**
 * Created by upc_jxzy on 2017/10/12 14:35:50
 */

public class UnauthorizedError extends HttpError {

    public UnauthorizedError() {
        super(HttpErrorCode.UNAUTHORIZED);
    }

}
