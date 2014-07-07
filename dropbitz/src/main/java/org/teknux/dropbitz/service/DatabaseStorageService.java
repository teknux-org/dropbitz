package org.teknux.dropbitz.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import org.teknux.dropbitz.exception.StorageException;
import org.teknux.dropbitz.model.IUser;
import org.teknux.dropbitz.model.User;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.Db4oException;


/**
 * Persistent user storage/service.
 */
public class DatabaseStorageService implements
		IUserService {

	private final StorageService storageService;

	public DatabaseStorageService(StorageService storageService) {
		this.storageService = storageService;
	}

	@Override
	public IUser getUser(String username) throws StorageException {
		final User u = new User();
		u.setEmail(username);

		return select(new SelectOperation<IUser>() {

			@Override
			public IUser execute(ObjectContainer container) {
				ObjectSet<User> resultSet = container.queryByExample(u);
				return resultSet.isEmpty() ? null : resultSet.next();
			}
		});
	}

	@Override
	public void createUser(IUser user) throws StorageException {
		Objects.requireNonNull(user);
		Objects.requireNonNull(user.getEmail());

		update(container -> container.store(user));
	}

	@Override
	public void updateUser(IUser user) throws StorageException {
		Objects.requireNonNull(user);
		Objects.requireNonNull(user.getEmail());

		updateUser(user.getEmail(), user);
	}

	@Override
	public void updateUser(String username, IUser user) throws StorageException {
		Objects.requireNonNull(username);
		Objects.requireNonNull(user);

		update(container -> {
			final User example = new User();
			example.setEmail(user.getEmail()); // find by old email
			ObjectSet<User> resultSet = container.queryByExample(example);
			if (resultSet.isEmpty())
				throw new StorageException("User does not exists : " + user);
			final User storedUser = resultSet.next();
			storedUser.setEmail(username); // update email
			storedUser.setName(user.getName());
			storedUser.setPassword(user.getPassword());
			storedUser.setActive(user.isActive());
			storedUser.setAdmin(user.isAdmin());
			container.store(storedUser); // update existing user
		});
	}

	@Override
	public void deleteUser(String username) throws StorageException {
		Objects.requireNonNull(username);

		update(container -> {
			final User example = new User();
			example.setEmail(username); // find the user first
			ObjectSet<User> resultSet = container.queryByExample(example);
			if (resultSet.isEmpty())
				throw new StorageException("User does not exists with username : " + username);
			container.delete(resultSet.next());
		});
	}

	@Override
	public void deleteUser(IUser user) throws StorageException {
		Objects.requireNonNull(user);
		Objects.requireNonNull(user.getEmail());

		deleteUser(user.getEmail());
	}

	@Override
	public Collection<IUser> getUsers() throws StorageException {
		return select(new SelectOperation<Collection<IUser>>() {

			@Override
			public Collection<IUser> execute(ObjectContainer container) throws Db4oException {
				return new ArrayList<>(container.query(User.class));
			}
		});
	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	private void update(final UpdateOperation runnable) throws StorageException {
		final ObjectContainer container = storageService.getObjectContainer();
		try {
			runnable.execute(container);
			container.commit();
		} catch (Exception e) {
			container.rollback();
			throw new StorageException(e);
		} finally {
			container.close();
		}
	}

	private <T> T select(final SelectOperation<T> runnable) throws StorageException {
		final ObjectContainer container = storageService.getObjectContainer();
		try {
			return runnable.execute(container);
		} catch (Exception e) {
			throw new StorageException(e);
		} finally {
			container.close();
		}
	}

	private interface SelectOperation<T> {

		T execute(ObjectContainer container) throws Db4oException, StorageException;
	}

	private interface UpdateOperation {

		void execute(ObjectContainer container) throws Db4oException, StorageException;
	}

}
