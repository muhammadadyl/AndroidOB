package pk.onlinebazaar.helpers;

import java.util.ArrayList;
import java.util.List;

import pk.onlinebazaar.model.Address;
import pk.onlinebazaar.model.Category;
import pk.onlinebazaar.model.Product;
import pk.onlinebazaar.model.Profile;
import pk.onlinebazaar.model.ShoppingCart;
import pk.onlinebazaar.model.SubCategory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "OnlineBazaar";

	// Categories table name
	private static final String TABLE_CATEGORIES = "Categories";

	// Categories Table Columns names
	private static final String CAT_KEY_ID = "_id";
	private static final String CAT_KEY_NAME = "name";
	private static final String CAT_KEY_DATETIME = "datetime";

	// SubCategories table name
	private static final String TABLE_SUBCATEGORIES = "Subcategories";

	// SubCategories Table Columns names
	private static final String SUB_KEY_ID = "_id";
	private static final String SUB_KEY_NAME = "name";
	private static final String SUB_KEY_CAT_ID = "catid";
	private static final String SUB_KEY_CAT_DATETIME = "datetime";

	// Products table name
	private static final String TABLE_PRODUCTS = "Products";

	// Products Table Columns names
	private static final String PRO_KEY_ID = "_id";
	private static final String PRO_KEY_NAME = "name";
	private static final String PRO_KEY_DESCRIPTION = "description";
	private static final String PRO_KEY_CAT_ID = "catid";
	private static final String PRO_KEY_SUB_CAT_ID = "subcatid";
	private static final String PRO_KEY_PRICE = "price";
	private static final String PRO_KEY_STOCK = "stock";
	private static final String PRO_KEY_IMAGE_URL = "imageurl";
	private static final String PRO_KEY_IMAGE_LARGE_URL = "imageLargeurl";
	private static final String PRO_KEY_DATETIME = "datetime";
	// Cart table name
	private static final String TABLE_CART = "Cart";

	// Cart Table Columns names
	private static final String CART_PRO_KEY_ID = "_id";
	private static final String CART_PRO_KEY_QUANTITY = "quantity";
	private static final String CART_PRO_KEY_DISCOUNT = "discount";
	private static final String CART_PRO_KEY_DISCOUNT_PRICE = "discount_price";
	private static final String CART_PRO_KEY_ATTRIBUTE = "attribute";
	private static final String CART_PRO_KEY_STITCH_PRICE = "stitch_price";

	// Profile table name
	private static final String TABLE_PROFILE = "Profile";

	// Profile Table Columns names
	private static final String PROFILE_KEY_EMAIL = "email";
	private static final String PROFILE_KEY_FIRSTNAME = "firstname";
	private static final String PROFILE_KEY_LASTNAME = "lastname";
	private static final String PROFILE_KEY_PHONE = "phone";
	private static final String PROFILE_KEY_MOBILE = "mobile";
	private static final String PROFILE_KEY_ADDRESS = "address";
	private static final String PROFILE_KEY_CITY = "city";
	private static final String PROFILE_KEY_STATE = "state";
	private static final String PROFILE_KEY_ZIP = "zip";
	private static final String PROFILE_KEY_COUNTRY = "country";
	private static final String PROFILE_KEY_TOKEN = "token";

	// Profile table name
	private static final String TABLE_ADDRESS = "Address";

	// Profile Table Columns names
	private static final String ADDRESS_KEY_ID = "_id";
	private static final String ADDRESS_KEY_PHONE = "phone";
	private static final String ADDRESS_KEY_ADDRESS = "address";
	private static final String ADDRESS_KEY_CITY = "city";
	private static final String ADDRESS_KEY_STATE = "state";
	private static final String ADDRESS_KEY_ZIP = "zip";
	private static final String ADDRESS_KEY_COUNTRY = "country";
	private static final String ADDRESS_KEY_IS_CURRENT_SHIP = "is_current_ship";
	private static final String ADDRESS_KEY_IS_CURRENT_BILL = "is_current_bill";
	private static final String ADDRESS_KEY_IS_DEFAULT_SHIP = "is_default_ship";
	private static final String ADDRESS_KEY_IS_DEFAULT_BILL = "is_default_bill";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {

		String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES
				+ "(" + CAT_KEY_ID + " INTEGER PRIMARY KEY ," + CAT_KEY_NAME
				+ " TEXT, " + CAT_KEY_DATETIME + " INTEGER)";
		db.execSQL(CREATE_CATEGORIES_TABLE);
		String CREATE_SUBCATEGORIES_TABLE = "CREATE TABLE "
				+ TABLE_SUBCATEGORIES + "(" + SUB_KEY_ID
				+ " INTEGER PRIMARY KEY ," + SUB_KEY_NAME + " TEXT,"
				+ SUB_KEY_CAT_ID + " INTEGER, " + SUB_KEY_CAT_DATETIME
				+ " INTEGER)";
		db.execSQL(CREATE_SUBCATEGORIES_TABLE);
		String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + "("
				+ CART_PRO_KEY_ID + " INTEGER PRIMARY KEY ,"
				+ CART_PRO_KEY_QUANTITY + " INTEGER, " + CART_PRO_KEY_DISCOUNT
				+ " TEXT," + CART_PRO_KEY_DISCOUNT_PRICE + " REAL, "
				+ CART_PRO_KEY_ATTRIBUTE + " TEXT, "
				+ CART_PRO_KEY_STITCH_PRICE + " REAL)";
		db.execSQL(CREATE_CART_TABLE);
		String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUCTS + "("
				+ PRO_KEY_ID + " INTEGER PRIMARY KEY ," + PRO_KEY_NAME
				+ " TEXT," + PRO_KEY_DESCRIPTION + " TEXT," + PRO_KEY_CAT_ID
				+ " INTEGER ," + PRO_KEY_SUB_CAT_ID + " INTEGER, "
				+ PRO_KEY_PRICE + " REAL ," + PRO_KEY_STOCK + " INTEGER, "
				+ PRO_KEY_IMAGE_URL + " TEXT," + PRO_KEY_IMAGE_LARGE_URL
				+ " TEXT, " + PRO_KEY_DATETIME + " INTEGER)";
		db.execSQL(CREATE_PRODUCTS_TABLE);
		String CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILE + "("
				+ PROFILE_KEY_EMAIL + " TEXT PRIMARY KEY,"
				+ PROFILE_KEY_FIRSTNAME + " TEXT," + PROFILE_KEY_LASTNAME
				+ " TEXT ," + PROFILE_KEY_PHONE + " TEXT, "
				+ PROFILE_KEY_MOBILE + " TEXT ," + PROFILE_KEY_ADDRESS
				+ " TEXT, " + PROFILE_KEY_CITY + " TEXT," + PROFILE_KEY_STATE
				+ " TEXT," + PROFILE_KEY_ZIP + " TEXT," + PROFILE_KEY_COUNTRY
				+ " TEXT," + PROFILE_KEY_TOKEN + " TEXT)";
		db.execSQL(CREATE_PROFILE_TABLE);
		String CREATE_ADDRESS_TABLE = "CREATE TABLE " + TABLE_ADDRESS + "("
				+ ADDRESS_KEY_ID + " INTEGER PRIMARY KEY ," + ADDRESS_KEY_PHONE
				+ " TEXT, " + ADDRESS_KEY_ADDRESS + " TEXT, "
				+ ADDRESS_KEY_CITY + " TEXT," + ADDRESS_KEY_STATE + " TEXT,"
				+ ADDRESS_KEY_ZIP + " TEXT," + ADDRESS_KEY_COUNTRY + " TEXT, "
				+ ADDRESS_KEY_IS_CURRENT_SHIP + " Numeric, "
				+ ADDRESS_KEY_IS_CURRENT_BILL + " Numeric, "
				+ ADDRESS_KEY_IS_DEFAULT_SHIP + " NUMERIC, "
				+ ADDRESS_KEY_IS_DEFAULT_BILL + " NUMERIC)";
		db.execSQL(CREATE_ADDRESS_TABLE);
		/*String CREATE_ORDER_DETAIL_HISTORY = "CREATE TABLE " + TABLE_CART + "("
				+ CART_PRO_KEY_ID + " INTEGER PRIMARY KEY ,"
				+ CART_PRO_KEY_QUANTITY + " INTEGER, " + CART_PRO_KEY_DISCOUNT
				+ " TEXT," + CART_PRO_KEY_DISCOUNT_PRICE + " REAL, "
				+ CART_PRO_KEY_ATTRIBUTE + " TEXT, "
				+ CART_PRO_KEY_STITCH_PRICE + " REAL)";*/
		Log.i("DB Created :", "Table Created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBCATEGORIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
		Log.i("DB Upgrade :", "Table Droped");
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new category
	public void addCategory(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CAT_KEY_ID, category.getID());
		values.put(CAT_KEY_NAME, category.getName());
		values.put(CAT_KEY_DATETIME, category.getDatetimeInLong());

		// Inserting Row

		db.insertWithOnConflict(TABLE_CATEGORIES, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);

		db.close(); // Closing database connection

		for (SubCategory subCat : category.getSubcategories()) {
			addSubCategory(subCat);
		}
	}

	// Getting single category
	public Category getCategory(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CATEGORIES, new String[] { CAT_KEY_ID,
				CAT_KEY_NAME, CAT_KEY_DATETIME }, CAT_KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Category category = new Category(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getLong(2), getAllSubCategories(id));

		cursor.close();
		// return category
		return category;
	}

	// Getting All Categories
	public List<Category> getAllCategories() {
		List<Category> categoryList = new ArrayList<Category>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CATEGORIES
				+ " ORDER BY " + CAT_KEY_NAME + " ASC";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Category category = new Category();
				category.setID(Integer.parseInt(cursor.getString(0)));
				category.setName(cursor.getString(1));
				category.setDatetime(cursor.getLong(2));
				category.setSubcategories(getAllSubCategories(Integer
						.parseInt(cursor.getString(0))));
				// Adding category to list
				categoryList.add(category);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return category list
		return categoryList;
	}

	// Updating single category
	public int updateCategory(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CAT_KEY_NAME, category.getName());
		values.put(CAT_KEY_DATETIME, category.getDatetimeInLong());

		// updating row
		return db.update(TABLE_CATEGORIES, values, CAT_KEY_ID + " = ?",
				new String[] { String.valueOf(category.getID()) });
	}

	// Deleting single category
	public void deleteCategory(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CATEGORIES, CAT_KEY_ID + " = ?",
				new String[] { String.valueOf(category.getID()) });
		db.close();
	}

	// Getting categories Count
	public int getCategoriesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_CATEGORIES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	// Adding new SubCategory
	public void addSubCategory(SubCategory subcategory) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SUB_KEY_ID, subcategory.getID());
		values.put(SUB_KEY_NAME, subcategory.getName());
		values.put(SUB_KEY_CAT_DATETIME, subcategory.getDatetimeInLong());
		values.put(SUB_KEY_CAT_ID, subcategory.getCatID());

		// Inserting Row
		db.insertWithOnConflict(TABLE_SUBCATEGORIES, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);

		db.close(); // Closing database connection
	}

	// Getting single SubCategory
	public SubCategory getSubCategory(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_SUBCATEGORIES,
				new String[] { SUB_KEY_ID, SUB_KEY_NAME, SUB_KEY_CAT_DATETIME,
						SUB_KEY_CAT_ID }, SUB_KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		SubCategory subcategory = new SubCategory(Integer.parseInt(cursor
				.getString(0)), cursor.getString(1), cursor.getLong(2),
				Integer.parseInt(cursor.getString(3)));

		cursor.close();
		// return category
		return subcategory;
	}

	// Getting All SubCategories
	public List<SubCategory> getAllSubCategories() {
		List<SubCategory> subcategoryList = new ArrayList<SubCategory>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SUBCATEGORIES
				+ " ORDER BY " + SUB_KEY_NAME + " ASC";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				SubCategory subcategory = new SubCategory();
				subcategory.setID(Integer.parseInt(cursor.getString(0)));
				subcategory.setName(cursor.getString(1));
				subcategory.setDatetime(cursor.getLong(2));
				// Adding category to list
				subcategoryList.add(subcategory);
			} while (cursor.moveToNext());
		}
		cursor.close();
		// return category list
		return subcategoryList;
	}

	// Getting All SubCategories by categories
	public List<SubCategory> getAllSubCategories(int catId) {
		List<SubCategory> subcategoryList = new ArrayList<SubCategory>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SUBCATEGORIES
				+ " WHERE " + SUB_KEY_CAT_ID + " = ? " + " ORDER BY "
				+ SUB_KEY_NAME + " ASC";

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery,
				new String[] { String.valueOf(catId) });

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				SubCategory subcategory = new SubCategory();
				subcategory.setID(Integer.parseInt(cursor.getString(0)));
				subcategory.setName(cursor.getString(1));
				subcategory.setDatetime(cursor.getLong(2));
				subcategory.setCatID(catId);
				// Adding category to list
				subcategoryList.add(subcategory);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return category list
		return subcategoryList;
	}

	// Updating single SubCategory
	public int updateSubCategory(SubCategory subcategory) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(SUB_KEY_NAME, subcategory.getName());
		values.put(SUB_KEY_CAT_ID, subcategory.getCatID());
		values.put(SUB_KEY_CAT_DATETIME, subcategory.getDatetimeInLong());

		// updating row
		return db.update(TABLE_SUBCATEGORIES, values, SUB_KEY_ID + " = ?",
				new String[] { String.valueOf(subcategory.getID()) });
	}

	// Deleting single SubCategory
	public void deleteSubCategory(SubCategory subcategory) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SUBCATEGORIES, SUB_KEY_ID + " = ?",
				new String[] { String.valueOf(subcategory.getID()) });
		db.close();
	}

	// Getting SubCategories Count
	public int getSubCategoriesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_SUBCATEGORIES;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	public void deleteAll() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SUBCATEGORIES, null, null);
		db.delete(TABLE_CATEGORIES, null, null);
		db.close();
	}

	// Adding new Product
	public void addProduct(Product product) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PRO_KEY_ID, product.getID());
		values.put(PRO_KEY_NAME, product.getName()); // Contact Name
		values.put(PRO_KEY_DESCRIPTION, product.getDescription());
		values.put(PRO_KEY_CAT_ID, product.getCat());
		values.put(PRO_KEY_SUB_CAT_ID, product.getSub());
		values.put(PRO_KEY_PRICE, product.getPrice());
		values.put(PRO_KEY_STOCK, product.getStock());
		values.put(PRO_KEY_IMAGE_URL, product.getURL());
		values.put(PRO_KEY_IMAGE_LARGE_URL, product.getLargeUrl());
		values.put(PRO_KEY_DATETIME, product.getDatetimeInLong());
		// Inserting Row

		db.insertWithOnConflict(TABLE_PRODUCTS, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
	}

	// Getting single Product
	public Product getProduct(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PRODUCTS, new String[] { PRO_KEY_ID,
				PRO_KEY_NAME, PRO_KEY_IMAGE_URL, PRO_KEY_PRICE, PRO_KEY_STOCK,
				PRO_KEY_CAT_ID, PRO_KEY_SUB_CAT_ID, PRO_KEY_IMAGE_LARGE_URL,
				PRO_KEY_DESCRIPTION, PRO_KEY_DATETIME }, PRO_KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Product product = new Product(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getDouble(3), cursor.getInt(4),
				cursor.getInt(5), cursor.getInt(6), cursor.getString(7),
				cursor.getString(8), cursor.getLong(9));

		cursor.close();
		db.close();
		// return product
		return product;
	}

	// Getting All Product
	public List<Product> getAllProduct(long subCategory) {
		List<Product> productList = new ArrayList<Product>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_PRODUCTS, new String[] { PRO_KEY_ID,
				PRO_KEY_NAME, PRO_KEY_IMAGE_URL, PRO_KEY_PRICE, PRO_KEY_STOCK,
				PRO_KEY_CAT_ID, PRO_KEY_SUB_CAT_ID, PRO_KEY_IMAGE_LARGE_URL,
				PRO_KEY_DESCRIPTION, PRO_KEY_DATETIME }, PRO_KEY_SUB_CAT_ID
				+ "=?", new String[] { String.valueOf(subCategory) }, null,
				null, null, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Product product = new Product(cursor.getInt(0),
						cursor.getString(1), cursor.getString(2),
						cursor.getDouble(3), cursor.getInt(4),
						cursor.getInt(5), cursor.getInt(6),
						cursor.getString(7), cursor.getString(8),
						cursor.getLong(9));
				productList.add(product);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return category list
		return productList;
	}

	// Updating single Product
	public int updateProduct(Product product) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PRO_KEY_NAME, product.getName());
		values.put(PRO_KEY_DESCRIPTION, product.getDescription());
		values.put(PRO_KEY_CAT_ID, product.getCat());
		values.put(PRO_KEY_SUB_CAT_ID, product.getSub());
		values.put(PRO_KEY_PRICE, product.getPrice());
		values.put(PRO_KEY_STOCK, product.getStock());
		values.put(PRO_KEY_IMAGE_URL, product.getURL());
		values.put(PRO_KEY_IMAGE_LARGE_URL, product.getLargeUrl());
		values.put(PRO_KEY_DATETIME, product.getDatetimeInLong());

		// updating row
		return db.update(TABLE_PRODUCTS, values, PRO_KEY_ID + " = ?",
				new String[] { String.valueOf(product.getID()) });
	}

	// Deleting single Product
	public void deleteProduct(Product product) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PRODUCTS, PRO_KEY_ID + " = ?",
				new String[] { String.valueOf(product.getID()) });
		db.close();
	}

	// Getting Products Count
	public int getProductsCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PRODUCTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		db.close();
		// return count
		return cursor.getCount();
	}

	// Delete all products
	public void deleteAllProducts(long _subcategoryId) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PRODUCTS, PRO_KEY_SUB_CAT_ID + " = ?",
				new String[] { String.valueOf(_subcategoryId) });
		db.close();
	}

	// Adding new Product
	public void addCart(ShoppingCart cart) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CART_PRO_KEY_ID, cart.getProduct().getID());
		values.put(CART_PRO_KEY_QUANTITY, cart.getQuantity());
		values.put(CART_PRO_KEY_DISCOUNT, cart.getDiscountCoupon());
		values.put(CART_PRO_KEY_ATTRIBUTE, cart.getAttribute());
		values.put(CART_PRO_KEY_STITCH_PRICE, cart.getStitchPrice());
		values.put(CART_PRO_KEY_DISCOUNT_PRICE, cart.getDiscountPrice());
		// Inserting Row
		db.insertWithOnConflict(TABLE_CART, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
		db.close();
	}

	// Getting single Cart
	public ShoppingCart getCartItem(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CART, new String[] { CART_PRO_KEY_ID,
				CART_PRO_KEY_QUANTITY, CART_PRO_KEY_DISCOUNT,
				CART_PRO_KEY_DISCOUNT_PRICE, CART_PRO_KEY_ATTRIBUTE,
				CART_PRO_KEY_STITCH_PRICE }, CART_PRO_KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		ShoppingCart cart = null;

		if (cursor != null)
			if (cursor.moveToFirst())
				cart = new ShoppingCart(getProduct(cursor.getInt(0)),
						cursor.getInt(1), cursor.getString(2),
						cursor.getDouble(3), cursor.getString(4),
						cursor.getDouble(5));

		cursor.close();
		db.close();
		// return product
		return cart;
	}

	// Getting All Cart Item
	public List<ShoppingCart> getAllCartItem() {
		List<ShoppingCart> cartList = new ArrayList<ShoppingCart>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CART;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				ShoppingCart cart = new ShoppingCart(
						getProduct(cursor.getInt(0)), cursor.getInt(1),
						cursor.getString(2), cursor.getDouble(3),
						cursor.getString(4), cursor.getDouble(5));
				cartList.add(cart);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return category list
		return cartList;
	}

	// Updating single Cart
	public int updateCart(ShoppingCart cart) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CART_PRO_KEY_ID, cart.getProduct().getID());
		values.put(CART_PRO_KEY_QUANTITY, cart.getQuantity());
		values.put(CART_PRO_KEY_DISCOUNT, cart.getDiscountCoupon());
		values.put(CART_PRO_KEY_ATTRIBUTE, cart.getAttribute());
		values.put(CART_PRO_KEY_STITCH_PRICE, cart.getStitchPrice());
		values.put(CART_PRO_KEY_DISCOUNT_PRICE, cart.getDiscountPrice());

		// updating row
		return db.update(TABLE_CART, values, CART_PRO_KEY_ID + " = ?",
				new String[] { String.valueOf(cart.getProduct().getID()) });
	}

	// Updating single Cart
	public int updateCartAttribute(int id, String attribute) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CART_PRO_KEY_ATTRIBUTE, attribute);

		// updating row
		return db.update(TABLE_CART, values, CART_PRO_KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
	}

	// update Discount Price
	public int updateCartDiscountPrice(int id, double discountPrice) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(CART_PRO_KEY_DISCOUNT_PRICE, discountPrice);

		// updating row
		return db.update(TABLE_CART, values, CART_PRO_KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
	}

	// Getting Cart Count
	public int getCartCount() {
		String countQuery = "SELECT * FROM " + TABLE_CART;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int result = cursor.getCount();
		cursor.close();
		db.close();
		// return count
		return result;
	}

	// Delete Cart
	public void deleteCart() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CART, null, null);
		db.close();
	}

	// Delete Cart Item
	public void deleteCartItem(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CART, CART_PRO_KEY_ID + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	// Sub total From cart
	public double getCartSubTotal() {
		String query = "SELECT Sum(" + CART_PRO_KEY_QUANTITY + " * ( (SELECT "
				+ PRO_KEY_PRICE + " FROM " + TABLE_PRODUCTS + " WHERE "
				+ PRO_KEY_ID + " = " + TABLE_CART + "." + CART_PRO_KEY_ID
				+ " LIMIT 1) + " + CART_PRO_KEY_STITCH_PRICE
				+ ") ) AS Total FROM " + TABLE_CART;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		double result = 0;

		if (cursor.moveToFirst()) {
			result = cursor.getDouble(0);
		}
		cursor.close();
		db.close();
		return result;
	}

	// Discount total From cart
	public double getCartDiscountTotal() {
		String query = "SELECT Sum(" + CART_PRO_KEY_QUANTITY + " * "
				+ CART_PRO_KEY_DISCOUNT_PRICE + " ) AS Total FROM "
				+ TABLE_CART;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		double result = 0;

		if (cursor.moveToFirst()) {
			result = cursor.getDouble(0);
		}
		cursor.close();
		db.close();
		return result;
	}

	// Total from cart
	public double getCartTotal() {
		String query = "SELECT Sum(" + CART_PRO_KEY_QUANTITY + " * ( (SELECT "
				+ PRO_KEY_PRICE + " FROM " + TABLE_PRODUCTS + " WHERE "
				+ PRO_KEY_ID + " = " + TABLE_CART + "." + CART_PRO_KEY_ID
				+ " LIMIT 1) + " + CART_PRO_KEY_STITCH_PRICE + " - "
				+ CART_PRO_KEY_DISCOUNT_PRICE + ") ) AS Total FROM "
				+ TABLE_CART;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		double result = 0;

		if (cursor.moveToFirst()) {
			result = cursor.getDouble(0);
		}
		cursor.close();
		db.close();
		return result;
	}

	// Adding new Prorfile
	public void addProfile(Profile profile) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PROFILE_KEY_EMAIL, profile.getEmail());
		values.put(PROFILE_KEY_FIRSTNAME, profile.getFirstName());
		values.put(PROFILE_KEY_LASTNAME, profile.getLastName());
		values.put(PROFILE_KEY_PHONE, profile.getPhone());
		values.put(PROFILE_KEY_MOBILE, profile.getMobile());
		values.put(PROFILE_KEY_ADDRESS, profile.getAddress());
		values.put(PROFILE_KEY_CITY, profile.getCity());
		values.put(PROFILE_KEY_STATE, profile.getState());
		values.put(PROFILE_KEY_ZIP, profile.getZip());
		values.put(PROFILE_KEY_COUNTRY, profile.getCountry());
		values.put(PROFILE_KEY_TOKEN, profile.getToken());

		clearToken(db);
		db.insertWithOnConflict(TABLE_PROFILE, null, values,
				SQLiteDatabase.CONFLICT_REPLACE);
	}

	// Getting single Profile
	public Profile getProfile(String email) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PROFILE, new String[] {
				PROFILE_KEY_EMAIL, PROFILE_KEY_FIRSTNAME, PROFILE_KEY_LASTNAME,
				PROFILE_KEY_PHONE, PROFILE_KEY_MOBILE, PROFILE_KEY_ADDRESS,
				PROFILE_KEY_CITY, PROFILE_KEY_STATE, PROFILE_KEY_ZIP,
				PROFILE_KEY_COUNTRY, PROFILE_KEY_TOKEN }, PROFILE_KEY_EMAIL
				+ " = ?", new String[] { email }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Profile profile = new Profile(cursor.getString(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6), cursor.getString(7),
				cursor.getString(8), cursor.getString(9), cursor.getString(10));

		cursor.close();
		db.close();
		// return product
		return profile;
	}

	// Getting single Profile
	public Profile getLogedInProfile() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PROFILE, new String[] {
				PROFILE_KEY_EMAIL, PROFILE_KEY_FIRSTNAME, PROFILE_KEY_LASTNAME,
				PROFILE_KEY_PHONE, PROFILE_KEY_MOBILE, PROFILE_KEY_ADDRESS,
				PROFILE_KEY_CITY, PROFILE_KEY_STATE, PROFILE_KEY_ZIP,
				PROFILE_KEY_COUNTRY, PROFILE_KEY_TOKEN }, PROFILE_KEY_TOKEN
				+ " IS NOT NULL", null, null, null, null, "1");

		Profile profile = null;
		if (cursor != null)
			if (cursor.moveToFirst()) {
				profile = new Profile(cursor.getString(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getString(7),
						cursor.getString(8), cursor.getString(9),
						cursor.getString(10));
			}
		cursor.close();
		db.close();
		// return product
		return profile;
	}

	// Getting All Product
	public List<Profile> getAllProfile() {
		List<Profile> profileList = new ArrayList<Profile>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_PROFILE, new String[] {
				PROFILE_KEY_EMAIL, PROFILE_KEY_FIRSTNAME, PROFILE_KEY_LASTNAME,
				PROFILE_KEY_PHONE, PROFILE_KEY_MOBILE, PROFILE_KEY_ADDRESS,
				PROFILE_KEY_CITY, PROFILE_KEY_STATE, PROFILE_KEY_ZIP,
				PROFILE_KEY_COUNTRY, PROFILE_KEY_TOKEN }, null, null, null,
				null, null, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Profile profile = new Profile(cursor.getString(0),
						cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6),
						cursor.getString(7), cursor.getString(8),
						cursor.getString(9), cursor.getString(10));
				profileList.add(profile);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return category list
		return profileList;
	}

	// Updating single Product
	public int updateProfile(Profile profile) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(PROFILE_KEY_FIRSTNAME, profile.getFirstName());
		values.put(PROFILE_KEY_LASTNAME, profile.getLastName());
		values.put(PROFILE_KEY_PHONE, profile.getPhone());
		values.put(PROFILE_KEY_MOBILE, profile.getMobile());
		values.put(PROFILE_KEY_ADDRESS, profile.getAddress());
		values.put(PROFILE_KEY_CITY, profile.getCity());
		values.put(PROFILE_KEY_STATE, profile.getState());
		values.put(PROFILE_KEY_ZIP, profile.getZip());
		values.put(PROFILE_KEY_COUNTRY, profile.getCountry());
		values.put(PROFILE_KEY_TOKEN, profile.getToken());

		// updating row
		return db.update(TABLE_PROFILE, values, PROFILE_KEY_EMAIL + " = ?",
				new String[] { profile.getEmail() });
	}

	// Clear Token
	private int clearToken(SQLiteDatabase db) {
		ContentValues values = new ContentValues();
		values.putNull(PROFILE_KEY_TOKEN);

		return db.update(TABLE_PROFILE, values, null, null);
	}

	// Deleting single Product
	public void deleteProfile(Profile profile) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PROFILE, PROFILE_KEY_EMAIL + " = ?",
				new String[] { profile.getEmail() });
		db.close();
	}

	// Getting Products Count
	public int getProfilesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_PROFILE;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		db.close();
		// return count
		return cursor.getCount();
	}

	// Delete all products
	public void deleteAllProfiles() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PROFILE, null, null);
		db.close();
	}

	// Adding new Address
	public void addAddress(Address address) {
		SQLiteDatabase db = this.getWritableDatabase();

		if (!isExistAddress(address)) {
			ContentValues values = new ContentValues();
			values.put(ADDRESS_KEY_ADDRESS, address.getAddress());
			values.put(ADDRESS_KEY_CITY, address.getCity());
			values.put(ADDRESS_KEY_STATE, address.getState());
			values.put(ADDRESS_KEY_COUNTRY, address.getCountry());
			values.put(ADDRESS_KEY_ZIP, address.getZip());
			values.put(ADDRESS_KEY_PHONE, address.getPhoneNo());
			values.put(ADDRESS_KEY_IS_CURRENT_SHIP, address.isCurrentShip());
			values.put(ADDRESS_KEY_IS_CURRENT_BILL, address.isCurrentBill());
			values.put(ADDRESS_KEY_IS_DEFAULT_SHIP, address.isShip());
			values.put(ADDRESS_KEY_IS_DEFAULT_BILL, address.isBill());
			
			if(address.isCurrentShip() == 1)
				clearCurrentShip();
			if(address.isCurrentBill() == 1)
				clearCurrentBill();
			if(address.isShip() == 1)
				clearDefaultShip();
			if(address.isBill() == 1)
				clearDefaultBill();

			db.insertWithOnConflict(TABLE_ADDRESS, null, values,
					SQLiteDatabase.CONFLICT_REPLACE);
		}
	}

	public boolean isExistAddress(Address address) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(
				TABLE_ADDRESS,
				new String[] { ADDRESS_KEY_ID, ADDRESS_KEY_ADDRESS,
						ADDRESS_KEY_CITY, ADDRESS_KEY_STATE,
						ADDRESS_KEY_COUNTRY, ADDRESS_KEY_ZIP,
						ADDRESS_KEY_PHONE, ADDRESS_KEY_IS_CURRENT_SHIP,
						ADDRESS_KEY_IS_CURRENT_BILL,
						ADDRESS_KEY_IS_DEFAULT_SHIP,
						ADDRESS_KEY_IS_DEFAULT_BILL },
				ADDRESS_KEY_ADDRESS + " = ? AND " + ADDRESS_KEY_CITY
						+ " = ? AND " + ADDRESS_KEY_STATE + " = ? AND "
						+ ADDRESS_KEY_COUNTRY + " = ? AND " + ADDRESS_KEY_ZIP
						+ " = ? AND " + ADDRESS_KEY_PHONE + " = ? AND "
						+ ADDRESS_KEY_IS_CURRENT_SHIP + " = ? AND "
						+ ADDRESS_KEY_IS_CURRENT_BILL + " = ? AND "
						+ ADDRESS_KEY_IS_DEFAULT_SHIP + " = ? AND "
						+ ADDRESS_KEY_IS_DEFAULT_BILL + " = ? ",
				new String[] { address.getAddress(), address.getCity(),
						address.getState(), address.getCountry(),
						address.getZip(), address.getPhoneNo(),
						String.valueOf(address.isCurrentShip()),
						String.valueOf(address.isCurrentBill()),
						String.valueOf(address.isShip()),
						String.valueOf(address.isBill()) }, null, null, null,
				null);
		boolean flag = false;
		if (cursor != null)
			if (cursor.moveToFirst()) {
				flag = true;
			}
		cursor.close();
		return flag;
	}

	// Getting single Address
	public Address getAddress(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ADDRESS, new String[] { ADDRESS_KEY_ID,
				ADDRESS_KEY_ADDRESS, ADDRESS_KEY_CITY, ADDRESS_KEY_STATE,
				ADDRESS_KEY_COUNTRY, ADDRESS_KEY_ZIP, ADDRESS_KEY_PHONE,
				ADDRESS_KEY_IS_CURRENT_SHIP, ADDRESS_KEY_IS_CURRENT_BILL,
				ADDRESS_KEY_IS_DEFAULT_SHIP, ADDRESS_KEY_IS_DEFAULT_BILL },
				ADDRESS_KEY_ID + " = ?", new String[] { String.valueOf(id) },
				null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Address address = new Address(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6), cursor.getInt(7),
				cursor.getInt(8), cursor.getInt(9), cursor.getInt(10));

		cursor.close();
		db.close();
		// return Address
		return address;
	}

	// Getting single Address
	public Address getDefaultShipAddress() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ADDRESS, new String[] { ADDRESS_KEY_ID,
				ADDRESS_KEY_ADDRESS, ADDRESS_KEY_CITY, ADDRESS_KEY_STATE,
				ADDRESS_KEY_COUNTRY, ADDRESS_KEY_ZIP, ADDRESS_KEY_PHONE,
				ADDRESS_KEY_IS_CURRENT_SHIP, ADDRESS_KEY_IS_CURRENT_BILL,
				ADDRESS_KEY_IS_DEFAULT_SHIP, ADDRESS_KEY_IS_DEFAULT_BILL },
				ADDRESS_KEY_IS_DEFAULT_SHIP + " = ? ",
				new String[] { String.valueOf(1) }, null, null, null, null);

		Address address = null;

		if (cursor != null)
			if (cursor.moveToLast()) {
				address = new Address(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getInt(7),
						cursor.getInt(8), cursor.getInt(9), cursor.getInt(10));
			}
		cursor.close();
		db.close();
		// return Address
		return address;
	}

	public Address getDefaultBillAddress() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ADDRESS, new String[] { ADDRESS_KEY_ID,
				ADDRESS_KEY_ADDRESS, ADDRESS_KEY_CITY, ADDRESS_KEY_STATE,
				ADDRESS_KEY_COUNTRY, ADDRESS_KEY_ZIP, ADDRESS_KEY_PHONE,
				ADDRESS_KEY_IS_CURRENT_SHIP, ADDRESS_KEY_IS_CURRENT_BILL,
				ADDRESS_KEY_IS_DEFAULT_SHIP, ADDRESS_KEY_IS_DEFAULT_BILL },
				ADDRESS_KEY_IS_DEFAULT_BILL + " = ? ",
				new String[] { String.valueOf(1) }, null, null, null, null);

		Address address = null;

		if (cursor != null)
			if (cursor.moveToLast()) {
				address = new Address(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getInt(7),
						cursor.getInt(8), cursor.getInt(9), cursor.getInt(10));
			}
		cursor.close();
		db.close();
		// return Address
		return address;
	}

	public Address getCurrentShipAddress() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ADDRESS, new String[] { ADDRESS_KEY_ID,
				ADDRESS_KEY_ADDRESS, ADDRESS_KEY_CITY, ADDRESS_KEY_STATE,
				ADDRESS_KEY_COUNTRY, ADDRESS_KEY_ZIP, ADDRESS_KEY_PHONE,
				ADDRESS_KEY_IS_CURRENT_SHIP, ADDRESS_KEY_IS_CURRENT_BILL,
				ADDRESS_KEY_IS_DEFAULT_SHIP, ADDRESS_KEY_IS_DEFAULT_BILL },
				ADDRESS_KEY_IS_CURRENT_SHIP + " = ? ",
				new String[] { String.valueOf(1) }, null, null, null, null);

		Address address = null;

		if (cursor != null)
			if (cursor.moveToLast()) {
				address = new Address(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getInt(7),
						cursor.getInt(8), cursor.getInt(9), cursor.getInt(10));
			}
		cursor.close();
		db.close();
		// return Address
		return address;
	}

	public Address getCurrentBillAddress() {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_ADDRESS, new String[] { ADDRESS_KEY_ID,
				ADDRESS_KEY_ADDRESS, ADDRESS_KEY_CITY, ADDRESS_KEY_STATE,
				ADDRESS_KEY_COUNTRY, ADDRESS_KEY_ZIP, ADDRESS_KEY_PHONE,
				ADDRESS_KEY_IS_CURRENT_SHIP, ADDRESS_KEY_IS_CURRENT_BILL,
				ADDRESS_KEY_IS_DEFAULT_SHIP, ADDRESS_KEY_IS_DEFAULT_BILL },
				ADDRESS_KEY_IS_CURRENT_BILL + " = ? ",
				new String[] { String.valueOf(1) }, null, null, null, null);

		Address address = null;

		if (cursor != null)
			if (cursor.moveToLast()) {
				address = new Address(cursor.getInt(0), cursor.getString(1),
						cursor.getString(2), cursor.getString(3),
						cursor.getString(4), cursor.getString(5),
						cursor.getString(6), cursor.getInt(7),
						cursor.getInt(8), cursor.getInt(9), cursor.getInt(10));
			}
		cursor.close();
		db.close();
		// return Address
		return address;
	}

	public int clearCurrentShip() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.putNull(ADDRESS_KEY_IS_CURRENT_SHIP);

		// updating row
		return db.update(TABLE_ADDRESS, values, ADDRESS_KEY_IS_CURRENT_SHIP
				+ " = ?", new String[] { "1" });
	}

	public int clearCurrentBill() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.putNull(ADDRESS_KEY_IS_CURRENT_BILL);

		// updating row
		return db.update(TABLE_ADDRESS, values, ADDRESS_KEY_IS_CURRENT_BILL
				+ " = ?", new String[] { "1" });
	}

	public int clearDefaultShip() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.putNull(ADDRESS_KEY_IS_DEFAULT_SHIP);

		// updating row
		return db.update(TABLE_ADDRESS, values, ADDRESS_KEY_IS_DEFAULT_SHIP
				+ " = ?", new String[] { "1" });
	}

	public int clearDefaultBill() {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.putNull(ADDRESS_KEY_IS_DEFAULT_BILL);

		// updating row
		return db.update(TABLE_ADDRESS, values, ADDRESS_KEY_IS_DEFAULT_BILL
				+ " = ?", new String[] { "1" });
	}

	// Getting All Address
	public List<Address> getAllAddress() {
		List<Address> addressList = new ArrayList<Address>();

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_ADDRESS, new String[] { ADDRESS_KEY_ID,
				ADDRESS_KEY_ADDRESS, ADDRESS_KEY_CITY, ADDRESS_KEY_STATE,
				ADDRESS_KEY_COUNTRY, ADDRESS_KEY_ZIP, ADDRESS_KEY_PHONE,
				ADDRESS_KEY_IS_CURRENT_SHIP, ADDRESS_KEY_IS_CURRENT_BILL,
				ADDRESS_KEY_IS_DEFAULT_SHIP, ADDRESS_KEY_IS_DEFAULT_BILL },
				null, null, null, null, null, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Address address = new Address(cursor.getInt(0),
						cursor.getString(1), cursor.getString(2),
						cursor.getString(3), cursor.getString(4),
						cursor.getString(5), cursor.getString(6),
						cursor.getInt(7), cursor.getInt(8), cursor.getInt(9),
						cursor.getInt(10));
				addressList.add(address);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		// return category list
		return addressList;
	}

	// Updating single Address
	public int updateAddress(Address address) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(ADDRESS_KEY_ADDRESS, address.getAddress());
		values.put(ADDRESS_KEY_CITY, address.getCity());
		values.put(ADDRESS_KEY_STATE, address.getState());
		values.put(ADDRESS_KEY_COUNTRY, address.getCountry());
		values.put(ADDRESS_KEY_ZIP, address.getZip());
		values.put(ADDRESS_KEY_PHONE, address.getPhoneNo());
		values.put(ADDRESS_KEY_IS_CURRENT_SHIP, address.isCurrentShip());
		values.put(ADDRESS_KEY_IS_CURRENT_BILL, address.isCurrentBill());
		values.put(ADDRESS_KEY_IS_DEFAULT_SHIP, address.isShip());
		values.put(ADDRESS_KEY_IS_DEFAULT_BILL, address.isBill());

		// updating row
		return db.update(TABLE_ADDRESS, values, ADDRESS_KEY_ID + " = ?",
				new String[] { String.valueOf(address.getId()) });
	}

}
