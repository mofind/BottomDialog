package com.mofind.widget;

import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mofind.bottomdialog.R;

import utils.ActivityUtils;

public class BottomDialog implements OnClickListener {

	private Context mContext;
	private Dialog dialog;
	private ViewGroup parentLayout, contentLayout;
	private ScrollView sv;

	private View shadowView, dialogView, defLayout, btnLayout;
	private TextView titleTv;
	private Button submitBtn, cancelBtn;
	private ListView lv;

	private TextView defTv;
	private EditText defEt;
	private ImageView leftBtn, rightBtn;

	/**
	 * 是否显示取消按钮
	 */
	private boolean isShowCancelBtn = true;

	/**
	 * 是否可以点击屏幕取消
	 */
	private boolean isCanceledOnTouchOutside = true;

	/**
	 * 是否可以动态改变Dialog布局大小
	 */
	private boolean isAdjustResize = true;

	private boolean isDefEtToUpperCase = false;

	public BottomDialog(Context context) {
		init(context);
	}

	public BottomDialog(Context context, boolean isAdjustResize) {
		this.isAdjustResize = isAdjustResize;
		init(context);
	}

	public Dialog getDlg() {
		return dialog;
	}

	private void init(Context context) {
		mContext = context;
		parentLayout = (ViewGroup) View.inflate(mContext, R.layout.widget_bottom_dialog, null);
		initDialog(parentLayout);
		initView();
	}

	private void initDialog(View parentView) {
		dialog = new Dialog(mContext, R.style.MyDialogTheme);
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.dialogAnim); // 添加动画

		// debug
		// window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
		// Utility.getScreenHeight(mContext));

		if (isAdjustResize)
			window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		dialog.setContentView(parentView);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);
	}

	public void initView() {
		shadowView = dialog.findViewById(R.id.shadowView);
		dialogView = dialog.findViewById(R.id.dialogView);
		sv = (ScrollView) dialog.findViewById(R.id.sv);
		contentLayout = (ViewGroup) dialog.findViewById(R.id.contentLayout);
		submitBtn = (Button) dialog.findViewById(R.id.submitBtn);
		cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
		titleTv = (TextView) dialog.findViewById(R.id.titleTv);
		rightBtn = (ImageView) dialog.findViewById(R.id.rightBtn);
		leftBtn = (ImageView) dialog.findViewById(R.id.leftBtn);
		defLayout = (LinearLayout) dialog.findViewById(R.id.defLayout);
		btnLayout = (LinearLayout) dialog.findViewById(R.id.btnLayout);
		defTv = (TextView) defLayout.findViewById(R.id.defTv);
		defEt = (EditText) defLayout.findViewById(R.id.defEt);
		lv = (ListView) dialog.findViewById(R.id.lv);
		dialogView.setOnClickListener(this);
		shadowView.setOnClickListener(this);
		submitBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
		// 是否显示取消按钮
		cancelBtn.setVisibility(isShowCancelBtn ? View.VISIBLE : View.GONE);
		// contentView默认等于defLayout
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shadowView:
			if (isCanceledOnTouchOutside)
				dismiss();
			break;
		case R.id.cancelBtn:
			dismiss();
			break;
		case R.id.dialogView:
			// nothing
			break;
		case R.id.submitBtn:
			doSubmit();
			break;
		default:
			break;
		}
	}

	private void doSubmit() {
		String value = defEt.getText().toString().trim();
		if (onSubmitClickListener != null)
			onSubmitClickListener.onSubmitClick(this, isDefEtToUpperCase ? value.toUpperCase() : value);
		ActivityUtils.colseInputMethod(mContext, defEt);
	}

	public TextView getTitleView() {
		return titleTv;
	}

	public BottomDialog setView(View view) {
		return setContentView(view);
	}

	public BottomDialog setContentView(View view) {
		if (view != null) {
			defLayout.setVisibility(View.GONE);
			contentLayout.addView(view);
		} else {
			defLayout.setVisibility(View.VISIBLE);
		}
		return this;
	}

	public BottomDialog setTitle(CharSequence text) {
		titleTv.setText(text);
		return this;
	}

	public BottomDialog setDefTv(CharSequence text) {
		defTv.setText(text);
		return this;
	}

	public BottomDialog setDefHint(CharSequence text) {
		defEt.setHint(text);
		return this;
	}

	public BottomDialog hideButton() {
		btnLayout.setVisibility(View.GONE);
		return this;
	}

	public BottomDialog show() {
		dialog.show();
		return this;
	}

	public void dismiss() {
		if (defEt != null)
			ActivityUtils.colseInputMethod(mContext, defEt);
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	/**
	 * dialog Title上右侧的按钮
	 *
	 * @param resId
	 *            图片资源id，默认值为0，显示delete的资源
	 * @param l
	 */
	public BottomDialog showTitleRight(int resId, OnClickListener l) {
		if (resId != 0)
			rightBtn.setImageResource(resId);
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setOnClickListener(l);
		return this;
	}

	/**
	 * dialog Title上左侧的按钮
	 *
	 * @param resId
	 *            图片资源id，默认值为0，显示back的资源
	 * @param l
	 */
	public BottomDialog showTitleLeft(int resId, OnClickListener l) {
		if (resId != 0)
			leftBtn.setImageResource(resId);
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setOnClickListener(l);
		return this;
	}

	public void showLeftBtn(boolean isShow) {
		leftBtn.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

	public boolean isShowCancelBtn() {
		return isShowCancelBtn;
	}

	public BottomDialog setShowCancelBtn(boolean isShowCancelBtn) {
		this.isShowCancelBtn = isShowCancelBtn;
		cancelBtn.setVisibility(isShowCancelBtn ? View.VISIBLE : View.GONE);
		return this;
	}

	public TextView getDefTv() {
		return defTv;
	}

	public EditText getDefEt() {
		return defEt;
	}

	public boolean isCanceledOnTouchOutside() {
		return isCanceledOnTouchOutside;
	}

	/**
	 * setadapter
	 *
	 * @param resdatas
	 */
	public BottomDialog setAdapterData(CharSequence[] resdatas, AdapterView.OnItemClickListener l, int index) {
		if (resdatas == null)
			return this;
		sv.setVisibility(View.GONE);
		lv.setVisibility(View.VISIBLE);
		lv.setAdapter(new SingleChoiceAdapter(mContext, resdatas, index));
		lv.setOnItemClickListener(l);
		return this;
	}

	public BottomDialog setCanceledOnTouchOutside(boolean isCanceledOnTouchOutside) {
		this.isCanceledOnTouchOutside = isCanceledOnTouchOutside;
		return this;
	}

	public OnSubmitClickListener getOnSubmitClickListener() {
		return onSubmitClickListener;
	}

	public BottomDialog setOnSubmitClickListener(OnSubmitClickListener onSubmitClickListener) {
		this.onSubmitClickListener = onSubmitClickListener;
		return this;
	}

	public BottomDialog setOnSubmitClickListener(String btnName, OnSubmitClickListener onSubmitClickListener) {
		submitBtn.setText(btnName);
		setOnSubmitClickListener(onSubmitClickListener);
		return this;
	}

	private OnSubmitClickListener onSubmitClickListener;

	public interface OnSubmitClickListener {
		void onSubmitClick(BottomDialog dlg, String value);
	}

	public boolean isAdjustResize() {
		return isAdjustResize;
	}

	public BottomDialog setAdjustResize(boolean isAdjustResize) {
		this.isAdjustResize = isAdjustResize;
		return this;
	}

	public static void showSimpleDialog(Context context, String title, String tvStr, String hint, boolean isShowCancelBtn,
			OnSubmitClickListener onSubmitClickListener) {
		new BottomDialog(context).setTitle(title).setDefTv(tvStr).setDefHint(hint).setCanceledOnTouchOutside(true).setShowCancelBtn(isShowCancelBtn)
				.setOnSubmitClickListener(onSubmitClickListener).show();
	}

	public static void showCustomViewDialog(Context context, String title, View contentView, boolean isShowCancelBtn,
			OnSubmitClickListener onSubmitClickListener) {
		new BottomDialog(context).setTitle(title).setView(contentView).setCanceledOnTouchOutside(true).setShowCancelBtn(isShowCancelBtn)
				.setOnSubmitClickListener(onSubmitClickListener).show();
	}

	/**
	 * @param @param context
	 * @param @param title
	 * @param @param contentView
	 * @param @param isShowCancelBtn
	 * @param @param onSubmitClickListener
	 * @param @return
	 * @Description:新建一个简单的dialog 并且返回dialog对象 方便外部关闭该弹出框
	 */
	public static Dialog showSimpleDialog(Context context, String title, View contentView, boolean isShowCancelBtn,
			OnSubmitClickListener onSubmitClickListener) {
		BottomDialog a = new BottomDialog(context).setTitle(title).setView(contentView).setCanceledOnTouchOutside(true)
				.setShowCancelBtn(isShowCancelBtn).setOnSubmitClickListener(onSubmitClickListener).show();
		return a.getDlg();
	}

	public boolean isDefEtToUpperCase() {
		return isDefEtToUpperCase;
	}

	public BottomDialog setDefEtToUpperCase(boolean isDefEtToUpperCase) {
		this.isDefEtToUpperCase = isDefEtToUpperCase;
		if (isDefEtToUpperCase)
			defEt.setInputType(InputType.TYPE_CLASS_TEXT);
		return this;
	}

	public Button getSubmitBtn() {
		return submitBtn;
	}

	public Button getCancelBtn() {
		return cancelBtn;
	}

	private class SingleChoiceAdapter extends BaseAdapter {

		private Context ct;
		private CharSequence[] list;
		public int curIndex = -1;

		private boolean switchInner = false; // 可内部点击,但setOnItemClick会失效

		public boolean isStyle_rightRButton = false; // 选中样式,true:右边cb,false:左侧"竖线"

		public SingleChoiceAdapter(Context ct, CharSequence[] list, int defIndex) {
			// TODO Auto-generated constructor stub
			this.ct = ct;
			this.list = list;
			this.curIndex = defIndex;
		}

		public SingleChoiceAdapter(Context ct, CharSequence[] list, int defIndex, boolean switchInner) {
			// TODO Auto-generated constructor stub
			this.ct = ct;
			this.list = list;
			this.curIndex = defIndex;
			this.switchInner = switchInner;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list == null ? 0 : list.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			Holder h;
			if (convertView == null) {
				convertView = LayoutInflater.from(ct).inflate(R.layout.simple_list_single_sel, null);
				h = new Holder();
				h.text1 = (TextView) convertView.findViewById(R.id.text1);
				h.vSel = convertView.findViewById(R.id.vSel);
				h.cb_dlg = (RadioButton) convertView.findViewById(R.id.cb_dlg);

				// 初始化-选中样式
				if (isStyle_rightRButton) {
					h.cb_dlg.setVisibility(View.VISIBLE);
					h.vSel.setVisibility(View.INVISIBLE);
				} else {
					h.vSel.setVisibility(View.VISIBLE);
					h.cb_dlg.setVisibility(View.INVISIBLE);
				}

				convertView.setTag(h);
			} else
				h = (Holder) convertView.getTag();

			// 刷ui
			h.text1.setText(list[position]);

			// 选中-样式
			boolean isSelected = position == curIndex;
			if (isStyle_rightRButton)
				h.cb_dlg.setChecked(isSelected);
			else
				h.vSel.setVisibility(isSelected ? View.VISIBLE : View.INVISIBLE);

			// debug
			if (switchInner)
				convertView.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						curIndex = position;
						notifyDataSetChanged();
					}
				});

			return convertView;
		}

		class Holder {
			TextView text1;
			View vSel;
			RadioButton cb_dlg;
		}

	}
}
