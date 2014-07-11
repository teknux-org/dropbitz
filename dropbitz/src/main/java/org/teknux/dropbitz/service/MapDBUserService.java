package org.teknux.dropbitz.service;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.exception.StorageException;
import org.teknux.dropbitz.model.IUser;
import org.teknux.dropbitz.service.StorageService.Storage;


/**
 * In testing..
 */
public class MapDBUserService implements
		IUserService {

	private Map<String, IUser> userMap;

	/**
	 * For testing only
	 */
	public MapDBUserService(StorageService storage) {
		userMap = storage.getStorageMap(Storage.USERS);
	}

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
	public void start(ServiceManager serviceManager) throws ServiceException {
		final StorageService storage = serviceManager.getService(StorageService.class);
		Objects.requireNonNull(storage);
		userMap = storage.getStorageMap(Storage.USERS);
	}

	@Override
	public void stop() throws ServiceException {
		try {
			close();
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void close() throws Exception {
		// TODO we should use transactions make sure to and commit everything here.
	}

}
