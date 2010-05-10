package testing;

import java.awt.Dimension;
import java.awt.Point;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import objects.table.RoundTable;
import objects.table.Table;
import objects.user.User;
import database.DataAdapter;

public class ModifyTablesTest extends TestCase {
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

	public void testModifyTables() {
		final Table myTable = new RoundTable(new Point(100, 100), 5,
				new Dimension(100, 100));
		DataAdapter.insertTable(myUser, myTable);
		List<Table> tables = DataAdapter.getTables(myUser, new Dimension(100,
				100));
		for (final Table table : tables)
			if (table.getId() == myTable.getId()) {
				Assert.assertTrue(myTable.getClass().equals(table.getClass()));
				Assert.assertEquals(myTable.getStatus(), table.getStatus());
			}
		DataAdapter.deleteTable(myUser, myTable);
		tables = DataAdapter.getTables(myUser, new Dimension(100, 100));
		for (final Table table : tables)
			if (table.getId() == myTable.getId()) {
				Assert.assertTrue(false);
			}
	}
}
