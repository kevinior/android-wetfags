package se.orourke.wetfags;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class MainActivity extends Activity implements OnCheckedChangeListener, OnEditorActionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		RadioGroup monthYearKgGroup = (RadioGroup) findViewById(R.id.monthsYearsKgRadioGroup);
		monthYearKgGroup.setOnCheckedChangeListener(this);
		EditText weightAgeEditText = (EditText) findViewById(R.id.weightAgeEditText);
		weightAgeEditText.setOnEditorActionListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateValues();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/** Updates the displayed values */
	private void updateValues() {
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

		if (weight == -1) {
			// show the unknown value for all of them
			setTextViewText(R.id.w_value, R.string.unknown_val);
			setTextViewText(R.id.e_value, R.string.unknown_val);
			setTextViewText(R.id.f_value, R.string.unknown_val);
			setTextViewText(R.id.a_value, R.string.unknown_val);
			setTextViewText(R.id.g_value, R.string.unknown_val);
			setTextViewText(R.id.s_iv_value, R.string.unknown_val);
			setTextViewText(R.id.s_pr_value, R.string.unknown_val);
		} else {
			// recalculate and display
			setTextViewString(R.id.w_value, String.format(getString(R.string.w_format), weight));
			
			int energy = 4*weight;
			setTextViewString(R.id.e_value, String.format(getString(R.string.e_format), energy));
			
			if (age < 0.0)
				setTextViewText(R.id.t_value, R.string.unknown_val);
			else
			{
				double tube = (age/4.0) + 4.0;
				setTextViewString(R.id.t_value, String.format(getString(R.string.t_format), Math.round( tube * 2.0 ) / 2.0));
			}
			
			int fluids_low = 10*weight;
			int fluids_high = 20*weight;
			setTextViewString(R.id.f_value, String.format(getString(R.string.f_format), fluids_low, fluids_high));
			
			double adrenaline_ug = 0.010*weight; // mg
			double adrenaline_ml = adrenaline_ug / 0.1; // 0.1 mg/ml
			setTextViewString(R.id.a_value, String.format(getString(R.string.a_format), adrenaline_ml));
			
			int glucose = 2*weight;
			setTextViewString(R.id.g_value, String.format(getString(R.string.g_format), glucose));
			
			double stesolid_iv = 0.25*weight;
			double stesolid_pr = 0.5*weight;
			setTextViewString(R.id.s_iv_value, String.format(getString(R.string.s_iv_format), stesolid_iv));
			setTextViewString(R.id.s_pr_value, String.format(getString(R.string.s_pr_format), stesolid_pr));
			
			double albuterol = 2.5;
			if (age >= 5.0) albuterol = 5.0;
			setTextViewString(R.id.a2_value, String.format(getString(R.string.a2_format), albuterol));
			
			double atropine = 0.020 * weight;
			setTextViewString(R.id.a4_value, String.format(getString(R.string.a4_format), atropine));
			
			int amiodarone = 5 * weight;
			setTextViewString(R.id.a5_value, String.format(getString(R.string.a5_format), amiodarone));
		}
	}

	private void setTextViewText(int view_id, int string_id)
	{
		TextView v = (TextView) findViewById(view_id);
		v.setText(string_id);
	}
	
	private void setTextViewString(int view_id, String s)
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
