package com.open.testc.util;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import com.open.testc.inject.Autowired;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by liuml on 2021/4/14 19:40
 */
public class InjectUtil {

    public static void InjectView(Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();
        try {
            //1.遍历这个activity里面所有的GetIntentField 注解
            //getDeclaredFields  和 getFields区别  getDeclaredFields()返回Class中所有的字段，包括私有字段
//        getFields  只返回公共字段，即有public修饰的字段。
            Field[] declaredFields = aClass.getDeclaredFields();
            Bundle bundle = activity.getIntent().getExtras();
            if (bundle == null) {
                return;
            }
            for (Field field : declaredFields) {
                Autowired annotation = field.getAnnotation(Autowired.class);
                //2. 找到注解后获取他的值
                if (field.isAnnotationPresent(Autowired.class)) {
                    //获得key
                    String key = TextUtils.isEmpty(annotation.value()) ? field.getName() : annotation.value();

                    if (bundle.containsKey(key)) {
                        //获取传输的值
                        Object obj = bundle.get(key);

                        //获得数组单个元素类型 下面需要用到
                        Class<?> componentType = field.getType().getComponentType();
                        //判断如果是Parcelable[] 素组
                        if (field.getType().isArray() && Parcelable.class.isAssignableFrom(componentType)) {
                            //创建对应类型并拷贝
                            Object[] objs = (Object[]) obj;//强转
                            //拷贝数据  (Class<? extends Object[]>) field.getType())   通过getType 获取真正的反射类型
                            Object[] objects = Arrays.copyOf(objs, objs.length, (Class<? extends Object[]>) field.getType());
//                            Object[] objects = Arrays.copyOf(objs, objs.length, Parcelable[].class);
                            obj = objects;//赋值
                        }

                        //3. 反射设置属性的值
                        field.setAccessible(true);
//                    Class<?> type = field.getType();
                        field.set(activity, obj);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();

        }
    }
} 