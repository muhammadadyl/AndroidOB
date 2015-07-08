package pk.onlinebazaar;

import java.util.List;

import pk.onlinebazaar.control.LoadMoreListView;
import pk.onlinebazaar.control.LoadMoreListView.OnLoadMoreListener;
import pk.onlinebazaar.helpers.ImageListAdapter;
import pk.onlinebazaar.helpers.LoadFeedData;
import pk.onlinebazaar.helpers.LoadFeedData.LoadFeedDataFinishedListener;
import pk.onlinebazaar.model.Product;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class DynamicListViewActivity extends ListActivity implements
		LoadFeedDataFinishedListener {

	private ImageListAdapter adapter;
	
	private LoadFeedData loadFeedData;
	
	private long subCategoryId;
	
	private final int MENU_SEARCH = 0;
	
	private final int MENU_CART = 1;
	
	private final int MENU_SETTINGS = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products_list);

		Intent menuIntent = getIntent();
		subCategoryId = menuIntent.getLongExtra("subId", 0);

		adapter = new ImageListAdapter(this);
		setListAdapter(adapter);

		loadList(true);

		((LoadMoreListView) getListView())
				.setOnLoadMoreListener(new OnLoadMoreListener() {
					@Override
					public void onLoadMore() {
						loadList(false);
					}
				});
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
			/*
			 * Intent intent = new Intent(this, MainActivity.class);
			 * startActivity(intent);
			 */
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

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Product product = (Product) adapter.getItem(position);
		Log.i("id", String.valueOf(product.getID()));
		Log.i("Name", product.getName());
		Log.i("id", String.valueOf(id));
		Intent productOnItemIntent = new Intent(DynamicListViewActivity.this,
				ProductDetailActivity.class);
		productOnItemIntent.putExtra("product", product);
		productOnItemIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(productOnItemIntent);

	}

	public void loadList(boolean refresh) {
		loadFeedData = new LoadFeedData(adapter, refresh,
				DynamicListViewActivity.this, DynamicListViewActivity.this,
				subCategoryId);
		loadFeedData.execute();
	}

	@Override
	public void onRefreshComplete() {
		
	}

	@Override
	public void onLoadMoreComplete() {
		((LoadMoreListView) getListView()).onLoadMoreComplete();
	}

	@Override
	public void onLoadMoreCompleteIfListEnd(List<Product> products) {
		((LoadMoreListView) getListView()).onLoadMoreCompleteIfListEnd();
	}

}
