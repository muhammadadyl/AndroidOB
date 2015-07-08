package pk.onlinebazaar;

import org.json.JSONException;
import org.json.JSONObject;

import pk.onlinebazaar.helpers.Authentication;
import pk.onlinebazaar.helpers.Authentication.IAuthenticationLogin;
import pk.onlinebazaar.helpers.Authentication.IAuthenticationRegister;
import pk.onlinebazaar.helpers.CustomDialogClass.IRedirection;
import pk.onlinebazaar.helpers.Rule;
import pk.onlinebazaar.helpers.Validator;
import pk.onlinebazaar.helpers.Validator.ValidationListener;
import pk.onlinebazaar.helpers.annotation.ConfirmPassword;
import pk.onlinebazaar.helpers.annotation.Email;
import pk.onlinebazaar.helpers.annotation.Password;
import pk.onlinebazaar.helpers.annotation.Regex;
import pk.onlinebazaar.helpers.annotation.Required;
import pk.onlinebazaar.helpers.annotation.TextRule;
import pk.onlinebazaar.helpers.CustomDialogClass;
import pk.onlinebazaar.helpers.DatabaseHandler;
import pk.onlinebazaar.helpers.JSONParser;
import pk.onlinebazaar.model.Profile;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener,
		IAuthenticationRegister, IAuthenticationLogin, ValidationListener,
		IRedirection {

	@Required(order = 1)
	@Email(order = 2)
	private EditText user;
	@Required(order = 3)
	@Password(order = 4)
	private EditText pass;
	@Required(order = 5)
	@ConfirmPassword(order = 6)
	private EditText conpass;
	@Required(order = 7)
	@TextRule(order = 8, maxLength = 25, message = "Enter atleast 25 characters.")
	private EditText fname;
	@Required(order = 9)
	@TextRule(order = 10, maxLength = 20, message = "Enter atleast 25 characters.")
	private EditText lname;
	@Required(order = 11)
	@TextRule(order = 12, minLength = 6, message = "Enter atleast 6 characters.")
	@Regex(order = 13, pattern = "[0-9]+", message = "Should contain only Numbers")
	private EditText phone;
	private Button mRegister, mLogin;

	private Authentication auth;

	private Validator validator;

	// JSON parser class
	JSONParser jsonParser;

	private static final String TAG_MESSAGE = "Message";
	private static final String TAG_ITEM = "Item";

	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		jsonParser = new JSONParser(this);
		db = new DatabaseHandler(this);

		if (db.getLogedInProfile() != null)
			redirectTo();

		user = (EditText) findViewById(R.id.username);
		pass = (EditText) findViewById(R.id.password);
		conpass = (EditText) findViewById(R.id.confirmPassword);
		fname = (EditText) findViewById(R.id.firstName);
		lname = (EditText) findViewById(R.id.lastName);
		phone = (EditText) findViewById(R.id.phoneNumber);
		mRegister = (Button) findViewById(R.id.btnRegister);
		mLogin = (Button) findViewById(R.id.btnLogin);
		mRegister.setOnClickListener(this);
		mLogin.setOnClickListener(this);

		validator = new Validator(this);
		validator.setValidationListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnRegister:
			validator.validateAsync();
			break;
		case R.id.btnLogin:
			CustomDialogClass cdd = new CustomDialogClass(Register.this);
			cdd.show();
			break;
		default:
			break;
		}
	}

	@Override
	public void onRegisterSucces(JSONObject message) {
		String username = user.getText().toString().trim();
		String password = pass.getText().toString().trim();

		auth.login(username, password, this);
	}

	@Override
	public void onRegisterFailure(JSONObject message) {
		try {
			Toast.makeText(this, message.getString(TAG_MESSAGE).toString(),
					Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onLoginSucces(JSONObject message) {
		JSONObject obj;
		try {
			obj = message.getJSONObject(TAG_ITEM);
			db.addProfile(new Profile(obj.getString("EmailAdress"), obj
					.getString("FirstName"), obj.getString("LastName"), obj
					.getString("Phone"), obj.getString("Mobile"), obj
					.getString("Address"), obj.getString("City"), obj
					.getString("State"), obj.getString("Zip"), obj
					.getString("Country"), obj.getString("Token")));
			db.close();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		redirectTo();
	}

	@Override
	public void onLoginFailure(JSONObject message) {
		CustomDialogClass cdd = new CustomDialogClass(this);
		cdd.show();
	}

	private void gotoShipmentDetails() {
		Intent shipIntent = new Intent(this, ShipmentDetailActivity.class);
		startActivity(shipIntent);
		finish();
	}

	@Override
	public void onValidationSucceeded() {
		// Check for success tag
		String username = user.getText().toString().trim();
		String password = pass.getText().toString().trim();
		String firstname = fname.getText().toString().trim();
		String lastname = lname.getText().toString().trim();
		String phonenumber = phone.getText().toString().trim();

		auth = new Authentication(this);
		auth.register(username, password, firstname, lastname, phonenumber,
				this);

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

	@Override
	public void redirectTo() {
		boolean isSettings = getIntent().getBooleanExtra("SettingsActivity", false);
		if (isSettings) {
			Intent intent = new Intent(this, SettingsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			finish();
		} else {
			gotoShipmentDetails();
		}
	}

	@Override
	public void redirectToRegister() {
		Intent intent = new Intent(this, Register.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}
}
