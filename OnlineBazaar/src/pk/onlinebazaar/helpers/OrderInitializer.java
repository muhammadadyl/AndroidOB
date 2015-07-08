package pk.onlinebazaar.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pk.onlinebazaar.MenuActivity;
import pk.onlinebazaar.helpers.PostGetAsyncData.IPostGetAsync;
import pk.onlinebazaar.model.Address;
import pk.onlinebazaar.model.CreditCard;
import pk.onlinebazaar.model.Profile;
import pk.onlinebazaar.model.ShoppingCart;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class OrderInitializer implements IPostGetAsync, OnClickListener {

	private Context context;

	private DatabaseHandler db;

	private TermsAndConditionDialog cd;

	private CreditCard creditCard;

	private String ORDER_URL = "https://www.onlinebazaar.pk/WebService/MobileAppService.svc/order";

	public OrderInitializer(Context context) {
		this.context = context;
		db = new DatabaseHandler(context);
		cd = new TermsAndConditionDialog((Activity) context, this);
		cd.show();
	}

	public OrderInitializer(Context context, CreditCard creditCard) {
		this(context);
		this.creditCard = creditCard;
	}

	private JSONObject GetBillShipInJson() {
		Address bill = db.getCurrentBillAddress();
		Address ship = db.getCurrentShipAddress();

		JSONObject json = new JSONObject();
		try {
			json.put("address", bill.getAddress());
			json.put("city", bill.getCity());
			json.put("state", bill.getState());
			json.put("zip", bill.getZip());
			json.put("country", bill.getCountry());
			json.put("phone", bill.getPhoneNo());
			json.put("shipaddress", ship.getAddress());
			json.put("shipcity", ship.getCity());
			json.put("shipstate", ship.getState());
			json.put("shipzip", ship.getZip());
			json.put("shipcountry", ship.getCountry());
			json.put("shipphone", ship.getPhoneNo());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}

	private JSONArray GetCartItemInJson() {
		List<ShoppingCart> cartItems = db.getAllCartItem();
		JSONArray jsonArray = new JSONArray();
		for (ShoppingCart cart : cartItems) {
			JSONObject json = new JSONObject();
			try {
				json.put("ProductId", cart.getProduct().getID());
				json.put("Quantity", cart.getQuantity());
				json.put("DiscountCode", cart.getDiscountCoupon());
				json.put("Instructions", cart.getAttribute());
				json.put("StitchingPrice", cart.getStitchPrice());
				jsonArray.put(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}

	private JSONObject GetCreditCard() {
		JSONObject json = new JSONObject();
		try {
			json.put("NameOnCard", creditCard.getNameOnCard());
			json.put("CardNumber", creditCard.getCardNumber());
			json.put("SecurityCard", creditCard.getSecurityCode());
			json.put("ExpiryDate", creditCard.getExpiryDate());
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		return json;
	}

	@Override
	public void onClick(View v) {
		cd.dismiss();
		Profile profile = db.getLogedInProfile();
		if (profile != null) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("token", db.getLogedInProfile()
					.getToken()));
			pairs.add(new BasicNameValuePair("billShip", GetBillShipInJson()
					.toString()));
			pairs.add(new BasicNameValuePair("creditCard", GetCreditCard()
					.toString()));
			pairs.add(new BasicNameValuePair("items", GetCartItemInJson()
					.toString()));
			PostGetAsyncData request = new PostGetAsyncData(this, context,
					ORDER_URL, "POST", pairs);
			if (!request.isConnectionStatus()) {
				Toast.makeText(context, "Please Check you internet Connection",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onPreExecute() {

	}

	@Override
	public void onPostExecute(JSONObject message) {
		String alerttitle = "Error";
		String alertmessage = "Error while Ordering your order. Please try some time after or if it is still appears please feel free to report at +92-322-922-9227";
		try {
			if (message.getInt("Success") == 1) {
				long OrderId = message.getJSONObject("Item").getLong("OrderId");
				if (OrderId > 0) {
					alerttitle = "Thank You for your order.";
					alertmessage = "Your order has been placed successfully with OnlineBazaar Mobile App. For Your order Id is "
							+ OrderId;
					db.deleteCart();
					alert(alerttitle, alertmessage, true);
					return;
				}
			} else {
				if (message.getString("Message").contains("Payment Failed")) {
					alerttitle = "Please check credit details";
					alertmessage = "Invalid Credit Card";
				}
				alert(alerttitle, alertmessage, false);
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void alert(String alerttitle, String alertmessage,
			final boolean checkStatus) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(alerttitle);
		builder.setMessage(alertmessage);
		builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intentCart = new Intent(context, MenuActivity.class);
				if (checkStatus) {
					intentCart.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					((Activity) context).startActivity(intentCart);
					((Activity) context).finish();
				}
			}
		});
		builder.show();
	}

}
