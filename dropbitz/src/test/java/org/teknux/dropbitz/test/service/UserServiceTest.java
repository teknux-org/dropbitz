package org.teknux.dropbitz.test.service;

import java.io.IOException;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.teknux.dropbitz.exception.StorageException;
import org.teknux.dropbitz.model.IUser;
import org.teknux.dropbitz.model.User;
import org.teknux.dropbitz.service.DatabaseStorageService;
import org.teknux.dropbitz.service.IUserService;
import org.teknux.dropbitz.service.InMemoryUserService;
import org.teknux.dropbitz.service.StorageService;


public class UserServiceTest {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@Test
	public void testInMemory() throws StorageException {
		runTests(new InMemoryUserService());
	}

	@Test
	public void testDatabase() throws IOException, StorageException {
		final StorageService srv = new StorageService(testFolder.getRoot() + "/database.db");
		srv.start(); // start the service
		final IUserService userService = new DatabaseStorageService(srv);

		runTests(userService);

		srv.stop();
	}

	private void runTests(final IUserService service) throws StorageException {
		testSelect(service);
		testCreate(service);
		testUpdate(service);
		testDelete(service);
		testUsernameIsUnique(service);
	}

	private void testUsernameIsUnique(final IUserService service) throws StorageException {
		// TODO check uniqueness of username
	}

	private void testSelect(final IUserService service) throws StorageException {
		final User u1 = createUser(UUID.randomUUID().toString());
		final User u2 = createUser(UUID.randomUUID().toString());
		final User u3 = createUser(UUID.randomUUID().toString());

		service.createUser(u1);
		service.createUser(u2);
		service.createUser(u3);

		Assert.assertEquals(3, service.getUsers().size());

		service.deleteUser(u1);
		service.deleteUser(u2);
		service.deleteUser(u3);

		Assert.assertEquals(0, service.getUsers().size());
	}

	private void testCreate(final IUserService service) throws StorageException {
		final String id = UUID.randomUUID().toString();
		final User u = createUser(id);

		service.createUser(u);
		Assert.assertNotNull(service.getUser(u.getEmail())); // check exists
		checkUser(u, id);

		service.deleteUser(u); // delete
		Assert.assertNull(service.getUser(u.getEmail())); // check exists
	}

	private void testUpdate(final IUserService service) throws StorageException {
		final String id = UUID.randomUUID().toString();
		final User u = createUser(id);

		service.createUser(u);
		Assert.assertNotNull(service.getUser(u.getEmail())); // check exists
		checkUser(u, id);

		// test simple update fields
		boolean active = !u.isActive();
		u.setActive(active);
		u.setName("test");
		service.updateUser(u); // update

		final IUser updatedUser = service.getUser(u.getEmail());
		Assert.assertNotNull(updatedUser);
		Assert.assertEquals(updatedUser.getName(), "test");
		Assert.assertEquals(updatedUser.isActive(), active);

		// test rename
		//service.updateUser("New Name", u); // update
		//Assert.assertNotNull(service.getUser("New Name")); // check exists

		// test user was not duplicated
		Assert.assertEquals(1, service.getUsers().size()); // check exists

		service.deleteUser(u); // delete
		Assert.assertNull(service.getUser(u.getEmail())); // check exists
	}

	private void testDelete(final IUserService service) throws StorageException {
		final String id = UUID.randomUUID().toString();
		final User u = createUser(id);

		service.createUser(u);
		Assert.assertNotNull(service.getUser(u.getEmail())); // check exists
		checkUser(u, id);

		service.deleteUser(u); // delete
		Assert.assertNull(service.getUser(u.getEmail())); // check exists
	}

	private User createUser(final String username) {
		final User u = new User();
		u.setActive(true);
		u.setAdmin(true);
		u.setEmail(username);
		u.setName("lolo is awesome");
		u.setPassword("password");

		return u;
	}

	private void checkUser(final User user, final String username) {
		Assert.assertEquals(user.getEmail(), username);
		Assert.assertEquals(user.getName(), "lolo is awesome");
		Assert.assertEquals(user.getPassword(), "password");
		Assert.assertEquals(user.isActive(), true);
		Assert.assertEquals(user.isAdmin(), true);
	}
}
