package pk.onlinebazaar.helpers;

import org.json.JSONException;
import org.json.JSONObject;

import pk.onlinebazaar.R;
import pk.onlinebazaar.Register;
import pk.onlinebazaar.helpers.Authentication.IAuthenticationLogin;
import pk.onlinebazaar.helpers.Validator.ValidationListener;
import pk.onlinebazaar.helpers.annotation.Email;
import pk.onlinebazaar.helpers.annotation.Password;
import pk.onlinebazaar.helpers.annotation.Required;
import pk.onlinebazaar.model.Profile;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomDialogClass extends Dialog implements
		android.view.View.OnClickListener, IAuthenticationLogin,
		ValidationListener {

	public Activity c;
	@Required(order = 1)
	@Email(order = 2, message = "Please enter valid email")
	public EditText user;
	@Required(order = 3)
	@Password(order = 4)
	public EditText pass;
	private Button mSubmit, mRegister;

	private Validator validator;

	public ProgressDialog pDialog;

	JSONParser jsonParser;

	private static final String TAG_MESSAGE = "Message";
	private static final String TAG_ITEM = "Item";

	private IRedirection redirect;

	private DatabaseHandler db;

	public interface IRedirection {
		void redirectTo();
		void redirectToRegister();
	}

	public CustomDialogClass(Activity a) {
		super(a);
		this.c = a;
		db = new DatabaseHandler(a);
		jsonParser = new JSONParser(a);
		this.redirect = (IRedirection) a;
	}

	@Override
	public void show() {
		if (db.getLogedInProfile() == null)
			super.show();
		else {
			redirect.redirectTo();
			dismiss();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		loginContentViewAndInit();

		validator = new Validator(this);
		validator.setValidationListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login:
			validator.validateAsync();
			break;
		case R.id.register:
			redirect.redirectToRegister();
			dismiss();
			break;
		default:
			break;
		}
	}

	private void loginContentViewAndInit() {
		setContentView(R.layout.login);
		// setup input fields
		user = (EditText) findViewById(R.id.username);
		pass = (EditText) findViewById(R.id.password);

		// setup buttons
		mSubmit = (Button) findViewById(R.id.login);
		mRegister = (Button) findViewById(R.id.register);

		// register listeners
		mSubmit.setOnClickListener(this);
		mRegister.setOnClickListener(this);
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
		redirect.redirectTo();
		dismiss();
	}

	@Override
	public void onLoginFailure(JSONObject message) {
		try {
			Toast.makeText(c, message.getString(TAG_MESSAGE).toString(),
					Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onValidationSucceeded() {
		String username = user.getText().toString().trim();
		String password = pass.getText().toString().trim();
		new Authentication(c).login(username, password, this);
	}

	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {
		String message = failedRule.getFailureMessage();

		if (failedView instanceof EditText) {
			failedView.requestFocus();
			((EditText) failedView).setError(message);
		} else {
			Toast.makeText(c, message, Toast.LENGTH_SHORT).show();
		}
	}

}
