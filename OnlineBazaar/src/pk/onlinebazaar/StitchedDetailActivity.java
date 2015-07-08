package pk.onlinebazaar;

import pk.onlinebazaar.helpers.DatabaseHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class StitchedDetailActivity extends Activity {

	private Spinner chestSize;
	private Spinner shoulderSize;
	private Spinner waistSize;
	private Spinner hipSize;
	private Spinner neckSize;
	private Spinner sleevesSize;
	private Spinner shirtSize;
	private Spinner trouserBottomSize;
	private Spinner trouserSize;
	private Spinner shirtStyle;
	private Spinner heightFeet;
	private Spinner heightInches;
	private Button btnProceedWithItem;
	private EditText edtInstruction;
	private DatabaseHandler db;
	private int Id;
	private String Size;
	private double StitchAmount;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stitching_details);
		db = new DatabaseHandler(this);

		Intent SizeIntent = getIntent();
		StitchAmount = SizeIntent.getDoubleExtra("StitchAmount", 0);
		Size = (String) SizeIntent.getCharSequenceExtra("Size");
		Id = SizeIntent.getIntExtra("Id", 0);
		initializer();

		if (Size.equalsIgnoreCase("x-small")) {
			chestSize.setSelection(2);
			shoulderSize.setSelection(0);
			waistSize.setSelection(10);
			hipSize.setSelection(2);
			sleevesSize.setSelection(0);
			shirtSize.setSelection(20);
			shirtStyle.setSelection(2);
			trouserSize.setSelection(18);
		} else if (Size.equalsIgnoreCase("small")) {
			chestSize.setSelection(6);
			shoulderSize.setSelection(1);
			waistSize.setSelection(16);
			hipSize.setSelection(10);
			sleevesSize.setSelection(0);
			shirtSize.setSelection(22);
			shirtStyle.setSelection(2);
			trouserSize.setSelection(20);
		} else if (Size.equalsIgnoreCase("medium")) {
			chestSize.setSelection(10);
			shoulderSize.setSelection(2);
			waistSize.setSelection(20);
			hipSize.setSelection(14);
			sleevesSize.setSelection(0);
			shirtSize.setSelection(24);
			shirtStyle.setSelection(2);
			trouserSize.setSelection(22);
		} else if (Size.equalsIgnoreCase("large")) {
			chestSize.setSelection(14);
			shoulderSize.setSelection(4);
			waistSize.setSelection(24);
			hipSize.setSelection(18);
			sleevesSize.setSelection(0);
			shirtSize.setSelection(24);
			shirtStyle.setSelection(2);
			trouserSize.setSelection(24);
		} else if (Size.equalsIgnoreCase("x-large")) {
			chestSize.setSelection(20);
			shoulderSize.setSelection(5);
			waistSize.setSelection(28);
			hipSize.setSelection(24);
			sleevesSize.setSelection(0);
			shirtSize.setSelection(24);
			shirtStyle.setSelection(2);
			trouserSize.setSelection(26);
		}

		btnProceedWithItem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Integer.parseInt(heightFeet.getSelectedItem().toString()) > 0) {
					saveDetails();
				} else {
					heightInches.setFocusable(true);
					heightInches.setFocusableInTouchMode(true);
					heightInches.requestFocus();
					Toast.makeText(getBaseContext(), "Please enter height",
							Toast.LENGTH_SHORT).show();
					heightInches.setFocusable(false);
					heightInches.setFocusableInTouchMode(false);
				}
			}
		});
	}

	private void saveDetails() {
		String chest = (String) chestSize.getSelectedItem();
		String shoulder = (String) shoulderSize.getSelectedItem();
		String waist = (String) waistSize.getSelectedItem();
		String hips = (String) hipSize.getSelectedItem();
		String neck = (String) neckSize.getSelectedItem();
		String sleeves = (String) sleevesSize.getSelectedItem();
		String shirt = (String) shirtSize.getSelectedItem();
		String sStyle = (String) shirtStyle.getSelectedItem();
		String trouserButtom = (String) trouserBottomSize.getSelectedItem();
		String trouser = (String) trouserSize.getSelectedItem();
		String heightFt = (String) heightFeet.getSelectedItem();
		String heightInch = (String) heightInches.getSelectedItem();
		String instruction = edtInstruction.getText().toString();

		String StitchingOption = instruction + " this_new_l1n3	BUST CHEST : "
				+ chest + " this_new_l1n3	SHOULDER : " + shoulder
				+ " this_new_l1n3	WAIST : " + waist + " this_new_l1n3	HIP : "
				+ hips + " this_new_l1n3	NECK WIDTH : " + neck
				+ " this_new_l1n3	SLEEVES : " + sleeves
				+ " this_new_l1n3	SHIRT LENGTH : " + shirt
				+ " this_new_l1n3	Trouser BOTTOM : " + trouserButtom
				+ " this_new_l1n3	Trouser LENGTH : " + trouser
				+ " this_new_l1n3	shirt style : " + sStyle +
				// " this_new_l1n3	Trouser style : " + Trouser_style +
				// " this_new_l1n3	As Per Picture : " + As_Per_Picture +
				" this_new_l1n3	Height feet : " + heightFt + "." + heightInch
				+ " product_option	" + Size + "~" + StitchAmount ;
		if (Id > 0) {
			db.updateCartAttribute(Id, StitchingOption);
			db.close();
			Intent shopingCartIntent = new Intent(this, ShoppingCartActivity.class);
			shopingCartIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(shopingCartIntent);
			finish();
		}
	}

	private void initializer() {
		chestSize = (Spinner) findViewById(R.id.spinnerCollectionChest);
		shoulderSize = (Spinner) findViewById(R.id.spinnerCollectionShoulder);
		waistSize = (Spinner) findViewById(R.id.spinnerCollectionWaist);
		hipSize = (Spinner) findViewById(R.id.spinnerCollectionHip);
		neckSize = (Spinner) findViewById(R.id.spinnerCollectionNeck);
		sleevesSize = (Spinner) findViewById(R.id.spinnerCollectionSleeves);
		shirtSize = (Spinner) findViewById(R.id.spinnerCollectionShirt);
		shirtStyle = (Spinner) findViewById(R.id.spinnerCollectionShirtStyle);
		trouserBottomSize = (Spinner) findViewById(R.id.spinnerCollectionTrouserBottom);
		trouserSize = (Spinner) findViewById(R.id.spinnerCollectionTrouser);
		heightFeet = (Spinner) findViewById(R.id.spinnerCollectionHeightFeet);
		heightInches = (Spinner) findViewById(R.id.spinnerCollectionHeightInches);
		edtInstruction = (EditText) findViewById(R.id.edtInstruction);
		btnProceedWithItem = (Button) findViewById(R.id.btnProceedWithItem);
		heightFeet.setPrompt("Please enter height");
	}

	@Override
	protected void onDestroy() {
		saveDetails();
		super.onDestroy();
	}

}
