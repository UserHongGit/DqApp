package com.hong.util;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.hong.AppApplication;
import com.hong.R;
import es.dmoral.toasty.Toasty;
import java.util.Locale;

public class AppUtils {
    private static final String DOWNLOAD_SERVICE_PACKAGE_NAME = "com.android.providers.downloads";

    public static boolean checkApplicationEnabledSetting(Context context, String packageName) {
        int state = context.getPackageManager().getApplicationEnabledSetting(packageName);
        return state == 0 || state == 1;
    }

    public static boolean checkDownloadServiceEnabled(Context context) {
        return checkApplicationEnabledSetting(context, DOWNLOAD_SERVICE_PACKAGE_NAME);
    }

    public static void showDownloadServiceSetting(Context context) {
        String str = "android.settings.APPLICATION_DETAILS_SETTINGS";
        try {
            Intent intent = new Intent(str);
            intent.setData(Uri.parse("package:com.android.providers.downloads"));
            intent.addFlags(268435456);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Intent intent2 = new Intent(str);
            intent2.addFlags(268435456);
            context.startActivity(intent2);
        }
    }

    public static void updateAppLanguage(@NonNull Context context) {
        String lang = PrefUtils.getLanguage();
        if (VERSION.SDK_INT >= 24) {
            updateResources(context, lang);
        }
        updateResourcesLegacy(context, lang);
    }

    private static void updateResources(Context context, String language) {
        Locale locale = getLocale(language);
        Locale.setDefault(locale);
        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);
        context.createConfigurationContext(configuration);
    }

    private static void updateResourcesLegacy(Context context, String language) {
        Locale locale = getLocale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @NonNull
    public static Locale getLocale(String language) {
        String[] array = language.split("-");
        if (array.length <= 1) {
            return new Locale(language);
        }
        return new Locale(array[0], array[1].replaceFirst("r", ""));
    }

    public static boolean isNightMode() {
        String theme = PrefUtils.getTheme();
        return PrefUtils.DARK.equals(theme) || PrefUtils.AMOLED_DARK.equals(theme);
    }

    public static void copyToClipboard(@NonNull Context context, @NonNull String uri) {
        ((ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText(context.getString(R.string.app_name), uri));
        Toasty.success(AppApplication.get(), context.getString(R.string.success_copied)).show();
    }

    public static boolean isLandscape(@NonNull Resources resources) {
        return resources.getConfiguration().orientation == 2;
    }

    public static void showKeyboard(@NonNull View v, @NonNull Context activity) {
        ((InputMethodManager) activity.getSystemService("input_method")).showSoftInput(v, 0);
    }

    public static void showKeyboard(@NonNull View v) {
        showKeyboard(v, v.getContext());
    }

    public static void hideKeyboard(@NonNull View view) {
        ((InputMethodManager) view.getContext().getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Nullable
    public static long getFirstInstallTime() {
        try {
            return AppApplication.get().getPackageManager().getPackageInfo(AppApplication.get().getPackageName(), 0).firstInstallTime;
        } catch (NameNotFoundException e1) {
            e1.printStackTrace();
            return 0;
        }
    }
}
