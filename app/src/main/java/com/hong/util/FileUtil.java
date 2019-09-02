package com.hong.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;
import java.io.File;
import java.io.FileInputStream;

public class FileUtil {
    private static final String AUDIO_CACHE_DIR_NAME = "audio";
    private static final String HTTP_CACHE_DIR_NAME = "http_response";
    private static final String SIGN_IMAGE_CACHE_DIR_NAME = "sign_image";

    @Nullable
    public static File getCacheDir(@NonNull Context context, @NonNull String dirName) {
        File cacheFile = new File(context.getExternalCacheDir(), dirName);
        if (!cacheFile.exists()) {
            cacheFile.mkdir();
        }
        return cacheFile;
    }

    @Nullable
    public static File getAudioCacheDir(@NonNull Context context) {
        return getCacheDir(context, AUDIO_CACHE_DIR_NAME);
    }

    @Nullable
    public static File getSignImageCacheDir(@NonNull Context context) {
        return getCacheDir(context, SIGN_IMAGE_CACHE_DIR_NAME);
    }

    @Nullable
    public static File getHttpImageCacheDir(@NonNull Context context) {
        return getCacheDir(context, HTTP_CACHE_DIR_NAME);
    }

    public static boolean isExternalStorageEnable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String encodeBase64File(@NonNull String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[((int) file.length())];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, 0);
    }
}
