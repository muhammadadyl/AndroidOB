package pk.onlinebazaar.helpers;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import pk.onlinebazaar.model.Product;
import pk.onlinebazaar.model.ShoppingCart;

import android.content.Context;

public class ShoppingCartHelper {

	public static final String PRODUCT_INDEX = "PRODUCT_INDEX";

	private static DatabaseHandler db;

	private List<ShoppingCart> cartList;
	
	public ShoppingCartHelper(Context context) {
		db = new DatabaseHandler(context);
		cartList = db.getAllCartItem();
	}

	public void setItemAttributes(Product product, int quantity,
			String discountCoupon, double discountPrice) {

		ShoppingCart curEntry = db.getCartItem(product.getID());

		// If the quantity is zero or less, remove the products
		if (quantity <= 0) {
			if (curEntry != null)
				removeProduct(product);
			return;
		}

		// If a current cart entry doesn't exist, create one
		if (curEntry == null) {
			curEntry = new ShoppingCart(product, quantity, discountCoupon, discountPrice);
			db.addCart(curEntry);
		} else {
			// Update the quantity
			curEntry.setQuantity(quantity);
			curEntry.setDiscountCoupon(discountCoupon);
			curEntry.setDiscountPrice(discountPrice);
			db.updateCart(curEntry);
			db.close();
		}
	}
	
	public void setItemAttributes(Product product, int quantity,
			String discountCoupon, String instruction, double stitchPrice, double discountPrice) {

		ShoppingCart curEntry = db.getCartItem(product.getID());

		// If the quantity is zero or less, remove the products
		if (quantity <= 0) {
			if (curEntry != null)
				removeProduct(product);
			return;
		}

		// If a current cart entry doesn't exist, create one
		if (curEntry == null) {
			curEntry = new ShoppingCart(product, quantity, discountCoupon, discountPrice, instruction, stitchPrice);
			db.addCart(curEntry);
		} else {
			// Update the quantity
			curEntry.setQuantity(quantity);
			curEntry.setDiscountCoupon(discountCoupon);
			curEntry.setAttribute(instruction);
			curEntry.setStitchPrice(stitchPrice);
			db.updateCart(curEntry);
			db.close();
		}
	}

	public int getProductQuantity(Product product) {
		// Get the current cart entry
		ShoppingCart curEntry = db.getCartItem(product.getID());

		if (curEntry != null)
			return curEntry.getQuantity();

		return 0;
	}

	public void removeProduct(Product product) {
		db.deleteCartItem(product.getID());
	}

	public List<Product> getCartList() {
		List<Product> productList = new Vector<Product>(db.getCartCount());
		
		for (ShoppingCart s : this.cartList) {
			int id = s.getProduct().getID();
			Product p = db.getProduct(id);
			if (p != null) //testing needed for validation
				productList.add(p);
			else
				db.deleteCartItem(id);
		}

		return productList;
	}

	public ShoppingCart getCartItem(Product product) {
		// Get the current cart entry
		if (product != null) {
			for (ShoppingCart s : cartList) {
				if (s.getProduct().getID() == product.getID())
					return s;
			}
		}
		return null;
	}
	
	public String getCartSubTotal(){
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(db.getCartSubTotal());
	}
	
	public String getCartDiscountTotal() {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(db.getCartDiscountTotal());
	}

	public String getCartTotal() {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(db.getCartTotal());
	}
}
