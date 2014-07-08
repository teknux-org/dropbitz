package org.teknux.dropbitz.test.service;

import java.util.UUID;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.teknux.dropbitz.exception.DropBitzException;
import org.teknux.dropbitz.exception.StorageException;
import org.teknux.dropbitz.model.IUser;
import org.teknux.dropbitz.model.User;
import org.teknux.dropbitz.service.DatabaseUserService;
import org.teknux.dropbitz.service.IUserService;
import org.teknux.dropbitz.service.StorageService;


public class UserServiceTest {

	@ClassRule
	public static TemporaryFolder testFolder = new TemporaryFolder();

	public static IUserService userService;

	@BeforeClass
	public static void begin() throws DropBitzException {
		final StorageService srv = new StorageService(testFolder.getRoot() + "/database.db");
		srv.start();
		userService = new DatabaseUserService(srv);
	}

	@AfterClass
	public static void end() throws Exception {
		userService.close();
	}

	@Test
	public void testUsernameIsUnique() throws StorageException {
		final String id = UUID.randomUUID().toString();
		final User u = createUser(id);

		userService.createUser(u);
		Assert.assertNotNull(userService.getUser(u.getEmail())); // check exists
		checkUser(u, id);

		try {
			final User duplicate = createUser(id);
			userService.createUser(duplicate);
			Assert.fail("Should throw a StorageException!");
			Assert.assertNull(userService.getUser(duplicate.getEmail())); // check exists

		} catch (StorageException e) {
			// we expect an exception here :)
		} finally {
			userService.deleteUser(u);
		}

	}

	@Test
	public void benchmark() throws StorageException {
		for (int i = 0; i < 100; i++) {
			final User u = createUser(UUID.randomUUID().toString());
			userService.createUser(u);
			userService.getUser(u.getEmail());
			userService.deleteUser(u);
		}
	}

	@Test
	public void testSelect() throws StorageException {
		final User u1 = createUser(UUID.randomUUID().toString());
		final User u2 = createUser(UUID.randomUUID().toString());
		final User u3 = createUser(UUID.randomUUID().toString());

		userService.createUser(u1);
		userService.createUser(u2);
		userService.createUser(u3);

		Assert.assertEquals(3, userService.getUsers().size());

		userService.deleteUser(u1);
		userService.deleteUser(u2);
		userService.deleteUser(u3);

		Assert.assertEquals(0, userService.getUsers().size());
	}

	@Test
	public void testCreate() throws StorageException {
		final String id = UUID.randomUUID().toString();
		final User u = createUser(id);

		userService.createUser(u);
		Assert.assertNotNull(userService.getUser(u.getEmail())); // check exists
		checkUser(u, id);

		userService.deleteUser(u); // delete
		Assert.assertNull(userService.getUser(u.getEmail())); // check exists
	}

	@Test
	public void testUpdate() throws StorageException {
		final String id = UUID.randomUUID().toString();
		final User u = createUser(id);

		userService.createUser(u);
		Assert.assertNotNull(userService.getUser(u.getEmail())); // check exists
		checkUser(u, id);

		// test simple update fields
		boolean active = !u.isActive();
		u.setActive(active);
		u.setName("test");
		userService.updateUser(u); // update

		final IUser updatedUser = userService.getUser(u.getEmail());
		Assert.assertNotNull(updatedUser);
		Assert.assertEquals(updatedUser.getName(), "test");
		Assert.assertEquals(updatedUser.isActive(), active);

		// test rename
		//service.updateUser("New Name", u); // update
		//Assert.assertNotNull(service.getUser("New Name")); // check exists

		// test user was not duplicated
		Assert.assertEquals(1, userService.getUsers().size()); // check exists

		userService.deleteUser(u); // delete
		Assert.assertNull(userService.getUser(u.getEmail())); // check exists
	}

	@Test
	public void testDelete() throws StorageException {
		final String id = UUID.randomUUID().toString();
		final User u = createUser(id);

		userService.createUser(u);
		Assert.assertNotNull(userService.getUser(u.getEmail())); // check exists
		checkUser(u, id);

		userService.deleteUser(u); // delete
		Assert.assertNull(userService.getUser(u.getEmail())); // check exists
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
