package pk.onlinebazaar.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import pk.onlinebazaar.SettingsActivity;
import pk.onlinebazaar.helpers.PostGetAsyncData.IPostGetAsync;

public class Authentication {

	public interface IAuthenticationLogin {
		void onLoginSucces(JSONObject message);

		void onLoginFailure(JSONObject message);
	}

	public interface IAuthenticationRegister {
		void onRegisterSucces(JSONObject message);

		void onRegisterFailure(JSONObject message);
	}

	public interface IAuthenticationLogOut {
		void onLogoutSucces(JSONObject message);

		void onLogoutFailure(JSONObject message);
	}

	public interface IAuthenticationProfile {
		void onProfileSucces(JSONObject message);

		void onProfileFailure(JSONObject message);
	}

	private static final String LOGIN_URL = "https://www.onlinebazaar.pk/WebService/MobileAppService.svc/login";
	private static final String REGISTER_URL = "https://www.onlinebazaar.pk/WebService/MobileAppService.svc/register";
	private static final String LOGOUT_URL = "https://www.onlinebazaar.pk/WebService/MobileAppService.svc/logout";
	private static final String PROFILE_URL = "https://www.onlinebazaar.pk/WebService/MobileAppService.svc/profile";
	private static final String TAG_SUCCESS = "Success";
	private IAuthenticationLogin login;
	private IAuthenticationRegister register;
	private IAuthenticationLogOut logout;
	private IAuthenticationProfile profile;

	private Context context;

	public Authentication(Context context) {
		this.context = context;
	}

	public void login(String username, String password,
			IAuthenticationLogin login) {

		this.login = login;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		try {
			params.add(new BasicNameValuePair("ipAdress", URLEncoder.encode(
					Utils.getIPAddress(true), "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		new PostGetAsyncData(new IPostGetAsync() {

			@Override
			public void onPreExecute() {

			}

			@Override
			public void onPostExecute(JSONObject message) {
				try {
					if (message.getInt(TAG_SUCCESS) == 1) {
						Authentication.this.login.onLoginSucces(message);
					} else {
						Authentication.this.login.onLoginFailure(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, context, LOGIN_URL, "POST", params);
	}

	public void register(String username, String password, String firstname,
			String lastname, String phonenumber,
			IAuthenticationRegister register) {

		this.register = register;

		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("firstName", firstname));
		params.add(new BasicNameValuePair("lastName", lastname));
		params.add(new BasicNameValuePair("phoneNo", phonenumber));

		new PostGetAsyncData(new IPostGetAsync() {

			@Override
			public void onPreExecute() {

			}

			@Override
			public void onPostExecute(JSONObject message) {
				try {
					if (message.getInt(TAG_SUCCESS) == 1) {
						Authentication.this.register.onRegisterSucces(message);
					} else {
						Authentication.this.register.onRegisterFailure(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, context, REGISTER_URL, "POST", params);
	}

	public void logout(String token, IAuthenticationLogOut logout) {
		this.logout = logout;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("token", token));

		new PostGetAsyncData(new IPostGetAsync() {

			ProgressDialog dialog;

			@Override
			public void onPreExecute() {
				dialog = ProgressDialog.show(context, "",
						"Loading. Please wait...", true);
			}

			@Override
			public void onPostExecute(JSONObject message) {
				dialog.dismiss();
				try {
					if (message.getInt(TAG_SUCCESS) == 1) {
						Authentication.this.logout.onLogoutSucces(message);
					} else {
						Authentication.this.logout.onLogoutFailure(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, context, LOGOUT_URL, "POST", params);
	}

	public void updateProfile(String token, String fname, String lname,
			String phone, IAuthenticationProfile profile) {
		this.profile = profile;
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("firstName", fname));
		params.add(new BasicNameValuePair("lastName", lname));
		params.add(new BasicNameValuePair("phoneNo", phone));
		params.add(new BasicNameValuePair("token", token));

		new PostGetAsyncData(new IPostGetAsync() {

			ProgressDialog dialog;

			@Override
			public void onPreExecute() {
				dialog = ProgressDialog.show(context, "",
						"Loading. Please wait...", true);
			}

			@Override
			public void onPostExecute(JSONObject message) {
				dialog.dismiss();
				try {
					if (message.getInt(TAG_SUCCESS) == 1) {
						Authentication.this.profile.onProfileSucces(message);
					} else {
						Authentication.this.profile.onProfileFailure(message);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}, context, PROFILE_URL, "POST", params);
	}
}
