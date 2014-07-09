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
 * Persistent user storage/service. This service is not thread safe and consumes resource that need to be freed.
 * 
 * @see AutoCloseable
 */
public class DatabaseUserService implements
		IUserService {

	private final ObjectContainer container;

	public DatabaseUserService(StorageService storageService) {
		Objects.requireNonNull(storageService);
		container = storageService.getObjectContainer();
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

		if (getUser(user.getEmail()) != null) {
			throw new StorageException("User with username " + user.getEmail() + " already exists");
		}
		// creates/stores the user
		update(container -> container.store(user));
	}

	@Override
	public void updateUser(IUser user) throws StorageException {
		Objects.requireNonNull(user);
		Objects.requireNonNull(user.getEmail());

		if (!container.ext().isActive(user)) {
			throw new IllegalArgumentException("Object is not managed by this service, please create or fetch the object first. Object : " + user);
		}
		// check if the username has changed
		IUser previousUser = getUser(user.getEmail());
		if (previousUser != null && previousUser != user) { // found an object with a different MEMORY ADDRESS
			throw new StorageException("User with username " + user.getEmail() + " already exists");
		}
		// update the user in db
		update(container -> container.store(user));
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

	private void update(final UpdateOperation runnable) throws StorageException {
		try {
			runnable.execute(container);
			container.commit(); // commit
		} catch (Exception e) {
			container.rollback(); // rollback the transaction
			throw new StorageException(e);
		}
	}

	private <T> T select(final SelectOperation<T> runnable) throws StorageException {
		try {
			return runnable.execute(container);
		} catch (Exception e) {
			throw new StorageException(e);
		}
	}

	private interface SelectOperation<T> {

		T execute(ObjectContainer container) throws Db4oException, StorageException;
	}

	private interface UpdateOperation {

		void execute(ObjectContainer container) throws Db4oException, StorageException;
	}

	@Override
	public void close() throws Exception {
		if (container != null) {
			container.close();
		}
	}

}
