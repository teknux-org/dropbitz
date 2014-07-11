package org.teknux.dropbitz.test.service;

import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.teknux.dropbitz.exception.ServiceException;
import org.teknux.dropbitz.exception.StorageException;
import org.teknux.dropbitz.model.IUser;
import org.teknux.dropbitz.model.User;
import org.teknux.dropbitz.service.IUserService;
import org.teknux.dropbitz.service.MapDBUserService;
import org.teknux.dropbitz.service.StorageService;


public class UserServiceTest {

	@ClassRule
	public static TemporaryFolder testFolder = new TemporaryFolder();

	public StorageService storageService;
	public IUserService userService;

	@Test
	public void testUsernameIsUniqueOnCreation() throws StorageException {
		final String id = UUID.randomUUID().toString();
		final User u = createUser(id);

		userService.createUser(u);
		Assert.assertNotNull(userService.getUser(u.getEmail())); // check exists
		checkUser(u, id);

		// try to CREATE with a duplicated username
		final User duplicate = createUser(id);
		try {
			userService.createUser(duplicate);
			Assert.fail("Should throw a StorageException!");

		} catch (StorageException e) {
			// we expect this
		} finally {
			userService.deleteUser(u);
		}
	}

	@Test
	public void testUsernameIsUniqueOnUpdate() throws StorageException {
		final String id1 = UUID.randomUUID().toString();
		final String id2 = UUID.randomUUID().toString();
		final User u1 = createUser(id1);
		final User u2 = createUser(id2);

		userService.createUser(u1);
		userService.createUser(u2);
		Assert.assertNotNull(userService.getUser(u1.getEmail())); // check exists
		Assert.assertNotNull(userService.getUser(u2.getEmail())); // check exists

		// try to UPDATE with a duplicated username
		u1.setEmail(id2);
		try {
			userService.updateUser(u1);
			Assert.fail("Should throw a StorageException!");

		} catch (StorageException e) {
			// we expect this
		} finally {
			userService.deleteUser(id1);
			userService.deleteUser(id2);
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

	@Before
	public void before() throws ServiceException {
		storageService = new StorageService(testFolder.getRoot().getPath() + "/" + UUID.randomUUID() + ".db");
		storageService.start(null);
		userService = new MapDBUserService(storageService);
	}

	@After
	public void done() throws Exception {
		if (userService != null) {
			userService.close();
		}
		if (storageService != null) {
			storageService.stop();
		}
	}

}
