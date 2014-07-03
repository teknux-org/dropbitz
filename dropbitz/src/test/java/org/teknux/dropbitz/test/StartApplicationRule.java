package org.teknux.dropbitz.test;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.teknux.dropbitz.Application;
import org.teknux.dropbitz.config.ConfigurationFile;


public class StartApplicationRule implements
		TestRule {

	private Application application;

	@Override
	public Statement apply(Statement base, Description description) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				startIfRequired();
				try {
					base.evaluate();
				} finally {
					if (application.isStarted()) {
						application.stop();
					}
				}
			}
		};
	}

	protected void startIfRequired() {
		if (application == null) {
			application = new Application(false);
		}
	}

	public Application getApplication() {
		return application;
	}

	public ConfigurationFile getConfiguration() {
		return Application.getConfigurationFile();
	}
}
