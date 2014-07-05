package org.teknux.dropbitz.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.teknux.dropbitz.model.IUser;


/**
 * In memory user storage/service.
 */
public class InMemoryUserService implements
		IUserService {

	private Map<String, IUser> userMap = new HashMap<>();

	@Override
	public IUser getUser(String username) {
		return userMap.get(username);
	}

	@Override
	public void updateUser(IUser user) {
		Objects.requireNonNull(user);
		Objects.requireNonNull(user.getEmail());
		updateUser(user.getEmail(), user);
	}

	@Override
	public void updateUser(String username, IUser user) {
		Objects.requireNonNull(username);
		Objects.requireNonNull(user);
		userMap.put(username, user);
	}

	@Override
	public void deleteUser(IUser user) {
		Objects.requireNonNull(user);
		Objects.requireNonNull(user.getEmail());
		deleteUser(user.getEmail());
	}

	@Override
	public void deleteUser(String username) {
		Objects.requireNonNull(username);
		userMap.remove(username);
	}

	@Override
	public Collection<IUser> getUsers() {
		return userMap.values();
	}

}
