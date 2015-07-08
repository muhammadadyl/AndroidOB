package pk.onlinebazaar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import pk.onlinebazaar.helpers.Authentication;
import pk.onlinebazaar.helpers.Authentication.IAuthenticationLogOut;
import pk.onlinebazaar.helpers.CustomDialogClass.IRedirection;
import pk.onlinebazaar.helpers.CustomDialogClass;
import pk.onlinebazaar.helpers.DatabaseHandler;
import pk.onlinebazaar.model.Profile;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SettingsActivity extends Activity implements IAuthenticationLogOut, IRedirection {

	private DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		db = new DatabaseHandler(this);

		final ListView listview = (ListView) findViewById(R.id.listSettings);
		String[] values;

		if (db.getLogedInProfile() != null) {
			values = getResources().getStringArray(
					R.array.settings_Authorize_arrays);
		} else {
			values = getResources().getStringArray(
					R.array.settings_UnAuthorize_arrays);
		}

		final ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; ++i) {
			list.add(values[i]);
		}
		final StableArrayAdapter adapter = new StableArrayAdapter(this,
				android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);
		

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				final String item = (String) parent.getItemAtPosition(position);
				final Profile logedInProfile = db.getLogedInProfile();
				if (logedInProfile != null) {
					switch (position) {
					case 0:
						DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
						    @Override
						    public void onClick(DialogInterface dialog, int which) {
						        switch (which){
						        case DialogInterface.BUTTON_POSITIVE:
						        	new Authentication(SettingsActivity.this).logout(logedInProfile.getToken().trim(), SettingsActivity.this);
						        	db.deleteProfile(logedInProfile);
						            break;
						        case DialogInterface.BUTTON_NEGATIVE:
						        	dialog.dismiss();
						            break;
						        }
						    }
						};
						AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
						builder.setMessage("Are Sure You Want to Sign Out")
						.setPositiveButton("Yes", dialogClickListener)
						.setNegativeButton("No", dialogClickListener)
						.show();
						break;
					case 1:
						Intent profileIntent = new Intent(SettingsActivity.this, ProfileActivity.class);
						startActivity(profileIntent);
						break;
					case 2:
						Intent orderHistoryIntent = new Intent(SettingsActivity.this, OrderHistoryActivity.class);
						startActivity(orderHistoryIntent);
						break;
					case 3:
						Intent aboutIntent = new Intent(SettingsActivity.this, AboutActivity.class);
						startActivity(aboutIntent);
						break;
					}
				} else {
					switch (position) {
					case 0:
						new CustomDialogClass(SettingsActivity.this).show();
						break;
					case 1:
						Intent aboutIntent = new Intent(SettingsActivity.this, AboutActivity.class);
						startActivity(aboutIntent);
						break;
					}
				}
			}
		});
	}
	
	private class StableArrayAdapter extends ArrayAdapter<String> {

		HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

		public StableArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
			for (int i = 0; i < objects.size(); ++i) {
				mIdMap.put(objects.get(i), i);
			}
		}

		@Override
		public long getItemId(int position) {
			String item = getItem(position);
			return mIdMap.get(item);
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

	}


	@Override
	public void onLogoutSucces(JSONObject message) {
		redirectTo();
	}

	@Override
	public void onLogoutFailure(JSONObject message) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("Failed");
		alertDialogBuilder.setMessage("Unable to sign you out please contact us");
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	@Override
	public void redirectTo() {
		Intent intent = new Intent(this, SettingsActivity.class);
		intent.putExtra("SettingsActivity", true);
		startActivity(intent);
		finish();
	}

	@Override
	public void redirectToRegister() {
		Intent registerIntent = new Intent(this, Register.class);
		registerIntent.putExtra("SettingsActivity", true);
		startActivity(registerIntent);
		finish();
	}

}
