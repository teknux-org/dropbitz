package org.teknux.dropbitz.model;

public interface IUser extends
		IDable<String> {

	public static enum Role {
		SuperAdmin, Operator, User;
	}

	public String getEmail();

	public void setEmail(String email);

	public String getName();

	public void setName(String name);

	public String getPassword();

	public void setPassword(String password);

	public boolean isAdmin();

	public void setAdmin(boolean isAdmin);

	public boolean isActive();

	public void setActive(boolean isActive);

	public Role[] getRoles();
}