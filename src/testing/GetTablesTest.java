package testing;

import java.awt.Dimension;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import objects.table.Table;
import objects.user.User;
import database.DataAdapter;

public class GetTablesTest extends TestCase {
	private User myUser;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		DataAdapter.open();
		myUser = DataAdapter.login("manager", "manager");
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		DataAdapter.close();
	}

	public void testGetTables() {
		final List<Table> tables = DataAdapter.getTables(myUser, new Dimension(
				100, 100));
		System.out.println(tables.size());
		Assert.assertEquals(false, tables.size() == 0);
	}
}
