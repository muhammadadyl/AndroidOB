package pk.onlinebazaar.helpers;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.anim;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class PostGetAsyncData extends AsyncTask<String, String, JSONObject> {

	public interface IPostGetAsync {
		void onPreExecute();

		void onPostExecute(JSONObject message);
	}

	private IPostGetAsync postGetAsync;

	private ProgressDialog pDialog;

	private List<NameValuePair> params;

	private String url;

	private String method;

	private static boolean _connectionStatus;

	private Context context;

	private static final String TAG_MESSAGE = "Message";

	// JSON parser class
	private JSONParser jsonParser;

	public PostGetAsyncData(IPostGetAsync postGetAsync, Context context,
			String url, String method, List<NameValuePair> params) {
		_connectionStatus = GetConnectivityStatus(context);
		if (_connectionStatus) {
			jsonParser = new JSONParser(context);
			this.postGetAsync = postGetAsync;
			this.url = url;
			this.params = params;
			this.method = method;
			this.context = context;
			this.execute();
		}
	}

	public boolean isConnectionStatus() {
		return _connectionStatus;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Loading...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
		postGetAsync.onPreExecute();
	}

	@Override
	protected JSONObject doInBackground(String... args) {
		Log.d("request!", "starting");
		// getting product details by making HTTP request
		JSONObject jsonObj = new JSONObject();
		int length = params.size();
		for (int a = 0; a < length; a++)
			try {
				jsonObj.put(params.get(a).getName(), params.get(a).getValue());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		JSONObject json = new JSONObject();
		if (method.contentEquals("POST"))
			json = jsonParser.makeHttpRequestPost(url, jsonObj);
		else
			json = jsonParser.makeHttpRequestGet(url, params);
		// json success tag
		return json;
	}

	/**
	 * After completing background task Dismiss the progress dialog
	 * **/
	@Override
	protected void onPostExecute(JSONObject message) {
		// dismiss the dialog once product deleted
		pDialog.dismiss();
		if (message != null) {
			postGetAsync.onPostExecute(message);
		} else {
			AlertDialog.Builder AlertBuilder = new AlertDialog.Builder(context);
			AlertBuilder.setTitle("Service not found");
			AlertBuilder.setMessage("Please check you intenet or try some time later.");
			AlertDialog alt = AlertBuilder.create();
			alt.show();
		}
	}

	private boolean GetConnectivityStatus(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}

}
