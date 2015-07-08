package pk.onlinebazaar;

import pk.onlinebazaar.helpers.DatabaseHandler;
import pk.onlinebazaar.helpers.Rule;
import pk.onlinebazaar.helpers.ShipmentHandler;
import pk.onlinebazaar.helpers.Validator;
import pk.onlinebazaar.helpers.Validator.ValidationListener;
import pk.onlinebazaar.helpers.annotation.Regex;
import pk.onlinebazaar.helpers.annotation.Required;
import pk.onlinebazaar.helpers.annotation.TextRule;
import pk.onlinebazaar.model.Address;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class BillingDetailActivity extends Activity implements
		ValidationListener {

	Button btnShip;
	@Required(order = 1)
	@TextRule(order = 2, minLength = 6, message = "Enter atleast 6 characters.")
	@Regex(order = 3, pattern = "[0-9]+", message = "Should contain only Numbers")
	EditText edtPhone;
	@Required(order = 4)
	EditText edtAddress;
	@Required(order = 5)
	EditText edtCity;
	EditText edtState;
	EditText edtZip;
	Spinner spCountry;
	CheckBox chkDefault;
	Validator validator;
	
	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_billing_detail);
		
		db = new DatabaseHandler(this);

		Initialize();

		validator = new Validator(this);
		validator.setValidationListener(this);

		btnShip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				validator.validateAsync();
			}
		});
		
		Address address = db.getDefaultBillAddress();
		if (address != null) {
			edtPhone.setText(address.getPhoneNo());
			edtAddress.setText(address.getAddress());
			edtCity.setText(address.getCity());
			edtState.setText(address.getState());
			edtZip.setText(address.getZip());

			String[] exercises = getResources().getStringArray(R.array.country);

			for (int a = 0; a < exercises.length; a++)
				if (address.getCountry().contentEquals(exercises[a]))
					spCountry.setSelection(a);
			
			chkDefault.setChecked(address.isBill() == 1);
		}
	}

	private void Initialize() {
		btnShip = (Button) findViewById(R.id.btnShip);
		edtPhone = (EditText) findViewById(R.id.edtPhone);
		edtAddress = (EditText) findViewById(R.id.edtAddress);
		edtCity = (EditText) findViewById(R.id.edtCity);
		edtState = (EditText) findViewById(R.id.edtState);
		edtZip = (EditText) findViewById(R.id.edtPostal);
		spCountry = (Spinner) findViewById(R.id.spCountry);
		chkDefault = (CheckBox) findViewById(R.id.chkDefault);
	}

	@Override
	public void onValidationSucceeded() {
		db.addAddress(new Address(0, edtAddress.getText().toString(), edtCity
				.getText().toString(), edtState.getText().toString(), spCountry
				.getSelectedItem().toString(), edtZip.getText().toString(),
				edtPhone.getText().toString(), 0, 1, 0, chkDefault.isChecked() ? 1 : 0));
		db.close();
		new ShipmentHandler(this);
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
