package pk.onlinebazaar.helpers;

import java.lang.reflect.Field;
import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	private int mYear;
	private int mMonth;
	private int mDay;
	
	public interface DataSetDialogListener {
        void onFinish(String inputText);
    }
	
	public DatePickerFragment() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DATE);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		DatePickerDialog datePickerDialog = customDatePicker();
		return datePickerDialog;
	}
	
	private DatePickerDialog customDatePicker() {
		
		DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, mYear, mMonth, mDay);
		try {
			Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
			for (Field datePickerDialogField : datePickerDialogFields) {
				if (datePickerDialogField.getName().equals("mDatePicker")) {
					datePickerDialogField.setAccessible(true);
					DatePicker datePicker = (DatePicker) datePickerDialogField
							.get(dpd);
					Field datePickerFields[] = datePickerDialogField.getType()
							.getDeclaredFields();
					for (Field datePickerField : datePickerFields) {
						if ("mDayPicker".equals(datePickerField.getName())
								|| "mDaySpinner".equals(datePickerField
										.getName())) {
							datePickerField.setAccessible(true);
							Object dayPicker = new Object();
							dayPicker = datePickerField.get(datePicker);
							((View) dayPicker).setVisibility(View.GONE);
						}
					}
				}
			}
		} catch (Exception ex) {
			
		}
		return dpd;
	}
	
	public void onDateSet(DatePicker view, int year, int month, int day) {
		mYear = year;
		mMonth = month;
		mDay = day;
		int localMonth = (mMonth + 1);
		String monthString = localMonth < 10 ? "0" + localMonth : Integer
				.toString(localMonth);
		String localYear = Integer.toString(mYear).substring(2);
		
		DataSetDialogListener activity = (DataSetDialogListener) getActivity();
		activity.onFinish(new StringBuilder()
		// Month is 0 based so add 1
		.append(monthString).append("/").append("20").append(localYear).toString());
	}
	
	
}
