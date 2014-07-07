package org.teknux.dropbitz.service;

import org.teknux.dropbitz.exception.DropBitzException;


public interface IService {

	void start() throws DropBitzException;

	void stop();
}
