package pk.onlinebazaar;

import java.text.DecimalFormat;
import java.util.List;

import pk.onlinebazaar.helpers.CustomDialogClass;
import pk.onlinebazaar.helpers.ImageListAdapter;
import pk.onlinebazaar.helpers.OrderInitializer;
import pk.onlinebazaar.helpers.ShoppingCartHelper;
import pk.onlinebazaar.helpers.CustomDialogClass.IRedirection;
import pk.onlinebazaar.model.Product;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ShoppingCartActivity extends Activity implements IRedirection {

	private List<Product> mCartList;

	private ImageListAdapter mProductAdapter;

	private final int MENU_EDIT = 0;

	private final int MENU_CHECKOUT = 1;

	private final int MENU_SETTINGS = 2;

	private ShoppingCartHelper cartHelper;

	private ImageButton btnCheckOut;

	private Button btnShipment;

	private TextView tvSubTotal;

	private TextView tvDiscount;

	private TextView tvShipRate;

	private TextView tvTotal;

	private RelativeLayout shipmentBlock;

	private RelativeLayout checkOutBlock;

	private double shipRate;

	private boolean isPakistan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shoppingcart);

		cartHelper = new ShoppingCartHelper(this);

		DisplayShipRate();

		mCartList = cartHelper.getCartList();

		// Create the list
		final ListView listViewCatalog = (ListView) findViewById(R.id.ListViewCatalog);
		mProductAdapter = new ImageListAdapter(this);
		listViewCatalog.setAdapter(mProductAdapter);

		mProductAdapter.updateCartEntries(mCartList);

		listViewCatalog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Product product = (Product) mProductAdapter.getItem(position);
				Intent cartItemDetailsIntent = new Intent(getBaseContext(),
						CartItemDetailActivity.class);
				cartItemDetailsIntent.putExtra("cartItem", product);
				startActivity(cartItemDetailsIntent);
				finish();
			}
		});

		mProductAdapter
				.setOnFinishDeleteEventObj(new pk.onlinebazaar.helpers.OnFinishDeleteEvent() {

					@Override
					public void onFinishDelete() {
						ShoppingCartActivity.this.finish();
						Intent intentCart = new Intent(
								ShoppingCartActivity.this,
								ShoppingCartActivity.class);
						startActivity(intentCart);
						finish();
					}
				});

		btnShipment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (shipRate < 0) {
					CustomDialogClass cdd = new CustomDialogClass(
							ShoppingCartActivity.this);
					cdd.show();
				}
			}
		});

		btnCheckOut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isPakistan) {
					new OrderInitializer(ShoppingCartActivity.this);
				} else {
					Intent intentCart = new Intent(ShoppingCartActivity.this,
							PaymentActivity.class);
					intentCart.putExtra("TotalAmount", Double.parseDouble(tvTotal.getText().toString().replace("Total Price: USD ", "")));
					startActivity(intentCart);
					finish();
				}
			}
		});
	}

	public void Initialize(double shipRate) {
		if (shipRate < 0) {
			tvShipRate = (TextView) findViewById(R.id.footerShipment);
			tvSubTotal = (TextView) findViewById(R.id.footerSubTotal);
			tvDiscount = (TextView) findViewById(R.id.footerDiscount);
			tvTotal = (TextView) findViewById(R.id.footerTotal);
		} else {
			tvShipRate = (TextView) findViewById(R.id.footerShipmentAfter);
			tvSubTotal = (TextView) findViewById(R.id.footerSubTotalAfter);
			tvDiscount = (TextView) findViewById(R.id.footerDiscountAfter);
			tvTotal = (TextView) findViewById(R.id.footerTotalAfter);
		}
		btnCheckOut = (ImageButton) findViewById(R.id.btnCheckout);
		btnShipment = (Button) findViewById(R.id.btnShipment);
		shipmentBlock = (RelativeLayout) findViewById(R.id.ShipmentBlock);
		checkOutBlock = (RelativeLayout) findViewById(R.id.CheckOutBlock);
	}

	public void DisplayShipRate() {
		Intent shipIntent = getIntent();
		shipRate = shipIntent.getDoubleExtra("ShipRate", -1);
		isPakistan = shipIntent.getBooleanExtra("IsPakistan", true);

		Initialize(shipRate);

		if (shipRate >= 0) {
			DecimalFormat df = new DecimalFormat("#.##");
			tvSubTotal
					.setText("Sub Price: USD " + cartHelper.getCartSubTotal());
			tvDiscount.setText("Discount Price: USD "
					+ cartHelper.getCartDiscountTotal());
			tvShipRate.setText("Shipment: USD " + shipRate);
			tvTotal.setText("Total Price: USD "
					+ df.format(Double.parseDouble(cartHelper.getCartTotal())
							+ shipRate));
			shipmentBlock.setVisibility(View.GONE);
			checkOutBlock.setVisibility(View.VISIBLE);
		} else {
			tvSubTotal
					.setText("Sub Price: USD " + cartHelper.getCartSubTotal());
			tvDiscount.setText("Discount Price: USD "
					+ cartHelper.getCartDiscountTotal());
			tvTotal.setText("Total Price: USD " + cartHelper.getCartTotal());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_EDIT, 0, "Goto Cart");
		menu.add(0, MENU_CHECKOUT, 0, "Check Out");
		menu.add(0, MENU_SETTINGS, 0, "Settings");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// when you click setting menu
		switch (item.getItemId()) {
		case MENU_EDIT:
			Intent intentCart = new Intent(this, ShoppingCartActivity.class);
			intentCart.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intentCart);
			finish();
			return true;
		case MENU_CHECKOUT:
			if (isPakistan) {
				new OrderInitializer(ShoppingCartActivity.this);
			} else {
				Intent menuCart = new Intent(ShoppingCartActivity.this,
						PaymentActivity.class);
				menuCart.putExtra("TotalAmount", Double.parseDouble(tvTotal.getText().toString().replace("Total Price: USD ", "")));
				startActivity(menuCart);
				finish();
			}
			return true;
		case MENU_SETTINGS:

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();

		DisplayShipRate();

		// Refresh the data
		if (mProductAdapter != null) {
			mProductAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}	

	@Override
	public void redirectTo() {
		gotoShipmentDetails();
	}
	
	private void gotoShipmentDetails() {
		Intent shipIntent = new Intent(this, ShipmentDetailActivity.class);
		shipIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(shipIntent);
	}

	@Override
	public void redirectToRegister() {
		Intent registerIntent = new Intent(this, Register.class);
		startActivity(registerIntent);
		finish();
	}

}
