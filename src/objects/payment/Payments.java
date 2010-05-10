package objects.payment;

public class Payments {

	private int id;
	private double payment;
	private double tip;

	public void setId(final int id) {
		this.id = id;
	}

	public void setPayment(final double payment) {
		this.payment = payment;
	}

	public void setTip(final double tip) {
		this.tip = tip;
	}

	public int getId() {
		return id;
	}

	public double getPayment() {
		return payment;
	}

	public double getTip() {
		return tip;
	}

}
