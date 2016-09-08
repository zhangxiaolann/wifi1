package com.liuguilin.only.utils;

import android.content.res.Resources;

/**
 * 粒子动画
 * Created by LGL on 2016/5/8.
 */
public class ParticleUtils {
    /**
     * 密度
     */
    public static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;

    public static int dp2px(int dp) {
        return Math.round(dp * DENSITY);
    }
}
