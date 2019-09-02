package com.hong.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.TextView;
import cn.addapp.pickers.widget.WheelListView;
import com.hong.R;
import com.hong.mvp.model.Label;
import com.hong.ui.widget.IssueLabelSpan;
import java.util.ArrayList;

public class ViewUtils {
    public static void virtualClick(View view) {
        virtualClick(view, WheelListView.SECTION_DELAY);
    }

    public static void virtualClick(View view, int pressTime) {
        View view2 = view;
        long downTime = System.currentTimeMillis();
        int width = view.getWidth();
        float x = view.getX() + ((float) (width / 2));
        float y = view.getY() + ((float) (view.getHeight() / 2));
        MotionEvent downEvent = MotionEvent.obtain(downTime, downTime, 0, x, y, 0);
        long upTime = ((long) pressTime) + downTime;
        MotionEvent upEvent = MotionEvent.obtain(upTime, upTime, 1, x, y, 0);
        view2.onTouchEvent(downEvent);
        view2.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }

    public static void setLongClickCopy(@NonNull TextView textView) {
        textView.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                TextView text = (TextView) v;
                AppUtils.copyToClipboard(text.getContext(), text.getText().toString());
                return true;
            }
        });
    }

    public static void setTextView(@NonNull TextView textView, String text) {
        if (StringUtils.isBlank(text)) {
            textView.setVisibility(8);
            return;
        }
        textView.setText(text);
        textView.setVisibility(0);
    }

    public static MenuItem getSelectedMenu(@NonNull MenuItem menuItem) {
        if (menuItem.getSubMenu() == null || menuItem.getSubMenu().size() == 0) {
            return null;
        }
        MenuItem selected = null;
        for (int i = 0; i < menuItem.getSubMenu().size(); i++) {
            MenuItem item = menuItem.getSubMenu().getItem(i);
            if (item.isChecked()) {
                selected = item;
                break;
            }
        }
        return selected;
    }

    public static void selectMenuItem(@NonNull Menu menu, @IdRes int itemId, boolean findSub) {
        boolean find = false;
        int i = 0;
        while (true) {
            boolean z = false;
            if (i >= menu.size()) {
                break;
            }
            if (!findSub) {
                MenuItem item = menu.getItem(i);
                if (menu.getItem(i).getItemId() == itemId) {
                    z = true;
                }
                item.setChecked(z);
            } else if (menu.getItem(i).getItemId() == itemId) {
                find = true;
            }
            i++;
        }
        if (findSub) {
            if (find) {
                selectMenuItem(menu, itemId, false);
            } else {
                for (i = 0; i < menu.size(); i++) {
                    SubMenu subMenu = menu.getItem(i).getSubMenu();
                    if (subMenu != null) {
                        selectMenuItem(subMenu, itemId, true);
                    }
                }
            }
        }
    }

    public static int[] getRefreshLayoutColors(Context context) {
        return new int[]{getAccentColor(context), getPrimaryColor(context), getPrimaryDarkColor(context)};
    }

    @ColorInt
    public static int getPrimaryColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorPrimary);
    }

    @ColorInt
    public static int getPrimaryDarkColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorPrimaryDark);
    }

    @ColorInt
    public static int getAccentColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.colorAccent);
    }

    @ColorInt
    public static int getPrimaryTextColor(@NonNull Context context) {
        return getColorAttr(context, 16842806);
    }

    @ColorInt
    public static int getSecondaryTextColor(@NonNull Context context) {
        return getColorAttr(context, 16842808);
    }

    @ColorInt
    public static int getWindowBackground(@NonNull Context context) {
        return getColorAttr(context, 16842836);
    }

    @ColorInt
    public static int getCardBackground(@NonNull Context context) {
        return getColorAttr(context, R.attr.card_background);
    }

    @ColorInt
    public static int getIconColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.icon_color);
    }

    @ColorInt
    public static int getTitleColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.title_color);
    }

    @ColorInt
    public static int getSubTitleColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.subtitle_color);
    }

    @ColorInt
    public static int getSelectedColor(@NonNull Context context) {
        return getColorAttr(context, R.attr.selected_color);
    }

    @ColorInt
    private static int getColorAttr(@NonNull Context context, int attr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{attr});
        int color = typedArray.getColor(0, -3355444);
        typedArray.recycle();
        return color;
    }

    private static Bitmap getBitmapFromResource(VectorDrawable vectorDrawable) {
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmapFromResource(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return BitmapFactory.decodeResource(context.getResources(), drawableId);
        }
        if (drawable instanceof VectorDrawable) {
            return getBitmapFromResource((VectorDrawable) drawable);
        }
        throw new IllegalArgumentException("unsupported drawable type");
    }

    public static String getRGBColor(int colorValue, boolean withAlpha) {
        StringBuilder stringBuilder;
        int g = (colorValue >> 8) & 255;
        int b = colorValue & 255;
        int a = (colorValue >> 24) & 255;
        String red = Integer.toHexString((colorValue >> 16) & 255);
        String green = Integer.toHexString(g);
        String blue = Integer.toHexString(b);
        String alpha = Integer.toHexString(a);
        red = fixColor(red);
        green = fixColor(green);
        blue = fixColor(blue);
        alpha = fixColor(alpha);
        if (withAlpha) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(alpha);
        } else {
            stringBuilder = new StringBuilder();
        }
        stringBuilder.append(red);
        stringBuilder.append(green);
        stringBuilder.append(blue);
        return stringBuilder.toString();
    }

    private static String fixColor(@NonNull String colorStr) {
        if (colorStr.length() != 1) {
            return colorStr;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("0");
        stringBuilder.append(colorStr);
        return stringBuilder.toString();
    }

    public static boolean isLightColor(int color) {
        if (1.0d - ((((((double) Color.red(color)) * 0.299d) + (((double) Color.green(color)) * 0.587d)) + (((double) Color.blue(color)) * 0.114d)) / 255.0d) < 0.5d) {
            return true;
        }
        return false;
    }

    public static int getLabelTextColor(@NonNull Context context, int bgColorValue) {
        if (isLightColor(bgColorValue)) {
            return context.getResources().getColor(R.color.light_text_color_primary);
        }
        return context.getResources().getColor(R.color.material_light_white);
    }

    @NonNull
    public static SpannableStringBuilder getLabelsSpan(@NonNull Context context, @Nullable ArrayList<Label> labels) {
        SpannableStringBuilder labelsText = new SpannableStringBuilder("");
        if (labels == null) {
            return labelsText;
        }
        for (int i = 0; i < labels.size(); i++) {
            Label label = (Label) labels.get(i);
            int start = labelsText.length();
            labelsText.append(label.getName());
            labelsText.setSpan(new IssueLabelSpan(context, label), start, label.getName().length() + start, 0);
        }
        return labelsText;
    }
}
