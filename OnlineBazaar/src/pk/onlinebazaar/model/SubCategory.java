package pk.onlinebazaar.model;

import java.util.Date;

public class SubCategory {

	int _id;
	String _name;
	int _catId;
	Date _datetime;

	public SubCategory() {
	}

	public SubCategory(int id, String name, long dateTime, int catId) {
		this._id = id;
		this._name = name;
		this._datetime = new Date(dateTime);
		this._catId = catId;
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

	// getting ID
	public int getCatID() {
		return this._catId;
	}

	// setting id
	public void setCatID(int catId) {
		this._catId = catId;
	}

}
