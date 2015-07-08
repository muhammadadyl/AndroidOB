package pk.onlinebazaar.helpers;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pk.onlinebazaar.model.Category;
import pk.onlinebazaar.model.SubCategory;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

public class LoadingTask extends AsyncTask<String, Integer, List<Category>> {

	public interface LoadingTaskFinishedListener {
		void onTaskFinished(List<Category> result); // If you want to pass
													// something
		// back to the
		// listener add a param to this method
	}

	// This is the progress bar you want to update while the task is in progress
	private final ProgressBar progressBar;
	// This is the listener that will be told when this task is finished
	private final LoadingTaskFinishedListener finishedListener;

	// private List<String> groupList;
	private JSONArray categoryArray;

	private static List<Category> categories = new ArrayList<Category>();

	JSONParser jsonParser;

	private static final String Categories_URL = "http://www.onlinebazaar.pk/WebService/MobileAppService.svc/categories";
	private static final String TYPE_CAT = "Categories";

	// JSON element ids from response of php script:
	private static final String TAG_SUCCESS = "Success";
	private static final String TAG_ITEMS = "Items";
	private static final String TAG_SUBCATEGORY = "SubCategories";

	public LoadingTask(ProgressBar progressBar,
			LoadingTaskFinishedListener finishedListener) {
		this.progressBar = progressBar;
		this.finishedListener = finishedListener;
		jsonParser = new JSONParser(progressBar.getContext());
	}

	@Override
	protected List<Category> doInBackground(String... params) {
		if (resourcesDontAlreadyExist()) {
			return downloadResources();
		}
		return categories;
	}

	private boolean resourcesDontAlreadyExist() {
		if (categories.size() > 0)
			return false;
		return true;
	}

	private List<Category> downloadResources() {
		createCategoryList();
		return InsertCategories();
	}

	private List<Category> InsertCategories() {
		if (categoryArray != null) {
			int count = categoryArray.length();
			publishProgress(20);
			for (int i = 0; i < count; i++) {
				Category cat = new Category();
				try {
					JSONObject json = ((JSONObject) categoryArray.get(i));
					cat.setID(json.getInt("Id"));
					cat.setName(json.getString("Name"));
					cat.setDatetime(json.getString("DateTimeStamp"));
					List<SubCategory> subcategories = new ArrayList<SubCategory>();
					JSONArray subCategoryArray = json
							.getJSONArray(TAG_SUBCATEGORY);
					int len = subCategoryArray.length();
					int progress = (int) ((i / (float) count) * 80) + 20;
					publishProgress(progress);
					for (int j = 0; j < len; j++) {
						JSONObject jsonSub = ((JSONObject) subCategoryArray
								.get(j));
						SubCategory sub = new SubCategory();
						sub.setID(jsonSub.getInt("Id"));
						sub.setName(jsonSub.getString("Name"));
						sub.setCatID(json.getInt("Id"));
						sub.setDatetime(jsonSub.getString("DateTimeStamp"));
						subcategories.add(sub);
					}
					cat.setSubcategories(subcategories);
					categories.add(cat);
				} catch (JSONException e) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							progressBar.getContext());
					builder.setTitle("Service Error");
					builder.setMessage("Service not available, Please try again some time later.");
					builder.setPositiveButton("OK", null);
					AlertDialog dialog = builder.show();
				}
			}
		}
		return categories;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		progressBar.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(List<Category> result) {
		finishedListener.onTaskFinished(result);
		progressBar.setProgress(100);
	}

	private void createCategoryList() {

		int success;

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		Log.d("request!", "starting");

		JSONObject json = jsonParser.makeHttpRequest(Categories_URL, "GET",
				params, TYPE_CAT);

		if (json != null) {
			Log.d("categories attempt", json.toString());
			try {
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					categoryArray = json.getJSONArray(TAG_ITEMS);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}
