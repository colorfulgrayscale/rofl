package objects.user;

public class User {
	private int id;
	private UserType type;
	private boolean active;
	private String firstName;
	private String lastName;
	private String password;
	private final String userName;
	private boolean isDirty;

	public User(final String aFirstName, final String aLastName,
			final String aUserName, final String aPassword, final UserType aType) {
		id = 0;
		firstName = aFirstName;
		lastName = aLastName;
		userName = aUserName;
		password = aPassword;
		type = aType;
		active = true;
		isDirty = false;
	}

	public User(final int anID, final String aFirstName,
			final String aLastName, final String aUserName,
			final String aPassword, final UserType aUserType) {
		this(aFirstName, aLastName, aUserName, aPassword, aUserType);
		id = anID;
	}

	public int getID() {
		return id;
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(final boolean aValue) {
		isDirty = aValue;
	}

	public UserType getType() {
		return type;
	}

	public void setType(final UserType aType) {
		if (type != aType) {
			isDirty = true;
		}
		type = aType;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean anActive) {
		if (active != anActive) {
			isDirty = true;
		}
		active = anActive;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String aFirstName) {
		if (!firstName.equals(aFirstName)) {
			isDirty = true;
		}
		firstName = aFirstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String aLastName) {
		if (!lastName.equals(aLastName)) {
			isDirty = true;
		}
		lastName = aLastName;
	}

	public void setPassword(final String oldPassword, final String aPassword) {
		if (oldPassword.equals(password)) {
			if (!password.equals(aPassword)) {
				isDirty = true;
			}
			password = aPassword;
		}
	}

	public String getUserName() {
		return userName;
	}
}
