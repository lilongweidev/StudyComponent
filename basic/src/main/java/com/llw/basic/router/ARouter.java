package com.llw.basic.router;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

/**
 * 路由核心类
 */
public class ARouter {

    @SuppressLint("StaticFieldLeak")
    private static final ARouter aRouter = new ARouter();
    private final Map<String, Class<? extends Activity>> map;
    private Context context;

    private ARouter() {
        map = new HashMap<>();
    }

    public static ARouter getInstance() {
        return aRouter;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        this.context = context;
        //执行生成的工具类中的方法  将Activity的类对象加入到路由表中
        List<String> classNames = getClassName();
        for (String className : classNames) {
            try {
                Class<?> utilClass = Class.forName(className);
                if (IRouter.class.isAssignableFrom(utilClass)) {
                    IRouter iRouter = (IRouter) utilClass.newInstance();
                    iRouter.putActivity();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 添加Activity
     * @param key 注解中的值 例如  "main/MainActivity"
     * @param clazz 目标Activity
     */
    public void addActivity(String key, Class<? extends Activity> clazz) {
        //如果Key不会空，activity不为空，且map中没有这个key
        if (key != null && clazz != null && !map.containsKey(key)) {
            map.put(key, clazz);
        }
    }

    /**
     * 跳转Activity
     * @param key 注解中的值 例如  "main/MainActivity"
     */
    public void jumpActivity(String key) {
        jumpActivity(key, null);
    }

    /**
     * 跳转Activity 带参数
     * @param key 注解中的值 例如  "main/MainActivity"
     * @param bundle 参数包
     */
    public void jumpActivity(String key, Bundle bundle) {
        Class<? extends Activity> aClass = map.get(key);
        if (aClass == null) {
            return;
        }
        Intent intent = new Intent(context, aClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
    }

    /**
     * 通过包名获取这个包下面的所有的类名
     */
    private List<String> getClassName() {
        //创建一个class对象的集合
        List<String> classList = new ArrayList<>();
        try {
            DexFile df = new DexFile(context.getPackageCodePath());
            Enumeration<String> entries = df.entries();
            while (entries.hasMoreElements()) {
                String className = entries.nextElement();
                if (className.contains("com.llw.util")) {
                    classList.add(className);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classList;
    }
}
