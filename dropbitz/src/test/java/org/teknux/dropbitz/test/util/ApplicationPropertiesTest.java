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
