package pk.onlinebazaar.model;

public class CreditCard {
	private String ExpiryDate;
	private String NameOnCard;
	private String CardNumber;
	private String SecurityCode;
	
	public String getExpiryDate() {
		return ExpiryDate;
	}
	
	public void setExpiryDate(String expiryDate) {
		ExpiryDate = expiryDate;
	}
	
	public String getNameOnCard() {
		return NameOnCard;
	}
	
	public void setNameOnCard(String nameOnCard) {
		NameOnCard = nameOnCard;
	}
	
	public String getCardNumber() {
		return CardNumber;
	}
	
	public void setCardNumber(String cardNumber) {
		CardNumber = cardNumber;
	}
	
	public String getSecurityCode() {
		return SecurityCode;
	}
	
	public void setSecurityCode(String securityCode) {
		SecurityCode = securityCode;
	}
	
}
