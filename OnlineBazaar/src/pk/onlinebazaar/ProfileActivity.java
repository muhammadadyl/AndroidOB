package pk.onlinebazaar;

import org.json.JSONException;
import org.json.JSONObject;

import pk.onlinebazaar.helpers.Authentication;
import pk.onlinebazaar.helpers.Authentication.IAuthenticationProfile;
import pk.onlinebazaar.helpers.DatabaseHandler;
import pk.onlinebazaar.helpers.JSONParser;
import pk.onlinebazaar.helpers.Rule;
import pk.onlinebazaar.helpers.Validator;
import pk.onlinebazaar.helpers.Validator.ValidationListener;
import pk.onlinebazaar.helpers.annotation.Regex;
import pk.onlinebazaar.helpers.annotation.Required;
import pk.onlinebazaar.helpers.annotation.TextRule;
import pk.onlinebazaar.model.Profile;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProfileActivity extends Activity implements ValidationListener,
		IAuthenticationProfile {

	@Required(order = 1)
	@TextRule(order = 2, maxLength = 25, message = "Enter atleast 25 characters.")
	private EditText fname;
	@Required(order = 3)
	@TextRule(order = 4, maxLength = 20, message = "Enter atleast 25 characters.")
	private EditText lname;
	@Required(order = 5)
	@TextRule(order = 6, minLength = 6, message = "Enter atleast 6 characters.")
	@Regex(order = 7, pattern = "[0-9]+", message = "Should contain only Numbers")
	private EditText phone;
	private Button btnSave;

	private Authentication auth;

	private Validator validator;

	private static final String TAG_MESSAGE = "Message";
	private static final String TAG_ITEM = "Item";

	private DatabaseHandler db;

	Profile pro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		db = new DatabaseHandler(this);

		fname = (EditText) findViewById(R.id.firstName);
		lname = (EditText) findViewById(R.id.lastName);
		phone = (EditText) findViewById(R.id.phoneNumber);
		btnSave = (Button) findViewById(R.id.btnEditDetais);

		pro = db.getLogedInProfile();

		if (pro != null) {
			fname.setText(pro.getFirstName());
			lname.setText(pro.getLastName());
			phone.setText(pro.getPhone());
		}

		validator = new Validator(this);
		validator.setValidationListener(this);

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				validator.validateAsync();
			}
		});

	}

	@Override
	public void onValidationSucceeded() {
		auth = new Authentication(this);
		auth.updateProfile(pro.getToken(), fname.getText().toString(), lname
				.getText().toString(), phone.getText().toString(), this);
		/*
		 * pro.setFirstName(fname.getText().toString());
		 * pro.setLastName(lname.getText().toString());
		 * pro.setPhone(phone.getText().toString()); db.updateProfile(pro);
		 */
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
	public void onProfileSucces(JSONObject message) {
		JSONObject obj;
		try {
			obj = message.getJSONObject(TAG_ITEM);
			int rowsEffected = db.updateProfile(new Profile(obj
					.getString("EmailAdress"), obj.getString("FirstName"), obj
					.getString("LastName"), obj.getString("Phone"), obj
					.getString("Mobile"), obj.getString("Address"), obj
					.getString("City"), obj.getString("State"), obj
					.getString("Zip"), obj.getString("Country"), obj
					.getString("Token")));
			db.close();

			if (rowsEffected > 0) {
				fname.setText(obj.getString("FirstName"));
				lname.setText(obj.getString("LastName"));
				phone.setText(obj.getString("Phone"));
				alertBox("Profile Updated",
						"Changes have been made to profile",
						android.R.drawable.ic_dialog_info);
			} else {
				alertBox("Fail to update", "Changes did not effected",
						android.R.drawable.ic_dialog_alert);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onProfileFailure(JSONObject message) {
		alertBox("Failed", "Unable to change profile details",
				android.R.drawable.ic_dialog_alert);
	}

	public void alertBox(String Title, String Message, final int icon) {
		AlertDialog.Builder alertBuild = new AlertDialog.Builder(this);
		alertBuild.setTitle(Title);
		alertBuild.setMessage(Message);
		alertBuild.setIcon(icon);
		alertBuild.setNeutralButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (icon == android.R.drawable.ic_dialog_info)
							finish();
						else
							dialog.dismiss();
					}
				});
		AlertDialog alt = alertBuild.create();
		alt.show();
	}

}
