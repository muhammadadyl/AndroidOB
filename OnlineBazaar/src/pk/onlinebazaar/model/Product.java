package pk.onlinebazaar.model;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Product implements Serializable {
	int _id;
	String _name;
	String _description;
	String _url;
	String _largeUrl;
	double _price;
	int _stock;
	int _cat;
	int _sub;
	Date _datetime;

	public Product() {

	}

	public Product(int id, String name, String url, double price, int stock,
			int cat, int sub, String largeUrl, String description, long dateTime) {
		this._id = id;
		this._name = name;
		this._url = url;
		this._price = price;
		this._stock = stock;
		this._cat = cat;
		this._sub = sub;
		this._largeUrl = largeUrl;
		this._description = description;
		this._datetime = new Date(dateTime);
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

	public String getDescription() {
		return _description;
	}

	public void setDescription(String description) {
		this._description = description;
	}

	// getting URL
	public String getURL() {
		return this._url;
	}

	// setting URL
	public void setURL(String url) {
		this._url = url;
	}

	public String getLargeUrl() {
		return _largeUrl;
	}

	public void setLargeUrl(String largeUrl) {
		this._largeUrl = largeUrl;
	}

	public double getPrice() {
		return _price;
	}

	public void setPrice(double price) {
		this._price = price;
	}

	public int getStock() {
		return _stock;
	}

	public void setStock(int stock) {
		this._stock = stock;
	}

	public int getCat() {
		return _cat;
	}

	public void setCat(int cat) {
		this._cat = cat;
	}

	public int getSub() {
		return _sub;
	}

	public void setSub(int sub) {
		this._sub = sub;
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
}
