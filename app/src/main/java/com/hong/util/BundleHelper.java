package com.hong.util;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import java.io.Serializable;
import java.util.ArrayList;

public class BundleHelper {
    private Bundle bundle = new Bundle();

    private BundleHelper() {
    }

    public static BundleHelper builder() {
        return new BundleHelper();
    }

    public BundleHelper put(@NonNull String key, @Nullable String value) {
        this.bundle.putString(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable char value) {
        this.bundle.putChar(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable int value) {
        this.bundle.putInt(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable boolean value) {
        this.bundle.putBoolean(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable long value) {
        this.bundle.putLong(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable float value) {
        this.bundle.putFloat(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable short value) {
        this.bundle.putShort(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable double value) {
        this.bundle.putDouble(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable byte value) {
        this.bundle.putByte(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable CharSequence value) {
        this.bundle.putCharSequence(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable Parcelable value) {
        this.bundle.putParcelable(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable String[] value) {
        this.bundle.putStringArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable char[] value) {
        this.bundle.putCharArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable int[] value) {
        this.bundle.putIntArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable boolean[] value) {
        this.bundle.putBooleanArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable long[] value) {
        this.bundle.putLongArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable float[] value) {
        this.bundle.putFloatArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable short[] value) {
        this.bundle.putShortArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable double[] value) {
        this.bundle.putDoubleArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable byte[] value) {
        this.bundle.putByteArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable CharSequence[] value) {
        this.bundle.putCharSequenceArray(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable Parcelable[] value) {
        this.bundle.putParcelableArray(key, value);
        return this;
    }

    public BundleHelper putStringList(@NonNull String key, @Nullable ArrayList<String> value) {
        this.bundle.putStringArrayList(key, value);
        return this;
    }

    public BundleHelper putIntegerList(@NonNull String key, @Nullable ArrayList<Integer> value) {
        this.bundle.putIntegerArrayList(key, value);
        return this;
    }

    public BundleHelper putParcelableList(@NonNull String key, @Nullable ArrayList<? extends Parcelable> value) {
        this.bundle.putParcelableArrayList(key, value);
        return this;
    }

    public BundleHelper putCharSequenceList(@NonNull String key, @Nullable ArrayList<CharSequence> value) {
        this.bundle.putCharSequenceArrayList(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable Size value) {
        this.bundle.putSize(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable SizeF value) {
        this.bundle.putSizeF(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable IBinder value) {
        this.bundle.putBinder(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable Serializable value) {
        this.bundle.putSerializable(key, value);
        return this;
    }

    public BundleHelper put(@NonNull String key, @Nullable SparseArray<? extends Parcelable> value) {
        this.bundle.putSparseParcelableArray(key, value);
        return this;
    }

    public Bundle build() {
        Parcel parcel = Parcel.obtain();
        this.bundle.writeToParcel(parcel, 0);
        if (parcel.dataSize() <= 524288) {
            return this.bundle;
        }
        this.bundle.clear();
        throw new IllegalArgumentException("bundle data is too large, please reduce date size to avoid Exception");
    }
}
