package pk.onlinebazaar;

import java.util.List;
import pk.onlinebazaar.helpers.DatabaseHandler;
import pk.onlinebazaar.helpers.LoadingTask;
import pk.onlinebazaar.helpers.LoadingTask.LoadingTaskFinishedListener;
import pk.onlinebazaar.model.Category;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashActivity extends Activity implements
		LoadingTaskFinishedListener {

	private DatabaseHandler db;
	private static boolean ISTEST = false; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);

		ProgressBar progressBar = (ProgressBar) findViewById(R.id.activity_splash_progress_bar);
		if (GetConnectivityStatus(getBaseContext()) && !ISTEST) {
			new LoadingTask(progressBar, this).execute();
			db = new DatabaseHandler(this);
		} else {
			completeSplash();
		}
	}

	@Override
	public void onTaskFinished(List<Category> result) {
		if(result != null){
		if (result.size() > 0)
			db.deleteAll();

		for (Category cat : result)
			db.addCategory(cat);
		
		completeSplash();
		}
		else {
			finish();
		}
	}

	private void completeSplash() {
		startApp();
		finish();
	}

	private void startApp() {
		Intent intent = new Intent(SplashActivity.this,
				MenuActivity.class);
		startActivity(intent);
	}

	private boolean GetConnectivityStatus(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}
}