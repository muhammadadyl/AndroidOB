package pk.onlinebazaar;

import pk.onlinebazaar.helpers.DatabaseHandler;
import pk.onlinebazaar.helpers.Rule;
import pk.onlinebazaar.helpers.ShipmentHandler;
import pk.onlinebazaar.helpers.Validator.ValidationListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import pk.onlinebazaar.helpers.Validator;
import pk.onlinebazaar.helpers.annotation.*;
import pk.onlinebazaar.model.Address;
import pk.onlinebazaar.model.Profile;

public class ShipmentDetailActivity extends Activity implements
		ValidationListener {

	Button btnShip;

	@Required(order = 1)
	@Email(order = 2)
	EditText edtEmail;
	@Required(order = 3)
	EditText edtFirstName;
	@Required(order = 4)
	EditText edtLastName;
	@Required(order = 5)
	@TextRule(order = 6, minLength = 6, message = "Enter atleast 6 characters.")
	@Regex(order = 7, pattern = "[0-9]+", message = "Should contain only Numbers")
	EditText edtPhone;
	@Required(order = 8)
	EditText edtAddress;
	@Required(order = 9)
	EditText edtCity;
	EditText edtState;
	EditText edtZip;
	Spinner spCountry;
	CheckBox chkBilling;
	CheckBox chkDefault;
	Validator validator;

	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shipment_detail);

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

		Profile profile = db.getLogedInProfile();
		edtEmail.setText(profile.getEmail());
		edtFirstName.setText(profile.getFirstName());
		edtLastName.setText(profile.getLastName());

		Address address = db.getDefaultShipAddress();
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
			chkDefault.setChecked(address.isShip() == 1);
			chkBilling.setChecked(address.isCurrentBill() != 1);
		}

	}

	private void Initialize() {
		btnShip = (Button) findViewById(R.id.btnShip);
		edtEmail = (EditText) findViewById(R.id.edtEmail);
		edtFirstName = (EditText) findViewById(R.id.edtFirst);
		edtLastName = (EditText) findViewById(R.id.edtLast);
		edtPhone = (EditText) findViewById(R.id.edtPhone);
		edtAddress = (EditText) findViewById(R.id.edtAddress);
		edtCity = (EditText) findViewById(R.id.edtCity);
		edtState = (EditText) findViewById(R.id.edtState);
		edtZip = (EditText) findViewById(R.id.edtPostal);
		chkBilling = (CheckBox) findViewById(R.id.chkBilling);
		chkDefault = (CheckBox) findViewById(R.id.chkDefaultShipment);
		spCountry = (Spinner) findViewById(R.id.spCountry);
	}

	@Override
	public void onValidationSucceeded() {
		db.addAddress(new Address(0, edtAddress.getText().toString(), edtCity
				.getText().toString(), edtState.getText().toString(), spCountry
				.getSelectedItem().toString(), edtZip.getText().toString(),
				edtPhone.getText().toString(), 1, chkBilling.isChecked() ? 0
						: 1, chkDefault.isChecked() ? 1 : 0, 0));
		db.close();

		if (!chkBilling.isChecked()) {
			new ShipmentHandler(this);
		} else {
			Intent intent = new Intent(ShipmentDetailActivity.this,
					BillingDetailActivity.class);
			startActivity(intent);
			finish();
		}
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
