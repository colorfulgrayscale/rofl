package objects.payment;

public class CreditPayment extends Payments {

	private String cardName;
	private String cardNumber;
	private int expireMonth;
	private int expireYear;
	private CreditCompany creditCompany;

	public String getCardName() {
		return cardName;
	}

	public void setCardName(final String cardName) {
		this.cardName = cardName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(final String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int getExpireMonth() {
		return expireMonth;
	}

	public void setExpireMonth(final int expireMonth) {
		this.expireMonth = expireMonth;
	}

	public int getExpireYear() {
		return expireYear;
	}

	public void setExpireYear(final int expireYear) {
		this.expireYear = expireYear;
	}

	public CreditCompany getCreditCompany() {
		return creditCompany;
	}

	public void setCreditCompany(final CreditCompany creditCompany) {
		this.creditCompany = creditCompany;
	}

}
