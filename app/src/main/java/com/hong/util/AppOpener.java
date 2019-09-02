package com.hong.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ResolveInfo.DisplayNameComparator;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsIntent.Builder;
import com.hong.R;
import com.hong.http.Downloader;
import com.hong.service.CopyBroadcastReceiver;
import com.hong.service.ShareBroadcastReceiver;
import es.dmoral.toasty.Toasty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AppOpener {
    private static final List<String> VIEW_IGNORE_PACKAGE = Arrays.asList(new String[]{"com.gh4a", "com.fastaccess", "com.taobao.taobao"});

    public static void openInCustomTabsOrBrowser(@NonNull Context context, @NonNull String url) {
        if (StringUtils.isBlank(url)) {
            Toasty.warning(context, context.getString(R.string.invalid_url), 1).show();
            return;
        }
        if (!url.contains("//")) {
            url = "http://".concat(url);
        }
        if (PrefUtils.isCustomTabsEnable()) {
            String bestPackageName = CustomTabsHelper.INSTANCE.getBestPackageName(context);
            String customTabsPackageName = bestPackageName;
            if (bestPackageName != null) {
                Bitmap backIconBitmap = ViewUtils.getBitmapFromResource(context, R.drawable.ic_arrow_back_title);
                Intent shareIntent = new Intent(context.getApplicationContext(), ShareBroadcastReceiver.class);
                shareIntent.addFlags(268435456);
                PendingIntent sharePendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), 0, shareIntent, 0);
                Intent copyIntent = new Intent(context.getApplicationContext(), CopyBroadcastReceiver.class);
                copyIntent.addFlags(268435456);
                CustomTabsIntent customTabsIntent = new Builder().setToolbarColor(ViewUtils.getPrimaryColor(context)).setCloseButtonIcon(backIconBitmap).setShowTitle(true).addMenuItem(context.getString(R.string.share), sharePendingIntent).addMenuItem(context.getString(R.string.copy_url), PendingIntent.getBroadcast(context.getApplicationContext(), 0, copyIntent, 0)).build();
                customTabsIntent.intent.setPackage(customTabsPackageName);
                customTabsIntent.launchUrl(context, Uri.parse(url));
                if (PrefUtils.isCustomTabsTipsEnable()) {
                    Toasty.info(context, context.getString(R.string.use_custom_tabs_tips), 1).show();
                    PrefUtils.set(PrefUtils.CUSTOM_TABS_TIPS_ENABLE, Boolean.valueOf(false));
                }
            }
        }
        openInBrowser(context, url);
    }

    public static void openInBrowser(@NonNull Context context, @NonNull String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent("android.intent.action.VIEW", uri).addCategory("android.intent.category.BROWSABLE");
        intent.addFlags(268435456);
        intent = createActivityChooserIntent(context, intent, uri, VIEW_IGNORE_PACKAGE);
        if (intent != null) {
            context.startActivity(intent);
        } else {
            Toasty.warning(context, context.getString(R.string.no_browser_clients), 1).show();
        }
    }

    public static void startDownload(@NonNull Context context, @NonNull String url, String fileName) {
        if (PrefUtils.isSystemDownloader()) {
            Downloader.create(context.getApplicationContext()).start(url, fileName);
        } else {
            openDownloader(context, url);
        }
    }

    public static void openDownloader(@NonNull Context context, @NonNull String url) {
        if (StringUtils.isBlank(url)) {
            Toasty.warning(context, context.getString(R.string.invalid_url), 1).show();
            return;
        }
        Uri uri = Uri.parse(url);
        Intent intent = new Intent("android.intent.action.VIEW", uri).addCategory("android.intent.category.BROWSABLE");
        intent.addFlags(268435456);
        intent = createActivityChooserIntent(context, intent, uri, VIEW_IGNORE_PACKAGE);
        if (intent != null) {
            context.startActivity(intent);
        } else {
            Toasty.warning(context, context.getString(R.string.no_download_clients), 1).show();
        }
    }

    public static void shareText(@NonNull Context context, @NonNull String text) {
        Intent shareIntent = new Intent();
        shareIntent.setAction("android.intent.action.SEND");
        shareIntent.putExtra("android.intent.extra.TEXT", text);
        shareIntent.setType("text/plain");
        shareIntent.addFlags(268435456);
        try {
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_to)).addFlags(268435456));
        } catch (ActivityNotFoundException e) {
            Toasty.warning(context, context.getString(R.string.no_share_clients)).show();
        }
    }

    public static void launchEmail(@NonNull Context context, @NonNull String email) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("message/rfc822");
        intent.putExtra("android.intent.extra.EMAIL", new String[]{email});
        intent.addFlags(268435456);
        try {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.send_email)).addFlags(268435456));
        } catch (ActivityNotFoundException e) {
            Toasty.warning(context, context.getString(R.string.no_email_clients)).show();
        }
    }

    public static void openInMarket(@NonNull Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("market://details?id=");
        stringBuilder.append(context.getPackageName());
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString()));
        intent.addFlags(268435456);
        try {
            context.startActivity(Intent.createChooser(intent, context.getString(R.string.open_in_market)).addFlags(268435456));
        } catch (ActivityNotFoundException e) {
            Toasty.warning(context, context.getString(R.string.no_market_clients)).show();
        }
    }

    public static void launchUrl(@NonNull Context context, @NonNull Uri uri) {
        if (!StringUtils.isBlank(uri.toString())) {
            String url = uri.toString();
        }
    }

    public static boolean isAppAlive(Context context, String packageName) {
        List<RunningAppProcessInfo> processInfos = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (((RunningAppProcessInfo) processInfos.get(i)).processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    private static Intent createActivityChooserIntent(Context context, Intent intent, Uri uri, List<String> ignorPackageList) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 65536);
        ArrayList<Intent> chooserIntents = new ArrayList();
        String ourPackageName = context.getPackageName();
        Collections.sort(activities, new DisplayNameComparator(pm));
        for (ResolveInfo resInfo : activities) {
            ActivityInfo info = resInfo.activityInfo;
            if (info.enabled) {
                if (info.exported) {
                    if (!info.packageName.equals(ourPackageName)) {
                        if (ignorPackageList == null || !ignorPackageList.contains(info.packageName)) {
                            Intent targetIntent = new Intent(intent);
                            targetIntent.setPackage(info.packageName);
                            targetIntent.setDataAndType(uri, intent.getType());
                            chooserIntents.add(targetIntent);
                        }
                    }
                }
            }
        }
        if (chooserIntents.isEmpty()) {
            return null;
        }
        Intent lastIntent = (Intent) chooserIntents.remove(chooserIntents.size() - 1);
        if (chooserIntents.isEmpty()) {
            return lastIntent;
        }
        Intent chooserIntent = Intent.createChooser(lastIntent, null);
        chooserIntent.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[]) chooserIntents.toArray(new Intent[chooserIntents.size()]));
        return chooserIntent;
    }
}
