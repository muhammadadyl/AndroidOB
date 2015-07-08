package pk.onlinebazaar.helpers;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pk.onlinebazaar.R;
import pk.onlinebazaar.model.Category;
import pk.onlinebazaar.model.SubCategory;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private Activity context;
	private Map<Category, List<SubCategory>> subCategories;
	private List<Category> categories;

	/*public ExpandableListAdapter(Activity context, List<String> categories,
			Map<String, List<String>> subCategories) {
		this.context = context;
		this.subCategories = subCategories;
		this.categories = categories;
	}*/

	public ExpandableListAdapter(Activity context, List<Category> categories) {
		this.context = context;
		// this.subCategories = subCategories;
		this.categories = new ArrayList<Category>();
		this.subCategories = new LinkedHashMap<Category, List<SubCategory>>();
		for (Category cat : categories) {
			this.categories.add(cat);
			
			List<SubCategory> subcategories = new ArrayList<SubCategory>();
			for (SubCategory sub : cat.getSubcategories())
				subcategories.add(sub);

			this.subCategories.put(cat, subcategories);
		}

	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return subCategories.get(categories.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return subCategories.get(categories.get(groupPosition)).get(childPosition).getID();
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final SubCategory subCategory = (SubCategory) getChild(groupPosition, childPosition);
		LayoutInflater inflater = context.getLayoutInflater();

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.child_item, null);
		}

		TextView item = (TextView) convertView.findViewById(R.id.categories);

		/*ImageView delete = (ImageView) convertView.findViewById(R.id.navigate_to);
		delete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
			}
		});*/
		
		
		item.setText(subCategory.getName());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return subCategories.get(categories.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return categories.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return categories.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return categories.get(groupPosition).getID();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		Category category = (Category) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.group_item, null);
		}
		TextView item = (TextView) convertView.findViewById(R.id.categories);
		item.setTypeface(null, Typeface.BOLD);
		item.setText(category.getName());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
