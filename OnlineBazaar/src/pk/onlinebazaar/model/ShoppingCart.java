package pk.onlinebazaar.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ShoppingCart  implements Serializable {
	
	private Product mProduct;
	private int mQuantity;
	private String mDiscountCoupon;
	private double mDiscountPrice;
	private String mAttribute;
	private double mStitchPrice;
	
	public ShoppingCart(Product product, int quantity, String discountCoupon, double discountPrice) {
		mProduct = product;
		mQuantity = quantity;
		mDiscountCoupon = discountCoupon;
		mDiscountPrice = discountPrice;
	}
	
	public ShoppingCart(Product product, int quantity, String discountCoupon, String attribute) {
		mProduct = product;
		mQuantity = quantity;
		mDiscountCoupon = discountCoupon;
		mAttribute = attribute;
	}
	
	public ShoppingCart(Product product, int quantity, String discountCoupon, double discountPrice, String attribute, double stitchPrice) {
		mProduct = product;
		mQuantity = quantity;
		mDiscountCoupon = discountCoupon;
		mAttribute = attribute;
		mStitchPrice = stitchPrice;
		mDiscountPrice = discountPrice;
	}
	
	public String getDiscountCoupon() {
		return mDiscountCoupon;
	}

	public void setDiscountCoupon(String discountCoupon) {
		mDiscountCoupon = discountCoupon;
	}
	
	public Product getProduct() {
		return mProduct;
	}
	
	public int getQuantity() {
		return mQuantity;
	}
	
	public void setQuantity(int quantity) {
		mQuantity = quantity;
	}

	public String getAttribute() {
		return mAttribute;
	}

	public void setAttribute(String attribute) {
		mAttribute = attribute;
	}

	public double getStitchPrice() {
		return mStitchPrice;
	}

	public void setStitchPrice(double stitchPrice) {
		mStitchPrice = stitchPrice;
	}

	public double getDiscountPrice() {
		return mDiscountPrice;
	}

	public void setDiscountPrice(double discountPrice) {
		mDiscountPrice = discountPrice;
	}

}
