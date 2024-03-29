package com.optimus.view.viewinject;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ViewInjectUtils
{
	private static final String METHOD_SET_CONTENTVIEW = "setContentView";
	private static final String METHOD_FIND_VIEW_BY_ID = "findViewById";

	public static void inject(Activity activity)
	{
		Log.e("TAG", "inject");
		injectContentView(activity);
		injectViews(activity);
		injectEvents(activity);
	}


	/**
	 * 注入所有的控件
	 * 
	 * @param activity
	 */
	private static void injectViews(Activity activity)
	{
		Class<? extends Activity> clazz = activity.getClass();
		Field[] fields = clazz.getDeclaredFields();
		// 遍历所有成员变量
		for (Field field : fields)
		{
			Log.e("TAG", field.getName()+"");
			ViewInject viewInjectAnnotation = field
					.getAnnotation(ViewInject.class);
			if (viewInjectAnnotation != null)
			{
				int viewId = viewInjectAnnotation.value();
				if (viewId != -1)
				{
					Log.e("TAG", viewId+"");
					// 初始化View
					try
					{
						Method method = clazz.getMethod(METHOD_FIND_VIEW_BY_ID,
								int.class);
						Object resView = method.invoke(activity, viewId);
						field.setAccessible(true);
						field.set(activity, resView);
					} catch (Exception e)
					{
						e.printStackTrace();
					}

				}
			}

		}

	}

	/**
	 * 注入主布局文件
	 * 
	 * @param activity
	 */
	private static void injectContentView(Activity activity)
	{
		Class<? extends Activity> clazz = activity.getClass();
		// 查询类上是否存在ContentView注解
		ContentView contentView = clazz.getAnnotation(ContentView.class);
		if (contentView != null)// 存在
		{
			int contentViewLayoutId = contentView.value();
			try
			{
				Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW,
						int.class);
				method.setAccessible(true);
				method.invoke(activity, contentViewLayoutId);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void injectEvents(Activity activity){
		Class<? extends Activity> clazz = activity.getClass();
		Method[] methods = clazz.getMethods();
		for(Method method : methods){
			Annotation[] annotations = method.getAnnotations();
			for(Annotation annotation : annotations){
				Class<? extends Annotation> annotationType = annotation.annotationType();
				EventBase eventBaseAnnotation = annotationType.getAnnotation(EventBase.class);
				if(eventBaseAnnotation != null){
					Class<?> listenerType = eventBaseAnnotation.listenerType();
					String listenerSetter = eventBaseAnnotation.listenerSetter();
					String methodName = eventBaseAnnotation.methodName();

					try {
						Method aMethod = annotationType.getDeclaredMethod("value");
						int[] viewIds = (int[]) aMethod.invoke(annotation,null);
						DynamicHandler handler = new DynamicHandler(activity);
						handler.addMethod(methodName,method);
						Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(),new Class<?>[] {listenerType},handler);
						for(int viewId : viewIds){
							View view = activity.findViewById(viewId);
							Method setEventListenerMethod = view.getClass().getMethod(listenerSetter,listenerType);
							setEventListenerMethod.invoke(view,listener);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
	}
}
