package org.teknux.dropbitz.services;

import java.util.Collection;

import org.teknux.dropbitz.exceptions.StorageException;
import org.teknux.dropbitz.model.IUser;


/**
 * Manages all aspects of user
 */
public interface IUserService extends
		IService {

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
	 * @return <code>true</code> if update is successful
	 */
	void updateUser(IUser user) throws StorageException;

	/**
	 * Updates or create a user object. Allows to rename the user.
	 *
	 * @param user
	 *            the user to update
	 * @return <code>true</code> if update is successful
	 */
	void updateUser(String username, IUser user) throws StorageException;

	/**
	 * Deletes the user object from storage.
	 *
	 * @param user
	 * @return true if successful
	 * @since 0.5.0
	 */
	void deleteUser(IUser user) throws StorageException;

	/**
	 * Delete the user object with the specified username
	 *
	 * @param username
	 * @return true if successful
	 * @since 0.5.0
	 */
	void deleteUser(String username) throws StorageException;

	/**
	 * Returns the list of all users available to the login service.
	 *
	 * @return list of all users
	 * @since 0.8.0
	 */
	Collection<IUser> getUsers() throws StorageException;

}