package pk.onlinebazaar.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import pk.onlinebazaar.model.Product;

public class LoadFeedData extends AsyncTask<String, Integer, List<Product>> {

	public interface LoadFeedDataFinishedListener {
		void onRefreshComplete();

		void onLoadMoreComplete();

		void onLoadMoreCompleteIfListEnd(List<Product> products);

	}

	private static boolean _connectionStatus;

	private final LoadFeedDataFinishedListener finishedListener;

	private static final String Categories_URL = "http://www.onlinebazaar.pk/WebService/MobileAppService.svc/subcategories/products/";
	private static final String TYPE_PRODUCT = "products";
	private static final String TAG_SUCCESS = "Success";
	private static final String TAG_ITEMS = "Items";
	private static int _page = 0;
	private static long _subcategoryId = 0;

	private final ImageListAdapter mAdapter;
	private final boolean mRefresh;
	private final Context mContext;

	private DatabaseHandler db;

	public LoadFeedData(ImageListAdapter adapter, boolean refresh,
			LoadFeedDataFinishedListener finishedListener, Context context,
			long subCategoryId) {
		this.mAdapter = adapter;
		this.mRefresh = refresh;
		this.finishedListener = finishedListener;
		this.mContext = context;
		db = new DatabaseHandler(context);
		_connectionStatus = GetConnectivityStatus(context);
		if (_connectionStatus) {
			if (refresh || (_subcategoryId != subCategoryId)) {
				setPage(1);
				setSubcategoryId(subCategoryId);
			} else
				setPage(getPage() + 1);
		}
	}

	@Override
	protected List<Product> doInBackground(String... url) {
		if (_connectionStatus) {
			JSONParser jsonParser = new JSONParser(mContext);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", Long
					.toString(_subcategoryId)));
			params.add(new BasicNameValuePair("page", Integer
					.toString(getPage())));
			JSONObject json = jsonParser.makeHttpRequest(Categories_URL, "GET",
					params, TYPE_PRODUCT);
			if (json != null) {
				try {
					if (json.getInt(TAG_SUCCESS) == 1) {
						JSONArray jsonArray = (JSONArray) json.get(TAG_ITEMS);
						List<Product> products = new ArrayList<Product>();

						if (jsonArray.length() > 0) {
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject jsonItem = jsonArray
										.getJSONObject(i);
								Product product = new Product();
								product.setID(jsonItem.getInt("Id"));
								product.setName(jsonItem.getString("Name"));
								product.setDescription(jsonItem
										.getString("LongDiscription"));
								product.setPrice(jsonItem.getDouble("Price"));
								product.setStock(jsonItem.getInt("Stock"));
								product.setURL(jsonItem.getString("ImageUrl"));
								product.setLargeUrl(jsonItem
										.getString("ImageLargeUrl"));
								product.setCat(jsonItem.getInt("CategoryId"));
								product.setSub(jsonItem.getInt("SubCategoryId"));
								product.setDatetime(jsonItem
										.getString("DateTimeStamp"));
								products.add(product);
								db.addProduct(product);
							}
							db.close(); // Closing database connection
						}
						return products;
					}
				} catch (JSONException e) {

				}
			} else {
				return db.getAllProduct(_subcategoryId);
			}
		} else {
			return db.getAllProduct(_subcategoryId);
		}
		return new ArrayList<Product>();
	}

	@Override
	protected void onPostExecute(List<Product> entries) {
		if (entries.size() > 0) {
			mAdapter.upDateEntries(entries, mRefresh);
			new UpdateDb().execute();
		} else
			finishedListener.onLoadMoreCompleteIfListEnd(entries);

		if (mRefresh)
			finishedListener.onRefreshComplete();
		else
			finishedListener.onLoadMoreComplete();

	}

	class UpdateDb extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... params) {
			new StoreImagesToStorage(mContext).cleanOldFiles(mAdapter.GetAllProduct());
			return null;
		}
	}

	@Override
	protected void onCancelled() {
		if (mRefresh)
			finishedListener.onRefreshComplete();
		else
			finishedListener.onLoadMoreComplete();
	}

	public static int getPage() {
		return _page;
	}

	public static void setPage(int page) {
		LoadFeedData._page = page;
	}

	public static long getSubcategoryId() {
		return _subcategoryId;
	}

	public static void setSubcategoryId(long subCategoryId) {
		LoadFeedData._subcategoryId = subCategoryId;
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
