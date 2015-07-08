package pk.onlinebazaar.helpers;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import pk.onlinebazaar.R;
import pk.onlinebazaar.helpers.ImageDownloader.ImageExtender;
import pk.onlinebazaar.model.Product;
import pk.onlinebazaar.model.ShoppingCart;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageListAdapter extends BaseAdapter implements ImageExtender {

	private Context mContext;

	private LayoutInflater mLayoutInflater;

	public List<Product> products = new ArrayList<Product>();

	private final ImageDownloader mImageDownloader;

	private StoreImagesToStorage storeImage;

	private Hashtable<String, Integer> urlId;

	private boolean isCart;

	private ShoppingCartHelper cartHelper;

	private OnFinishDeleteEvent OnFinishDeleteEventObj;

	public ImageListAdapter(Context context) {
		mContext = context;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mImageDownloader = new ImageDownloader(this);
		storeImage = new StoreImagesToStorage(context);
		urlId = new Hashtable<String, Integer>();
		cartHelper = new ShoppingCartHelper(context);
	}

	@Override
	public int getCount() {
		return products.size();
	}

	@Override
	public Object getItem(int position) {
		return products.get(position);
	}

	@Override
	public long getItemId(int position) {
		return products.get(position).getID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout itemView;
		final Product item = products.get(position);
		if (convertView == null) {
			itemView = (RelativeLayout) mLayoutInflater.inflate(
					R.layout.list_item, parent, false);
		} else {
			itemView = (RelativeLayout) convertView;
		}

		ImageView imageView = (ImageView) itemView.findViewById(R.id.listImage);
		TextView titleText = (TextView) itemView.findViewById(R.id.listTitle);
		TextView priceText = (TextView) itemView.findViewById(R.id.listPrice);
		TextView quantityText = (TextView) itemView
				.findViewById(R.id.listQuantity);
		ImageView deleteTv = (ImageView) itemView.findViewById(R.id.deleteView);
		TextView totalTv = (TextView) itemView.findViewById(R.id.listTotal);
		String imageUrl = item.getURL();
		String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);

		if (storeImage.isFileExist("medium-" + fileName))
			imageView.setImageBitmap(storeImage.getThumbnail("medium-"
					+ fileName));
		else if (!urlId.containsKey(imageUrl)) {
			mImageDownloader.download(imageUrl, imageView);
			urlId.put(imageUrl, item.getID());
		} else {
			mImageDownloader.download(imageUrl, imageView);
		}

		String title = item.getName();
		titleText.setText(title);
		String description = "Price: USD " + Double.toString(item.getPrice());
		if (description.trim().length() == 0) {
			description = "Sorry, no description for this image.";
		}
		priceText.setText(description);
		if (isCart) {
			DecimalFormat df = new DecimalFormat("#.##");
			ShoppingCart cart = cartHelper.getCartItem(item);
			priceText.setText("Price: USD "
					+ df.format(item.getPrice() + cart.getStitchPrice()));
			quantityText.setText("Quantity: " + cart.getQuantity());
			quantityText.setVisibility(View.VISIBLE);
			totalTv.setText(cart.getQuantity()
					+ " X "
					+ df.format(item.getPrice() + cart.getStitchPrice())
					+ " = "
					+ df.format((item.getPrice() + cart.getStitchPrice())
							* cart.getQuantity()));
			totalTv.setVisibility(View.VISIBLE);
			deleteTv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							mContext);
					builder.setMessage("Do you want to remove?");
					builder.setCancelable(false);
					builder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									cartHelper.removeProduct(item);
									products.remove(item);
									// notifyDataSetChanged();
									OnFinishDeleteEventObj.onFinishDelete();
								}
							});
					builder.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
					AlertDialog alertDialog = builder.create();
					alertDialog.show();
				}
			});

			deleteTv.setVisibility(View.VISIBLE);
		} else {
			quantityText.setVisibility(View.GONE);
			deleteTv.setVisibility(View.GONE);
		}

		return itemView;
	}

	public void upDateEntries(List<Product> products, boolean refresh) {
		if (refresh)
			this.products = products;
		else if (products.size() > 0)
			this.products.addAll(products);
		notifyDataSetChanged();
	}

	public void updateCartEntries(List<Product> products) {
		this.isCart = true;
		this.products = products;
		notifyDataSetChanged();
	}

	public List<Product> GetAllProduct() {
		return this.products;
	}

	@Override
	public void saveImage(String url, Bitmap image) {
		new SaveImagetask().execute(url);
	}

	public OnFinishDeleteEvent getOnFinishDeleteEventObj() {
		return OnFinishDeleteEventObj;
	}

	public void setOnFinishDeleteEventObj(
			OnFinishDeleteEvent onFinishDeleteEventObj) {
		OnFinishDeleteEventObj = onFinishDeleteEventObj;
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
}