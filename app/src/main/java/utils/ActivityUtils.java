package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityUtils {

	/**
	 * 弹至某activity,如:主页
	 */
	public static void popTo(Context context, Class<?> main) {
		Intent intent = new Intent(context, main);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}
	
	public static void popTo(Context context, Intent intent) {
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent);
	}

	/**
	 * px转dp
	 */
	public static int px2dip(Context c, int px) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, c.getResources().getDisplayMetrics());
	}

	/**
	 * 获取手机屏幕宽度
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = getDisplayMetrics(context);
		return dm.widthPixels;
	}

	/**
	 * 获取手机屏幕高度
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = getDisplayMetrics(context);
		return dm.heightPixels;
	}

	private static DisplayMetrics getDisplayMetrics(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}

	/**
	 * 隐藏键盘
	 */
	public static void hideSoftInput(Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void colseInputMethod(Context context, View edit) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
	}

	/**
	 * 移动EditText光标至末尾
	 */
	public static void endEtCursor(EditText et) {
		String str = et.getEditableText().toString();
		if (str == null)
			str = "";
		et.setSelection(str.length());
	}

	/**
	 * 获取手机状态栏高度
	 */
	public static int getStatusBarHeight(Context context) {
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return statusBarHeight;
	}

	/**
	 * 添加span,spOld为"str0+str1",style1添加到str1
	 */
	public static void addSpan(Spannable spOld, String str0, String str1, CharacterStyle style1) {
		spOld.setSpan(style1, str0.length(), str0.length() + str1.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
	}

	/**
	 * 获取-复合文本,用于只有"两个子串" 参数:style1(str1的样式)
	 */
	public static Spannable getCompoundText(String str0, String str1, CharacterStyle style1) {
		return getCompoundText(new String[] { str0, str1 }, new CharacterStyle[] { null, style1 });
	}

	/**
	 * 获取-复合文本,两个数组维度需相同
	 */
	public static Spannable getCompoundText(String[] strs, CharacterStyle[] styles) {
		SpannableStringBuilder strTarget = new SpannableStringBuilder();
		int len = strs.length;
		for (int i = 0; i < len; i++) {
			SpannableString str = new SpannableString(strs[i]);
			str.setSpan(styles[i], 0, str.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			strTarget.append(str);
		}
		return strTarget;
	}

	/**
	 * 再按一次退出程序
	 */
	public static void exitSystem(Activity activity) {
		if (isExit == false) {
			isExit = true;
			Toast.makeText(activity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false;
				}
			}, 2000);
		} else {
			activity.finish();
		}
	}

	private static boolean isExit = false;

}
