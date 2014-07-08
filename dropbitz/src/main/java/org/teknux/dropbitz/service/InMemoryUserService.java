package org.teknux.dropbitz.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.teknux.dropbitz.exception.StorageException;
import org.teknux.dropbitz.model.IUser;


/**
 * In memory user storage/service. This class does not keep any persistent information between application restarts.
 */
public class InMemoryUserService implements
		IUserService {

	private Map<String, IUser> userMap = new HashMap<>();

	@Override
	public IUser getUser(String username) {
		return userMap.get(username);
	}

	@Override
	public void createUser(IUser user) throws StorageException {
		Objects.requireNonNull(user);
		Objects.requireNonNull(user.getEmail());
		if (userMap.containsKey(user.getEmail())) {
			throw new StorageException("User with username " + user.getEmail() + " already exists");
		}
		userMap.put(user.getEmail(), user);
	}

	@Override
	public void updateUser(IUser user) {
		Objects.requireNonNull(user);
		Objects.requireNonNull(user.getEmail());
		userMap.put(user.getEmail(), user);
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

	@Override
	public void close() throws Exception {

	}

}
