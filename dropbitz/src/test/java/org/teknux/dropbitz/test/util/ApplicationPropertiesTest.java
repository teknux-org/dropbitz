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

package org.teknux.dropbitz.test.util;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.teknux.dropbitz.exception.DropBitzException;
import org.teknux.dropbitz.util.ApplicationProperties;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationPropertiesTest {
    @Test(expected = DropBitzException.class)
    public void test01badPropertiesFile() throws DropBitzException {
        new ApplicationProperties("/bad-application.properties");
    }

    @Test
    public void test02propertiesFile() throws DropBitzException {
        ApplicationProperties applicationProperties = new ApplicationProperties("/application.properties");
        Assert.assertEquals("nameValue", applicationProperties.getProperty(ApplicationProperties.APPLICATION_NAME_KEY));
        Assert.assertEquals("versionValue", applicationProperties.getProperty(ApplicationProperties.APPLICATION_VERSION_KEY));
    }

    @Test
    public void test03propertiesFileStatic() throws DropBitzException {
        ApplicationProperties applicationProperties = ApplicationProperties.getInstance();
        Assert.assertEquals("nameValue", applicationProperties.getProperty(ApplicationProperties.APPLICATION_NAME_KEY));
        Assert.assertEquals("versionValue", applicationProperties.getProperty(ApplicationProperties.APPLICATION_VERSION_KEY));
    }
}
