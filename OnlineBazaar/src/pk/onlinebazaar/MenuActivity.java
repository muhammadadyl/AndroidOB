package pk.onlinebazaar;

import java.util.List;
import pk.onlinebazaar.helpers.DatabaseHandler;
import pk.onlinebazaar.helpers.ExpandableListAdapter;
import pk.onlinebazaar.model.Category;
import pk.onlinebazaar.model.SubCategory;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

public class MenuActivity extends Activity {
	
	ExpandableListView expListView;
	
	ExpandableListAdapter expListAdapter;

	private final int MENU_SEARCH = 0;
	
	private final int MENU_CART = 1;
	
	private final int MENU_SETTINGS = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_menu);

		DatabaseHandler db = new DatabaseHandler(this);
		
		expandableList(this, db.getAllCategories());
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_SEARCH, 0, "Search");
		menu.add(0, MENU_CART, 0, "Goto Cart");
		menu.add(0, MENU_SETTINGS, 0, "Settings");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// when you click setting menu
		switch (item.getItemId()) {
		case MENU_SETTINGS:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			return true;
		case MENU_SEARCH:
			/*Intent intent = new Intent(this, ShoppingCartActivity.class);
			startActivity(intent);
			finish();*/
			return true;
		case MENU_CART:
			Intent intentCart = new Intent(this, ShoppingCartActivity.class);
			startActivity(intentCart);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}
	
	public void expandableList(Activity context, List<Category> categories) {

		expListView = (ExpandableListView) findViewById(R.id.category_list);
		expListAdapter = new ExpandableListAdapter(context, categories);
		expListView.setAdapter(expListAdapter);

		setGroupIndicatorToRight();

		expListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
				final SubCategory selected = (SubCategory) expListAdapter.getChild(groupPosition, childPosition);
				Log.i("CategoryId", Integer.toString(selected.getCatID()));
				Log.i("Category Name", selected.getName());
				Log.i("SubCategoryId", Integer.toString(selected.getID()));
				Log.i("Id", Long.toString(id));
				Intent productListIntent = new Intent(MenuActivity.this, DynamicListViewActivity.class);
				productListIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				productListIntent.putExtra("subId", id);
				startActivity(productListIntent);
				return true;
			}
		});

	}
	
	private void setGroupIndicatorToRight() {
		/* Get the screen width */
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;

		expListView.setIndicatorBounds(width - getDipsFromPixel(35), width- getDipsFromPixel(5));
	}

	// Convert pixel to dip
	public int getDipsFromPixel(float pixels) {
		// Get the screen's density scale
		final float scale = getResources().getDisplayMetrics().density;
		// Convert the dps to pixels, based on density scale
		return (int) (pixels * scale + 0.5f);
	}

}
