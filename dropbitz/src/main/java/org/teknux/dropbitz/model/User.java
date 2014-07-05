package org.teknux.dropbitz.model;

public class User extends Timestampable implements
		IUser {

	private static final long serialVersionUID = 1L;

	private String email;

	private String name;

	private String password;

	private boolean isAdmin;

	private boolean isActive;

	@Override
	public String toString() {
		return "User [email=" + email + ", name=" + name + ", isAdmin=" + isAdmin + ", isActive=" + isActive + "]";
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean isAdmin() {
		return isAdmin;
	}

	@Override
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public Role[] getRoles() {
		if (isAdmin) {
			return new Role[] { Role.SuperAdmin, Role.User };
		}
		return new Role[] { Role.User };
	}

}
