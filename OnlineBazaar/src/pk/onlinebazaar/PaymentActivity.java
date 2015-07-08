package pk.onlinebazaar;

import pk.onlinebazaar.helpers.DatePickerFragment;
import pk.onlinebazaar.helpers.DatePickerFragment.DataSetDialogListener;
import pk.onlinebazaar.helpers.OrderInitializer;
import pk.onlinebazaar.helpers.Rule;
import pk.onlinebazaar.helpers.Validator;
import pk.onlinebazaar.helpers.Validator.ValidationListener;
import pk.onlinebazaar.helpers.annotation.Regex;
import pk.onlinebazaar.helpers.annotation.Required;
import pk.onlinebazaar.helpers.annotation.TextRule;
import pk.onlinebazaar.model.CreditCard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PaymentActivity extends FragmentActivity implements
		DataSetDialogListener, OnClickListener, ValidationListener {

	@Required(order = 1)
	private EditText etPickADate;
	@Required(order = 2)
	private EditText etTotalAmount;
	@Required(order = 3)
	private EditText etNameOnCard;
	@Required(order = 4)
	@TextRule(order = 5, maxLength = 4, minLength= 3, message = "Enter atleast 3 digits and atmost 4 digits.")
	@Regex(order = 6, pattern = "[0-9]+", message = "Should contain only Numbers")
	private EditText etSecurityCode;
	@Required(order = 7)
	@TextRule(order = 8, maxLength = 16, minLength= 16, message = "Enter atleast 16 digits and atmost 16 digits.")
	@Regex(order = 9, pattern = "[0-9]+", message = "Should contain only Numbers")
	private EditText etCardNumber;
	private Button btnPay;
	
	private Validator validator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);

		Initializer();

		validator = new Validator(this);
		validator.setValidationListener(this);
		
		Intent shopCartIntent = getIntent();
		double Total = shopCartIntent.getDoubleExtra("TotalAmount", 0);

		etTotalAmount.setText(String.valueOf(Total));

		final DialogFragment newFragment = new DatePickerFragment();

		etPickADate.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					newFragment.show(getSupportFragmentManager(), "datePicker");
			}
		});
		
		btnPay.setOnClickListener(this);
		
	}

	public void Initializer() {
		etPickADate = (EditText) findViewById(R.id.dpExpireDate);
		etTotalAmount = (EditText) findViewById(R.id.edtAmount);
		etNameOnCard = (EditText) findViewById(R.id.edtNameOnCard);
		etCardNumber = (EditText) findViewById(R.id.edtCardNumber);
		etSecurityCode = (EditText) findViewById(R.id.edtSecurityCode);
		btnPay = (Button) findViewById(R.id.btnCC);
	}

	@Override
	public void onFinish(String inputText) {
		etPickADate.setText(inputText);
	}
	
	@Override
	public void onClick(View v) {
		validator.validateAsync();
	}

	@Override
	public void onValidationSucceeded() {
		CreditCard creditCard = new CreditCard();
		creditCard.setNameOnCard(etNameOnCard.getText().toString());
		creditCard.setCardNumber(etCardNumber.getText().toString());
		creditCard.setSecurityCode(etSecurityCode.getText().toString());
		creditCard.setExpiryDate(etPickADate.getText().toString());
		new OrderInitializer(this, creditCard);
	}

	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {
		String message = failedRule.getFailureMessage();

		if (failedView instanceof EditText) {
			failedView.requestFocus();
			((EditText) failedView).setError(message);
		} else {
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		}
	}

}
