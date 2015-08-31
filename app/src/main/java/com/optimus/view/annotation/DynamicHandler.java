package com.optimus.view.annotation;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by RenTianzhu on 2015/8/31.
 */
public class DynamicHandler implements InvocationHandler {
    private WeakReference<Object> handlerRef;
    private final HashMap<String,Method> methodHashMap = new HashMap<String,Method>(1);
    public DynamicHandler(Object handler){
        this.handlerRef = new WeakReference<Object>(handler);
    }
    public void addMethod(String name,Method method){
        methodHashMap.put(name,method);
    }
    public Object getHandler(){
        return handlerRef.get();
    }
    public void setHandler(Object handler){
        this.handlerRef = new WeakReference<Object>(handler);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object handler = handlerRef.get();
        if(handler != null){
            String methodName = method.getName();
            method  = methodHashMap.get(methodName);
            if(method != null){
                return method.invoke(handler,args);
            }
        }
        return null;
    }
}
