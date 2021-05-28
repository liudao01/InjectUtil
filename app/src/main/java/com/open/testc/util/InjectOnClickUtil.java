package com.open.testc.util;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.open.testc.onclickInject.ClickEvent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


/**
 * Created by liuml on 2021/4/14 19:40
 */
public class InjectOnClickUtil {

    private static String TAG = "InjectOnClickUtil";

//    public static void inject(final Activity target) {
//        if (target == null) {
//            return;
//        }
//        Class<? extends Activity> clz = target.getClass();
//        Method[] declaredMethods = clz.getDeclaredMethods();
//        for (final Method method : declaredMethods) {
//            if (method.isAnnotationPresent(ClickEvent.class)) {
//                ClickEvent annotation = method.getAnnotation(ClickEvent.class);
//                int[] resIds = annotation.value();
//                for (int resId : resIds) {
//                    final View view = target.findViewById(resId);
//                    final Object proxyInstance = Proxy.newProxyInstance(InjectOnClickUtil.class.getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
//                        @Override
//                        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
//                            if (method != null) {
//
//                                return method.invoke(target, view);
//                            }
//                            return m.invoke(target, view);
//                        }
//                    });
//                    view.setOnClickListener((View.OnClickListener) proxyInstance);
//                }
//            }
//        }
//    }

    public static void InjectView(Activity activity) {

        Class<? extends Activity> aClass = activity.getClass();
        //1.遍历这个activity里面所有的GetIntentField 注解
        //getDeclaredFields  和 getFields区别  getDeclaredFields()返回Class中所有的字段，包括私有字段
        Method[] methods = aClass.getDeclaredMethods();
        //判断是否存在成员方法
        if (methods == null) {
            return;
        }
////
        for (Method method : methods) {
            //方法上有Onclick注解
            if (method.isAnnotationPresent(ClickEvent.class)) {
                ClickEvent annotation = method.getAnnotation(ClickEvent.class);
                int[] ids = annotation.value();
                method.setAccessible(true);
                for (int id : ids) {
                    View view = activity.findViewById(id);
                    Log.d(TAG, "InjectView: view = " + view.getId());
//                        method.setAccessible(true);//设置权限
                    //获取onclicklstener实例对象
                    final Object listener = Proxy.newProxyInstance(activity.getClassLoader(), new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method m, Object[] objects) throws Throwable {
//                                proxy实际就是代理对象   method 执行哪个方法  objects 方法里面的参数
                            //代理实际执行方法,真正执行的方法使用反射

                            //第一个参数是接口的实例-在哪个对象上执行这个方法,第二个是参数
                            if (method != null) {
                                return method.invoke(activity, objects);
                            } else {
                                return m.invoke(activity, objects);
                            }
                        }
                    });
                    view.setOnClickListener((View.OnClickListener) listener);
                }
            }

        }

//        Class<? extends Activity> clz = activity.getClass();
//        Method[] declaredMethods = clz.getDeclaredMethods();
//        for (Method method : declaredMethods) {
//            Annotation[] annotations = method.getAnnotations();
//            for (Annotation annotation : annotations) {
//                Class<? extends Annotation> annotationType = annotation.annotationType();
//                if (annotationType.isAnnotationPresent(ClickEvent.class)) {
//
////                    EventType eventType = annotationType.getAnnotation(EventType.class);
////                    Class listenerType = eventType.listenerType();
////                    String listenerSetter = eventType.listenerSetter();
//                    try {
//                        ClickEvent annotation1 = method.getAnnotation(ClickEvent.class);
//                        Method valueMethod = annotationType.getDeclaredMethod("value");
//                        int[] ids = (int[]) valueMethod.invoke(annotation);
//                        method.setAccessible(true);
//                        ListenerInvocationHandler invocationHandler = new ListenerInvocationHandler(method, activity);
//                        Object proxyInstance = Proxy.newProxyInstance(activity.getClassLoader(), new Class[]{View.OnClickListener.class}, invocationHandler);
//                        for (int id : ids) {
//                            Log.d(TAG, "InjectView: id = "+id);
//                            View view = activity.findViewById(id);
//                            Method setter = view.getClass().getMethod("setOnClickListener", View.OnClickListener.class);
//                            setter.invoke(view, proxyInstance);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }


    }

    static class ListenerInvocationHandler<T> implements InvocationHandler {

        private Method method;

        private T target;

        public ListenerInvocationHandler(Method method, T target) {
            this.method = method;
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return this.method.invoke(target, args);
        }
    }
}