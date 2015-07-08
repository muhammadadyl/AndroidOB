package pk.onlinebazaar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import pk.onlinebazaar.control.NumberPicker;
import pk.onlinebazaar.helpers.ImageDownloader;
import pk.onlinebazaar.helpers.ImageDownloader.ImageExtender;
import pk.onlinebazaar.helpers.PostGetAsyncData;
import pk.onlinebazaar.helpers.PostGetAsyncData.IPostGetAsync;
import pk.onlinebazaar.helpers.ShoppingCartHelper;
import pk.onlinebazaar.helpers.StoreImagesToStorage;
import pk.onlinebazaar.model.Product;
import pk.onlinebazaar.model.ShoppingCart;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CartItemDetailActivity extends Activity implements ImageExtender, IPostGetAsync {

	private ImageView selectedImageView;
	private TextView productTitle;
	private TextView productDescription;
	private TextView productPrice;
	private NumberPicker productQuantity;
	private EditText productDiscountCoupon;
	private Button addToCart;
	private Product product;
	
	private final static String URLDISCOUNT = "https://www.onlinebazaar.pk/WebService/MobileAppService.svc/discount/";
	
	private final static String ITEM = "Item";
	
	private final static String PRICE = "Price";

	private StoreImagesToStorage storeImage;

	LinearLayout myGallery;

	private ImageDownloader mImageDownloader;

	private ShoppingCartHelper cartHelper;

	private ShoppingCart cart;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_detail);

		cartHelper = new ShoppingCartHelper(this);

		storeImage = new StoreImagesToStorage(this);

		mImageDownloader = new ImageDownloader(this);

		cartHelper = new ShoppingCartHelper(this);

		Intent cartIntent = getIntent();
		product = (Product) cartIntent.getSerializableExtra("cartItem");

		if (product != null) {
			cart = cartHelper.getCartItem(product);
		} else {
			finish();
		}

		setupUI();

		productTitle.setText(product.getName());
		productDescription.setText(product.getDescription()
				.replaceAll("( )+", " ").replace(", ", ",\n"));
		String Url = product.getLargeUrl();
		String FileName = "large-" + Url.substring(Url.lastIndexOf("/") + 1);
		if (storeImage.isFileExist(FileName)) {
			selectedImageView.setImageBitmap(storeImage.getThumbnail(FileName));
		} else {
			mImageDownloader.download(Url, selectedImageView);
		}

		double amount = product.getPrice() + cart.getStitchPrice() - cart.getDiscountPrice();
		DecimalFormat formatter = new DecimalFormat("#,###.##");
		productPrice.setText(formatter.format(amount));

		if (cart != null) {
			productDiscountCoupon.setText(cart.getDiscountCoupon());
			productQuantity.valueText.setText(String.valueOf(cart.getQuantity()));
			productQuantity.value = cart.getQuantity();
		}
		addToCart.setText("Update To Cart");
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
		if(!request.isConnectionStatus())
			addDetailsToCartWithoutDiscount(0);
	}
	
	private boolean addDetailsToCartWithoutDiscount(double discountPrice){
		// Check to see that a valid quantity was entered
				int quantity = 0;
				try {
					quantity = productQuantity.getValue();

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

				if (product != null)
					// If we make it here, a valid quantity was entered
					cartHelper.setItemAttributes(product, quantity,
							productDiscountCoupon.getText().toString(), discountPrice);
				
				Intent intent = new Intent(CartItemDetailActivity.this,
						ShoppingCartActivity.class);
				startActivity(intent);
				// Close the activity
				finish();
				return true;
	}

 	private void setupUI() {
		selectedImageView = (ImageView) findViewById(R.id.selected_imageview);
		productTitle = (TextView) findViewById(R.id.productTitle);
		productDescription = (TextView) findViewById(R.id.productDescription);
		productQuantity = (NumberPicker) findViewById(R.id.productQuantity);
		productPrice = (TextView) findViewById(R.id.productPrice);
		addToCart = (Button) findViewById(R.id.addToCart);
		productDiscountCoupon = (EditText) findViewById(R.id.productDiscountCoupon);
		// myGallery = (LinearLayout) findViewById(R.id.mygallery);
	}

	@Override
	public void saveImage(String url, Bitmap image) {
		new SaveImagetask().execute(url);
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
			addDetailsToCartWithoutDiscount(message.getJSONObject(ITEM).getDouble(PRICE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}

}
