package com.optimus.view.annotation;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by RenTianzhu on 2015/8/31.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@EventBase(listenerType = View.OnClickListener.class,listenerSetter ="setOnClickListener",methodName = "onClick")
public @interface OnClick {
    int[] value();
}
