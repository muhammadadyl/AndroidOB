package pk.onlinebazaar.helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import pk.onlinebazaar.model.Product;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

public class StoreImagesToStorage {

	public final static String APP_PATH_SD_CARD = "/OnlineBazaar/";
	public final static String APP_THUMBNAIL_PATH_SD_CARD = "Images";
	private Context context;

	public StoreImagesToStorage(Context context) {
		this.context = context;
	}

	public boolean SaveImage(String fileName, Bitmap image) {
		if (isExternalStorageAvailable()) {
			return saveImageToExternalStorage(fileName, image);
		}
		return saveImageToInternalStorage(fileName, image);
	}

	public boolean saveImageToExternalStorage(String fileName, Bitmap image) {
		String fullPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ APP_PATH_SD_CARD
				+ APP_THUMBNAIL_PATH_SD_CARD;

		try {
			File dir = new File(fullPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			OutputStream fOut = null;
			File file = new File(fullPath, fileName);
			file.createNewFile();
			fOut = new FileOutputStream(file);

			// 100 means no compression, the lower you go, the stronger the
			// compression
			image.compress(Bitmap.CompressFormat.PNG, 0, fOut);
			fOut.flush();
			fOut.close();

			MediaStore.Images.Media.insertImage(context.getContentResolver(),
					file.getAbsolutePath(), file.getName(), file.getName());

			return true;

		} catch (Exception e) {
			Log.e("saveToExternalStorage()", e.getMessage());
			return false;
		}
	}

	public boolean saveImageToInternalStorage(String fileName, Bitmap image) {

		try {
			// Use the compress method on the Bitmap object to write image to
			// the OutputStream
			FileOutputStream fos = context.openFileOutput(fileName,
					Context.MODE_PRIVATE);

			// Writing the bitmap to the output stream
			image.compress(Bitmap.CompressFormat.PNG, 0, fos);
			fos.close();

			return true;
		} catch (Exception e) {
			Log.e("saveToInternalStorage()", e.getMessage());
			return false;
		}
	}

	public boolean isFileExist(String fileName) {
		String fullPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ APP_PATH_SD_CARD
				+ APP_THUMBNAIL_PATH_SD_CARD;
		if (isExternalStorageAvailable()) {
			File f = new File(fullPath, fileName);
			if (f.exists()) {
				return true;
			} else {
				File fi = context.getFileStreamPath(fileName);
				return fi.exists();
			}
		} else {
			File fi = context.getFileStreamPath(fileName);
			return fi.exists();
		}
	}

	public boolean isSdReadable() {

		boolean mExternalStorageAvailable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = true;
			Log.i("isSdReadable", "External storage card is readable.");
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			Log.i("isSdReadable", "External storage card is readable.");
			mExternalStorageAvailable = true;
		} else {
			// Something else is wrong. It may be one of many other
			// states, but all we need to know is we can neither read nor write
			mExternalStorageAvailable = false;
		}

		return mExternalStorageAvailable;
	}

	public boolean isExternalStorageAvailable() {
		String extStorageState = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
			return true;
		}
		return false;
	}

	public Bitmap getThumbnail(String filename) {

		String fullPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ APP_PATH_SD_CARD
				+ APP_THUMBNAIL_PATH_SD_CARD;
		Bitmap thumbnail = null;

		// Look for the file on the external storage
		try {
			if (isSdReadable() == true) {
				thumbnail = BitmapFactory.decodeFile(fullPath + "/" + filename);
			}
		} catch (Exception e) {
			Log.e("getThumbnail() on external storage", e.getMessage());
		}

		// If no file on external storage, look in internal storage
		if (thumbnail == null) {
			try {
				File filePath = context.getFileStreamPath(filename);
				FileInputStream fi = new FileInputStream(filePath);
				thumbnail = BitmapFactory.decodeStream(fi);
			} catch (Exception ex) {
				Log.e("getThumbnail() on internal storage", ex.getMessage());
			}
		}
		return thumbnail;
	}

	public boolean deleteFiles(String fileName) {
		String fullPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ APP_PATH_SD_CARD
				+ APP_THUMBNAIL_PATH_SD_CARD;
		if (isExternalStorageAvailable()) {
			File f = new File(fullPath, fileName);
			if (f.delete()) {
				return true;
			} else {
				File fi = context.getFileStreamPath(fileName);
				return fi.delete();
			}
		} else {
			File fi = context.getFileStreamPath(fileName);
			return fi.delete();
		}
	}

	public void cleanOldFiles(List<Product> products) {
		String fullPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ APP_PATH_SD_CARD
				+ APP_THUMBNAIL_PATH_SD_CARD;
		if (isExternalStorageAvailable()) {
			File dir = new File(fullPath);
			if (dir.exists()) {
				String[] files = dir.list();
				for (String fileName : files) {
					if (!isFileExist(products, fileName)) {
						new File(fullPath, fileName).delete();
					}
				}
			}
		} else {
			File fi = context.getFilesDir();
			String[] files = fi.list();
			if (files != null) {
				for (String fileName : files) {
					if (!isFileExist(products, fileName)) {
						context.getFileStreamPath(fileName).delete();
					}
				}
			}
		}
	}

	public boolean isFileExist(List<Product> products, String fileName) {
		boolean check = false;
		for (Product p : products) {
			if (fileName.contains("medium"))
				if (fileName
						.contentEquals("medium-"
								+ p.getURL().substring(
										p.getURL().lastIndexOf('/') + 1))) {
					check = true;
					break;
				}
			if (fileName.contains("large"))
				if (fileName.contentEquals("large-"
						+ p.getLargeUrl().substring(
								p.getLargeUrl().lastIndexOf('/') + 1))) {
					check = true;
					break;
				}
		}
		return check;
	}
}
