package se.orourke.wetfags;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity implements OnCheckedChangeListener, OnEditorActionListener {
	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View)arg1);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) container.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            int resId = 0;
            switch (position) {
	            case 0:
	                resId = R.layout.wetfags_layout;
	                break;
	            case 1:
	                resId = R.layout.parameters_layout;
	                break;
            }
            View view = inflater.inflate(resId, null);
            ((ViewPager) container).addView(view, 0);
            return view;
        }
		
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		MyPagerAdapter adapter = new MyPagerAdapter();
	    ViewPager myPager = (ViewPager) findViewById(R.id.resultspanelpager);
	    myPager.setAdapter(adapter);
	    myPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				updateValues();
			}
	    });
	    
		RadioGroup monthYearKgGroup = (RadioGroup) findViewById(R.id.monthsYearsKgRadioGroup);
		monthYearKgGroup.setOnCheckedChangeListener(this);
		EditText weightAgeEditText = (EditText) findViewById(R.id.weightAgeEditText);
		weightAgeEditText.setOnEditorActionListener(this);
	}

	/** Updates the displayed values */
	private void updateValues() {
		if (findViewById(R.id.w_formula) == null)
			return;
		
		EditText weightAgeEditText = (EditText) findViewById(R.id.weightAgeEditText);
		int value = -1;
		try {
			value = Integer.parseInt(weightAgeEditText.getText().toString());
		} catch (NumberFormatException e) {
			// ignore it, the default value is already invalid
		}
		
		RadioGroup monthYearKgGroup = (RadioGroup) findViewById(R.id.monthsYearsKgRadioGroup);
		int weight = -1;
		double age = -1.0;
		switch (monthYearKgGroup.getCheckedRadioButtonId()) {
			case R.id.monthsRadio:
				if ((value >= 1) && (value <= 12)) {
					weight = (value/2) + 4;
					age = value/12.0;
					setTextViewText(R.id.w_formula, R.string.w_lt_1_formula);
				}
				break;
				
			case R.id.yearsRadio:
				if ((value >= 1) && (value <= 5)) {
					weight = (value*2) + 8;
					age = (double)value;
					setTextViewText(R.id.w_formula, R.string.w_1_5_formula);
				}
				else if ((value >= 6) && (value <= 12)) {
					weight = (value*3) + 7;
					age = (double)value;
					setTextViewText(R.id.w_formula, R.string.w_6_12_formula);
				}
				break;
			
			case R.id.kgRadio:
				if (value >= 1)
					weight = value;
				break;
		}
		
		ViewPager myPager = (ViewPager) findViewById(R.id.resultspanelpager);
		switch (myPager.getCurrentItem())
		{
			case 0:
				updateWetfagsValues(weight, age);
				break;
			
			case 1:
				updateParametersValues(weight, age);
				break;
		}
	}
	
	private void updateWetfagsValues(int weight, double age) {
		if (findViewById(R.id.wetfags_rel_layout) == null)
			return;
		
		if (weight == -1) {
			setTextViewText(R.id.w_value, R.string.unknown_val);
			setTextViewText(R.id.e_value, R.string.unknown_val);
			setTextViewText(R.id.f_value, R.string.unknown_val);
			setTextViewText(R.id.a_value, R.string.unknown_val);
			setTextViewText(R.id.g_value, R.string.unknown_val);
			setTextViewText(R.id.s_iv_value, R.string.unknown_val);
			setTextViewText(R.id.s_pr_value, R.string.unknown_val);
		} else {
			setTextViewText(R.id.w_value, String.format(getString(R.string.w_format), weight));
			
			int energy = 4*weight;
			setTextViewText(R.id.e_value, String.format(getString(R.string.e_format), energy));
			
			if (age < 0.0)
				setTextViewText(R.id.t_value, R.string.unknown_val);
			else
			{
				double tube = (age/4.0) + 4.0;
				setTextViewText(R.id.t_value, String.format(getString(R.string.t_format), Math.round( tube * 2.0 ) / 2.0));
			}
			
			int fluids_low = 10*weight;
			int fluids_high = 20*weight;
			setTextViewText(R.id.f_value, String.format(getString(R.string.f_format), fluids_low, fluids_high));
			
			double adrenaline_ug = 0.010*weight; // mg
			double adrenaline_ml = adrenaline_ug / 0.1; // 0.1 mg/ml
			setTextViewText(R.id.a_value, String.format(getString(R.string.a_format), adrenaline_ml));
			
			int glucose = 2*weight;
			setTextViewText(R.id.g_value, String.format(getString(R.string.g_format), glucose));
			
			double stesolid_iv = 0.25*weight;
			double stesolid_pr = 0.5*weight;
			setTextViewText(R.id.s_iv_value, String.format(getString(R.string.s_iv_format), stesolid_iv));
			setTextViewText(R.id.s_pr_value, String.format(getString(R.string.s_pr_format), stesolid_pr));
			
			double albuterol = 2.5;
			if (age < 0.0)
				setTextViewText(R.id.a2_value, R.string.unknown_val);
			else
			{
				if (age >= 5.0) albuterol = 5.0;
				setTextViewText(R.id.a2_value, String.format(getString(R.string.a2_format), albuterol));
			}
			
			double atropine = 0.020 * weight;
			setTextViewText(R.id.a4_value, String.format(getString(R.string.a4_format), atropine));
			
			int amiodarone = 5 * weight;
			setTextViewText(R.id.a5_value, String.format(getString(R.string.a5_format), amiodarone));
		}
	}
	
	private void updateParametersValues(int weight, double age)
	{
		if (findViewById(R.id.parameters_rel_layout) == null)
			return;
		
		if (age < 0.0)
		{
			setTextViewText(R.id.breathing_value, R.string.unknown_val);
			setTextViewText(R.id.heart_value, R.string.unknown_val);
			setTextViewText(R.id.pressure_value, R.string.unknown_val);
		}
		else
		{
			int age_index = 3;
			if (age < 1)
				age_index = 0;
			else if (age < 6)
				age_index = 1;
			else if (age < 12)
				age_index = 2;
			setTextViewText(R.id.breathing_value, getResources().getStringArray(R.array.breathing_values)[age_index]);
			setTextViewText(R.id.heart_value, getResources().getStringArray(R.array.heart_values)[age_index]);
			setTextViewText(R.id.pressure_value, getResources().getStringArray(R.array.pressure_values)[age_index]);
		}
	}

	private void setTextViewText(int view_id, int string_id)
	{
		TextView v = (TextView) findViewById(view_id);
		v.setText(string_id);
	}
	
	private void setTextViewText(int view_id, String s)
	{
		TextView v = (TextView) findViewById(view_id);
		v.setText(s);
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		updateValues();
		hideKeyboard();
	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		updateValues();
		hideKeyboard();
		return true;
	}

	private void hideKeyboard()
	{
		View v = findViewById(R.id.weightAgeEditText);
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
}
