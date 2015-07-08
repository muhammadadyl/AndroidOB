package pk.onlinebazaar.model;

public class Profile {
	String _email;
	String _firstName;
	String _lastName;
	String _phone;
	String _mobile;
	String _address;
	String _city;
	String _state;
	String _zip;
	String _country;
	String _token;

	public Profile(String email, String firstName, String lastName,
			String phone, String mobile, String address, String city,
			String state, String zip, String country, String token) {
		_email = email;
		_firstName = firstName;
		_lastName = lastName;
		_phone = phone;
		_mobile = mobile;
		_address = address;
		_city = city;
		_state = state;
		_zip = zip;
		_country = country;
		_token = token;
	}

	public String getEmail() {
		return _email;
	}

	public void setEmail(String email) {
		this._email = email;
	}

	public String getFirstName() {
		return _firstName;
	}

	public void setFirstName(String firstName) {
		this._firstName = firstName;
	}

	public String getLastName() {
		return _lastName;
	}

	public void setLastName(String lastName) {
		this._lastName = lastName;
	}

	public String getPhone() {
		return _phone;
	}

	public void setPhone(String phone) {
		this._phone = phone;
	}

	public String getMobile() {
		return _mobile;
	}

	public void setMobile(String mobile) {
		this._mobile = mobile;
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

	public String getZip() {
		return _zip;
	}

	public void setZip(String zip) {
		this._zip = zip;
	}

	public String getCountry() {
		return _country;
	}

	public void setCountry(String country) {
		this._country = country;
	}

	public String getToken() {
		return _token;
	}

	public void setToken(String token) {
		this._token = token;
	}
	
	

}
