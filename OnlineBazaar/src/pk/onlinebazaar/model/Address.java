package pk.onlinebazaar.model;

public class Address {
	
	int _id;
	String _address;
	String _city;
	String _state;
	String _country;
	String _zip;
	String _phone_no;
	int _isShip;
	int _isBill;
	int _isCurrentShip;
	int _isCurrentBill;
	
	public Address(int id, String address, String city, String state, String country, String zip, String phone, int isCurrentShip, int isCurrentBill, int isShip, int isBill){
		_id = id;
		_address = address;
		_city = city;
		_country = country;
		_state = state;
		_zip = zip;
		_phone_no = phone;
		_isCurrentBill = isCurrentBill;
		_isCurrentShip = isCurrentShip;
		_isBill = isBill;
		_isShip = isShip;
	}

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		this._id = id;
	}

	public String getAddress() {
		return _address;
	}

	public void setAddress(String address) {
		this._address = address;
	}

	public String getCity() {
		return _city;
	}

	public void setCity(String city) {
		this._city = city;
	}

	public String getState() {
		return _state;
	}

	public void setState(String state) {
		this._state = state;
	}

	public String getCountry() {
		return _country;
	}

	public void setCountry(String country) {
		this._country = country;
	}

	public String getZip() {
		return _zip;
	}

	public void setZip(String zip) {
		this._zip = zip;
	}

	public String getPhoneNo() {
		return _phone_no;
	}

	public void setNhoneNo(String phoneNo) {
		this._phone_no = phoneNo;
	}

	public int isShip() {
		return _isShip;
	}

	public void setIsShip(int isShip) {
		this._isShip = isShip;
	}

	public int isBill() {
		return _isBill;
	}

	public void setIsBill(int isBill) {
		this._isBill = isBill;
	}

	public int isCurrentShip() {
		return _isCurrentShip;
	}

	public void setIsCurrentShip(int isCurrentShip) {
		this._isCurrentShip = isCurrentShip;
	}

	public int isCurrentBill() {
		return _isCurrentBill;
	}

	public void setIsCurrentBill(int isCurrentBill) {
		this._isCurrentBill = isCurrentBill;
	}

}
