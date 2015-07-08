package pk.onlinebazaar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pk.onlinebazaar.control.NumberPicker;
import pk.onlinebazaar.helpers.ImageDownloader;
import pk.onlinebazaar.helpers.PostGetAsyncData.IPostGetAsync;
import pk.onlinebazaar.helpers.PostGetAsyncData;
import pk.onlinebazaar.helpers.ShoppingCartHelper;
import pk.onlinebazaar.helpers.StoreImagesToStorage;
import pk.onlinebazaar.helpers.ImageDownloader.ImageExtender;
import pk.onlinebazaar.model.Product;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProductDetailActivity extends Activity implements ImageExtender,
		IPostGetAsync {

	private ImageView selectedImageView;
	private TextView productTitle;
	private TextView productDescription;
	private TextView productPrice;
	private EditText productDiscountCoupon;
	private NumberPicker productQuantity;
	private TextView tvStitchOption;
	private Spinner spinnerCollection;
	private Button addToCart;
	private Product product;

	private final static String URLGET = "http://www.onlinebazaar.pk/WebService/MobileAppService.svc/products/";

	private final static String URLDISCOUNT = "http://www.onlinebazaar.pk/WebService/MobileAppService.svc/discount/";

	private final static String ITEM = "Item";

	private final static String OPTION = "StitchingOption";

	private final static String TYPE = "Type";

	private final static String PRICE = "Price";

	private StoreImagesToStorage storeImage;

	private JSONArray jsonArray;

	LinearLayout myGallery;

	private ImageDownloader mImageDownloader;

	private final int MENU_ADD = 0;

	private final int MENU_CART = 1;

	private final int MENU_SETTINGS = 2;

	private ShoppingCartHelper cartHelper;

	private double amount;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_detail);

		Intent productIntent = getIntent();

		product = (Product) productIntent.getSerializableExtra("product");

		storeImage = new StoreImagesToStorage(this);

		mImageDownloader = new ImageDownloader(this);

		cartHelper = new ShoppingCartHelper(this);

		setupUI();

		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("id", String.valueOf(product.getID())));

		PostGetAsyncData request = new PostGetAsyncData(this, this, URLGET,
				"GET", pairs);

		if (!request.isConnectionStatus()) {
			productTitle.setText(product.getName());
			productDescription.setText(product.getDescription()
					.replaceAll("( )+", " ").replace(", ", ",\n"));
			String Url = product.getLargeUrl();
			String FileName = "large-"
					+ Url.substring(Url.lastIndexOf("/") + 1);
			if (storeImage.isFileExist(FileName)) {
				selectedImageView.setImageBitmap(storeImage
						.getThumbnail(FileName));
			} else {
				mImageDownloader.download(Url, selectedImageView);
			}

			amount = product.getPrice();
			DecimalFormat formatter = new DecimalFormat("#,###.##");
			productPrice.setText(formatter.format(amount));
		}

		spinnerCollection
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						try {
							amount = product.getPrice()
									+ jsonArray.getJSONObject(pos).getDouble(
											PRICE);
							DecimalFormat formatter = new DecimalFormat(
									"#,###.##");
							productPrice.setText("USD "
									+ formatter.format(amount));
						} catch (JSONException e) {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									ProductDetailActivity.this);
							builder.setTitle("Service Error");
							builder.setMessage("Service not available, Please try again some time later.");
							builder.setPositiveButton("OK", null);
							builder.show();
						}
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {

					}
				});

		addToCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addToCart();
			}
		});

	}

	private void addToCart() {
		List<NameValuePair> pairs = new ArrayList<NameValuePair>();
		pairs.add(new BasicNameValuePair("id", String.valueOf(product.getID())));
		try {
			pairs.add(new BasicNameValuePair("code", URLEncoder.encode(
					productDiscountCoupon.getText().toString().trim(), "utf-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		PostGetAsyncData request = new PostGetAsyncData(this, this,
				URLDISCOUNT, "GET", pairs);
		if (!request.isConnectionStatus())
			addDetailsToCartWithoutDiscount(0);
	}

	private boolean addDetailsToCartWithoutDiscount(double discountPrice) {
		// Check to see that a valid quantity was entered
		int quantity = 0;
		try {
			if (product.getStock() >= productQuantity.getValue())
				quantity = productQuantity.getValue();
			else{
				productQuantity.valueText.setError("You can only purchase maximum " + product.getStock()  + " items.");
				productQuantity.valueText.setFocusable(true);
				productQuantity.valueText.setFocusableInTouchMode(true);
				productQuantity.valueText.requestFocus();
				Toast.makeText(this, "You can only purchase maximum " + product.getStock()  + " items.", Toast.LENGTH_LONG).show();
				productQuantity.setValue(product.getStock());
				productQuantity.valueText.setFocusable(false);
				productQuantity.valueText.setFocusableInTouchMode(false);
				return false;
			}

			if (quantity < 0) {
				Toast.makeText(getBaseContext(),
						"Please enter a quantity of 0 or higher",
						Toast.LENGTH_SHORT).show();
				return false;
			}

		} catch (Exception e) {
			Toast.makeText(getBaseContext(), "Please enter a numeric quantity",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		String spinnerSelect = spinnerCollection.getSelectedItem() == null ? ""
				: (spinnerCollection.getSelectedItem().toString()
						.contains("Size") ? "For " + productTitle.getText()
						+ " selected option is "
						+ spinnerCollection.getSelectedItem().toString()
						: spinnerCollection.getSelectedItem().toString() + "~"
								+ (amount - product.getPrice()));

		if (product != null) {
			// If we make it here, a valid quantity was entered
			cartHelper.setItemAttributes(product, quantity,
					productDiscountCoupon.getText().toString(), spinnerSelect,
					amount - product.getPrice(), discountPrice);
		}

		if (!spinnerSelect.equalsIgnoreCase("un-stitched~0.0")
				&& !spinnerSelect.contains("Size")
				&& !spinnerSelect.contentEquals("")) {
			Intent intentStitched = new Intent(ProductDetailActivity.this,
					StitchedDetailActivity.class);
			intentStitched
					.putExtra("StitchAmount", amount - product.getPrice());
			intentStitched.putExtra("Size", spinnerCollection.getSelectedItem()
					.toString());
			intentStitched.putExtra("Id", product.getID());
			startActivity(intentStitched);
		} else {
			Intent intent = new Intent(ProductDetailActivity.this,
					ShoppingCartActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			// Close the activity
			finish();
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ADD, 0, "Add To Cart");
		menu.add(0, MENU_CART, 0, "Goto Cart");
		menu.add(0, MENU_SETTINGS, 0, "Settings");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// when you click setting menu
		switch (item.getItemId()) {
		case MENU_SETTINGS:
			/*
			 * Intent intent = new Intent(this, MainActivity.class);
			 * startActivity(intent);
			 */
			return true;
		case MENU_ADD:
			addToCart();
		case MENU_CART:
			Intent intentCart = new Intent(this, ShoppingCartActivity.class);
			startActivity(intentCart);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void setupUI() {
		selectedImageView = (ImageView) findViewById(R.id.selected_imageview);
		productTitle = (TextView) findViewById(R.id.productTitle);
		productDescription = (TextView) findViewById(R.id.productDescription);
		productQuantity = (NumberPicker) findViewById(R.id.productQuantity);
		productPrice = (TextView) findViewById(R.id.productPrice);
		addToCart = (Button) findViewById(R.id.addToCart);
		productDiscountCoupon = (EditText) findViewById(R.id.productDiscountCoupon);
		spinnerCollection = (Spinner) findViewById(R.id.spinnerCollection);
		tvStitchOption = (TextView) findViewById(R.id.tvStitching);
		tvStitchOption.setVisibility(View.VISIBLE);
	}

	@Override
	public void saveImage(String url, Bitmap image) {
		new SaveImagetask().execute(url);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	class SaveImagetask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... url) {
			Bitmap image = mImageDownloader.getBitmapFromCache(url[0]);
			if (image != null) {
				if (url[0].contains("medium")) {
					String filename = "medium-"
							+ url[0].substring(url[0].lastIndexOf('/') + 1);
					if (!storeImage.isFileExist(filename))
						return storeImage.SaveImage(
								"medium-"
										+ url[0].substring(url[0]
												.lastIndexOf('/') + 1), image);
					else
						return true;
				} else if (url[0].contains("large")) {
					String filename = "large-"
							+ url[0].substring(url[0].lastIndexOf('/') + 1);
					if (!storeImage.isFileExist(filename))
						return storeImage.SaveImage(
								"large-"
										+ url[0].substring(url[0]
												.lastIndexOf('/') + 1), image);
					else
						return true;
				}

			}
			return false;
		}

	}

	@Override
	public void onPreExecute() {

	}

	@Override
	public void onPostExecute(JSONObject message) {
		try {
			if (!message.getJSONObject(ITEM).has("ProductId")) {
				productTitle.setText(product.getName());
				productDescription.setText(product.getDescription()
						.replaceAll("( )+", " ").replace(", ", ",\n"));
				String Url = product.getLargeUrl();
				String FileName = "large-"
						+ Url.substring(Url.lastIndexOf("/") + 1);
				if (storeImage.isFileExist(FileName)) {
					selectedImageView.setImageBitmap(storeImage
							.getThumbnail(FileName));
				} else {
					mImageDownloader.download(Url, selectedImageView);
				}

				amount = product.getPrice();
				DecimalFormat formatter = new DecimalFormat("#,###.##");
				productPrice.setText(formatter.format(amount));

				jsonArray = message.getJSONObject(ITEM).getJSONArray(OPTION);
				if (jsonArray.length() > 0) {
					List<String> options = new ArrayList<String>();
					for (int a = 0; a < jsonArray.length(); a++) {
						JSONObject obj = jsonArray.getJSONObject(a);
						options.add(obj.getString(TYPE));
					}
					ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
							this, android.R.layout.simple_spinner_item, options);
					dataAdapter
							.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinnerCollection.setAdapter(dataAdapter);
					spinnerCollection.setVisibility(View.VISIBLE);
				} else {
					spinnerCollection.setVisibility(View.GONE);
					tvStitchOption.setVisibility(View.GONE);
				}
			} else {
				addDetailsToCartWithoutDiscount(message.getJSONObject(ITEM)
						.getDouble(PRICE));
			}
		} catch (JSONException e) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Service Error");
			builder.setMessage("Service not available, Please try again some time later.");
			builder.setPositiveButton("OK", null);
			AlertDialog dialog = builder.show();
		}
	}
}
