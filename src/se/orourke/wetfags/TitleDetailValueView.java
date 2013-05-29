package se.orourke.wetfags;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleDetailValueView extends RelativeLayout {
	private TextView title;
	private TextView detail;
	private TextView value;
	
	private void init(Context context)
	{
		LayoutInflater.from(context).inflate(R.layout.title_detail_value_layout, this, true);
		title = (TextView) findViewById(R.id.title);
		detail = (TextView) findViewById(R.id.detail);
		value = (TextView) findViewById(R.id.value);
	}
	
	private void setIfNotNull(TextView view, TypedArray attrs, int attribute)
	{
		if (view == null)
			return;
		CharSequence cs = attrs.getText(attribute);
		if (cs != null)
			view.setText(cs);
	}
	
	private void set_attrs(Context context, AttributeSet attrs) {
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TitleDetailValueView, 0, 0);

		try {
			setIfNotNull(title, a, R.styleable.TitleDetailValueView_titleText);
			setIfNotNull(detail, a, R.styleable.TitleDetailValueView_detailText);
			setIfNotNull(value, a, R.styleable.TitleDetailValueView_valueText);
			title.setVisibility(a.getBoolean(
					R.styleable.TitleDetailValueView_hideTitle, false) ? GONE
					: VISIBLE);
		} finally {
			a.recycle();
		}
	}
	
	public TitleDetailValueView(Context context) {
		super(context);
		init(context);
	}

	public TitleDetailValueView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		set_attrs(context, attrs);
	}

	public TitleDetailValueView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
		set_attrs(context, attrs);
	}

	public CharSequence getTitle() {
		return title.getText();
	}

	public void setTitle(CharSequence title) {
		this.title.setText(title);
	}

	public CharSequence getDetail() {
		return detail.getText();
	}

	public void setDetail(CharSequence detail) {
		this.detail.setText(detail);
	}

	public void setDetail(int resid) {
		this.detail.setText(resid);
	}

	public CharSequence getValue() {
		return value.getText();
	}

	public void setValue(CharSequence value) {
		this.value.setText(value);
	}
	
	public void setValue(int resid)
	{
		this.value.setText(resid);
	}

	public boolean getHideTitle()
	{
		return title.isShown();
	}
	
	public void setHideTitle(boolean hidden)
	{
		title.setVisibility(hidden ? GONE : VISIBLE);
	}
}
