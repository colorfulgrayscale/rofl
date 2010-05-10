package objects.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
	private int id;
	private int table;
	private Date seatedTime;
	private Date orderTaken;
	private Date orderReady;
	private Date orderDelivered;
	private Date orderPaid;
	private Date customerLeft;
	private final List<OrderItem> items;
	private boolean isDirty;

	public Order() {
		items = new ArrayList<OrderItem>();
		id = 0;
		table = 0;
		seatedTime = new Date(System.currentTimeMillis());
		isDirty = true;
	}

	public String getShortID() {
		String value = String.valueOf(id);
		if (value.length() > 3) {
			value = value.substring(value.length() - 3, value.length());
		}
		while (value.length() < 3) {
			value = "0" + value;
		}
		return value;
	}

	public int getId() {
		return id;
	}

	public void setId(final int anID) {
		id = anID;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(final boolean aValue) {
		isDirty = aValue;
	}

	public Date getSeatedTime() {
		return seatedTime;
	}

	public void setSeatedTime(final Date aSeatedTime) {
		if ((seatedTime == null) || !seatedTime.equals(aSeatedTime)) {
			isDirty = true;
		}
		seatedTime = aSeatedTime;
	}

	public Date getOrderTaken() {
		return orderTaken;
	}

	public void setOrderTaken(final Date anOrderTaken) {
		if ((orderTaken == null) || !orderTaken.equals(anOrderTaken)) {
			isDirty = true;
		}
		orderTaken = anOrderTaken;
	}

	public Date getOrderReady() {
		return orderReady;
	}

	public void setOrderReady(final Date anOrderReady) {
		if ((orderReady == null) || !orderReady.equals(anOrderReady)) {
			isDirty = true;
		}
		orderReady = anOrderReady;
	}

	public Date getOrderDelivered() {
		return orderDelivered;
	}

	public void setOrderDelivered(final Date anOrderDelivered) {
		if ((orderDelivered == null)
				|| !orderDelivered.equals(anOrderDelivered)) {
			isDirty = true;
		}
		orderDelivered = anOrderDelivered;
	}

	public Date getOrderPaid() {
		return orderPaid;
	}

	public void setOrderPaid(final Date anOrderPaid) {
		if ((orderPaid == null) || !orderPaid.equals(anOrderPaid)) {
			isDirty = true;
		}
		orderPaid = anOrderPaid;
	}

	public Date getCustomerLeft() {
		return customerLeft;
	}

	public void setCustomerLeft(final Date aCustomerLeft) {
		if ((customerLeft == null) || !customerLeft.equals(aCustomerLeft)) {
			isDirty = true;
		}
		customerLeft = aCustomerLeft;
	}

	public int getTable() {
		return table;
	}

	public void setTable(final int aTable) {
		table = aTable;
		isDirty = true;
	}

	public List<OrderItem> getItems() {
		return new ArrayList<OrderItem>(items);
	}

	public boolean addOrderItem(final OrderItem anItem) {
		if (items.add(anItem)) {
			isDirty = true;
			return true;
		}
		return false;
	}

	public boolean removeOrderItem(final OrderItem anItem) {
		if (items.remove(anItem)) {
			isDirty = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Order))
			return false;
		if ((((Order) obj).getId() == id)
				&& ((Order) obj).getOrderTaken().equals(orderTaken))
			return true;
		return false;
	}
}
