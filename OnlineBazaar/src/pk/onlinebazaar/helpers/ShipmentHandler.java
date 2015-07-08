package pk.onlinebazaar.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pk.onlinebazaar.ShoppingCartActivity;
import pk.onlinebazaar.helpers.PostGetAsyncData.IPostGetAsync;
import pk.onlinebazaar.model.Address;
import pk.onlinebazaar.model.ShoppingCart;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class ShipmentHandler implements IPostGetAsync {
	
	private Activity activity;
	private DatabaseHandler db;
	private Address address;
	private String SHIP_URL = "http://www.onlinebazaar.pk/WebService/MobileAppService.svc/shipment";
	
	public ShipmentHandler(Activity activity){
		this.activity = activity;
		db = new DatabaseHandler(activity);
		address = db.getCurrentShipAddress();
		if (address != null) {
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("items", GetCartItemInJson()
					.toString()));
			pairs.add(new BasicNameValuePair("city", address.getCity()));
			pairs.add(new BasicNameValuePair("country", address.getCountry()));
			pairs.add(new BasicNameValuePair("ipaddress", Utils.getIPAddress(true)));
			PostGetAsyncData request = new PostGetAsyncData(this, activity, SHIP_URL, "POST", pairs);
			if (!request.isConnectionStatus()) {
				Toast.makeText(activity, "Please Check you internet Connection", Toast.LENGTH_LONG).show();
			}
		}
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
				jsonArray.put(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonArray;
	}


	@Override
	public void onPreExecute() {
	
	}


	@Override
	public void onPostExecute(JSONObject message) {
		try {
			JSONArray jsonArray = message.getJSONObject("Item").getJSONArray("Items");
			if(jsonArray != null){
				int count = jsonArray.length();
				for(int a=0; a<count; a++){
				 ShoppingCart cart = db.getCartItem(jsonArray.getJSONObject(a).getInt("ProductId"));
				 cart.setQuantity(jsonArray.getJSONObject(a).getInt("Quantity"));
				 db.updateCart(cart);			
				}
				db.close();
			}
			
			Intent shipmentIntent = new Intent(activity, ShoppingCartActivity.class);
			shipmentIntent.putExtra("ShipRate", message.getJSONObject("Item").getDouble("Rate"));
			shipmentIntent.putExtra("IsInter", message.getJSONObject("Item").getBoolean("IsInternational"));
			shipmentIntent.putExtra("IsPakistan", address.getCountry().equalsIgnoreCase("pakistan"));
			shipmentIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			activity.startActivity(shipmentIntent);
			activity.finish();
			Log.i("ShipmentAmount", String.valueOf(message.getJSONObject("Item").getDouble("Rate")));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
