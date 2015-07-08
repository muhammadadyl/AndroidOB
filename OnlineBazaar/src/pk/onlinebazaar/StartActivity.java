package pk.onlinebazaar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pk.onlinebazaar.helpers.ExpandableListAdapter;
import pk.onlinebazaar.helpers.JSONParser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Toast;

public class StartActivity extends Activity {

	List<String> groupList;
	List<String> childList;
	Map<String, List<String>> subCategories;
	ExpandableListView expListView;
	ExpandableListAdapter expListAdapter;

	JSONArray Items;
	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser;

	private static final String Categories_URL = "https://www.onlinebazaar.pk/WebService/MobileAppService.svc/categories";
	private static final String TYPE_CAT = "Categories";

	private static final String SubCategories_URL = "https://www.onlinebazaar.pk/WebService/MobileAppService.svc/subcategories";
	private static final String TYPE_SUB = "SubCategories";

	// JSON element ids from response of php script:
	private static final String TAG_SUCCESS = "Success";
	private static final String TAG_MESSAGE = "Message";
	private static final String TAG_ITEMS = "Items";
	private static final String TAG_CATEGORY = "Categories";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		jsonParser = new JSONParser(this);
		createGroupList();
	}

	private void createGroupList() {
		new FetchCategories().execute();
	}

	private void createCollection() {
		new FetchSubCategories().execute(groupList.toArray(new String[0]));
	}

	private void loadChild(String[] subCategories) {
		childList = new ArrayList<String>();
		for (String model : subCategories)
			childList.add(model);
	}

	private void setGroupIndicatorToRight() {
		/* Get the screen width */
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;

		expListView.setIndicatorBounds(width - getDipsFromPixel(35), width- getDipsFromPixel(5));
	}

	// Convert pixel to dip
	public int getDipsFromPixel(float pixels) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 0.5f);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	public void expandableList(Activity context, List<String> categories,	Map<String, List<String>> subCategories) {

		expListView = (ExpandableListView) findViewById(R.id.category_list);
		
		expListView.setAdapter(expListAdapter);
		
		expListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				final String selected = (String) expListAdapter.getChild(groupPosition, childPosition);
				Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG).show();
				return true;
			}
		});

	}	

	class FetchCategories extends AsyncTask<String, String, JSONObject> {

		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(StartActivity.this);
			pDialog.setMessage("Fetching Categories...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {
			// Check for success tag
			int success;
			try {
				// Building Parameters
				List<NameValuePair> params = new ArrayList<NameValuePair>();

				Log.d("request!", "starting");
				// getting product details by making HTTP request
				JSONObject json = jsonParser.makeHttpRequest(Categories_URL, "GET", params, TYPE_CAT);

				// check your log for json response
				Log.d("categories attempt", json.toString());

				// json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Fetch Successful!", json.toString());
				} else {
					Log.d("Fetch Failure!", json.getString(TAG_MESSAGE));
				}
				return json;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			// dismiss the dialog once product deleted
			int success;

			pDialog.dismiss();
			if (json != null) {
				// json.getInt(TAG_SUCCESS)
				try {
					Toast.makeText(StartActivity.this, json.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
					success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
						Items = json.getJSONArray(TAG_ITEMS);
						groupList = convertInToGroupList(Items);
						//expandableList(StartActivity.this, groupList, null);
						createCollection();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

		private List<String> convertInToGroupList(JSONArray items) {

			ArrayList<String> list = new ArrayList<String>();
			if (items != null) {
				int len = items.length();
				for (int i = 0; i < len; i++) {
					try {
						list.add(((JSONObject) items.get(i)).getString("Name"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			return list;
		}
	}

	class FetchSubCategories extends AsyncTask<String, String, JSONArray> {

		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(StartActivity.this);
			pDialog.setMessage("Fetching SubCategories...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONArray doInBackground(String... categoryName) {
			// Check for success tag
			int success;
			int count = categoryName.length;
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < count; i++) {
				try {
					// Building Parameters
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					
					Log.d("request!", "starting");

					JSONObject json = jsonParser.makeHttpRequest(SubCategories_URL + "/" + URLEncoder.encode(categoryName[i].replace("&", "AND"), "utf-8") , "GET", params, TYPE_SUB);
					json.put(TAG_CATEGORY, categoryName[i]);
					
					Log.d("Subcategories attempt", json.toString());

					success = json.getInt(TAG_SUCCESS);
					if (success == 1) {
						Log.d("Fetch Successful!", json.toString());
					} else {
						Log.d("Fetch Failure!", json.getString(TAG_MESSAGE));
					}
					jsonArray.put(json);
				} catch (JSONException e) {
					e.printStackTrace();
					break;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			return jsonArray;
		}

		@Override
		protected void onPostExecute(JSONArray jsonArray) {

			int success;
			JSONArray Items;
			int count = jsonArray.length();
			subCategories = new LinkedHashMap<String, List<String>>();
			pDialog.dismiss();
			for (int i = 0; i < count; i++) {
				try {
					JSONObject jsonObj = jsonArray.getJSONObject(i); 
					if (jsonObj != null) {
						// json.getInt(TAG_SUCCESS)
						Toast.makeText(StartActivity.this, jsonObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
						success = jsonObj.getInt(TAG_SUCCESS);
						if (success == 1) {
							Items = jsonObj.getJSONArray(TAG_ITEMS);
							subCategories.put(jsonObj.getString(TAG_CATEGORY), convertInToList(Items));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (NullPointerException e){
					e.printStackTrace();
				}
			}
			expandableList(StartActivity.this, groupList, subCategories);
		}

		private List<String> convertInToList(JSONArray items) {

			ArrayList<String> list = new ArrayList<String>();
			if (items != null) {
				int len = items.length();
				for (int i = 0; i < len; i++) {
					try {
						list.add(((JSONObject) items.get(i)).getString("Name"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			return list;
		}
	}
}
