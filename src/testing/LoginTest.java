package testing;

import junit.framework.Assert;
import junit.framework.TestCase;
import objects.user.User;
import objects.user.UserType;
import database.DataAdapter;

public class LoginTest extends TestCase {
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		DataAdapter.open();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		DataAdapter.close();
	}

	public void testLogin() {
		final User myUser = DataAdapter.login("manager", "manager");
		Assert.assertNotNull(myUser);
		Assert.assertEquals(myUser.getType(), UserType.MANAGER);
		Assert.assertEquals(myUser.getUserName(), "manager");
	}
}
