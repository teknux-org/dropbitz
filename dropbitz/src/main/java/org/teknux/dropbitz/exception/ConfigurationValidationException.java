/*
 * Copyright (C) 2014 TekNux.org
 *
 * This file is part of the dropbitz Community GPL Source Code.
 *
 * dropbitz Community Source Code is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * dropbitz Community Source Code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with dropbitz Community Source Code.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.teknux.dropbitz.exception;

public class ConfigurationValidationException extends ConfigurationException {

	private static final long serialVersionUID = 1L;

	public ConfigurationValidationException(String s) {
		super(s);
	}

	public ConfigurationValidationException(Throwable throwable) {
		super(throwable);
	}

	public ConfigurationValidationException(String s, Throwable throwable) {
		super(s, throwable);
	}
}
