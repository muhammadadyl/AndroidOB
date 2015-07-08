package pk.onlinebazaar.model;

import java.util.Date;
import java.util.List;

public class Category {

	int _id;
	String _name;
	Date _datetime;
	List<SubCategory> _subCategories;

	public Category() {

	}

	public Category(int id, String name, long dateTime, List<SubCategory> subCategories) {
		this._id = id;
		this._name = name;
		this._datetime = new Date(dateTime);
		this._subCategories = subCategories;
	}

	public Category(int id, String name) {
		this._id = id;
		this._name = name;
	}

	// getting ID
	public int getID() {
		return this._id;
	}

	// setting id
	public void setID(int id) {
		this._id = id;
	}

	// getting name
	public String getName() {
		return this._name;
	}

	// setting name
	public void setName(String name) {
		this._name = name;
	}

	public Date getDatetime() {
		return _datetime;
	}
	
	public long getDatetimeInLong() {
		return _datetime.getTime();
	}

	public void setDatetime(String datetime) { 
		datetime = datetime.replace("/Date(", "").replace(")/", "");
		long time = Long.parseLong(datetime);  
		this._datetime = new Date(time);
	}
	
	public void setDatetime(long datetime) { 
		this._datetime = new Date(datetime);
	}

	// getting SubCategories
	public List<SubCategory> getSubcategories() {
		return this._subCategories;
	}

	// setting SubCategories
	public void setSubcategories(List<SubCategory> subCategories) {
		this._subCategories = subCategories;
	}

}
