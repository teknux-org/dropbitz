package org.teknux.dropbitz.service;

import java.util.Collection;

import org.teknux.dropbitz.exception.StorageException;
import org.teknux.dropbitz.model.IUser;


/**
 * Manages all aspects of user management
 */
public interface IUserService extends
		AutoCloseable {

	/**
	 * Retrieve the user object for the specified username.
	 *
	 * @param username
	 * @return a user object or <code>null</code> if the user is not found
	 */
	IUser getUser(String username) throws StorageException;

	/**
	 * Updates or create a user object. this method does not allow to rename a user.
	 *
	 * @param user
	 *            the user to update
	 */
	void createUser(IUser user) throws StorageException;

	/**
	 * Updates a user object.
	 *
	 * @param user
	 *            the user to update
	 */
	void updateUser(IUser user) throws StorageException;

	/**
	 * Deletes the user object from storage.
	 *
	 * @param user
	 */
	void deleteUser(IUser user) throws StorageException;

	/**
	 * Delete the user object with the specified username
	 *
	 * @param username
	 */
	void deleteUser(String username) throws StorageException;

	/**
	 * Returns the list of all users available to the login service.
	 *
	 * @return a collection of all users
	 */
	Collection<IUser> getUsers() throws StorageException;

}