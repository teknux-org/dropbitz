package org.teknux.dropbitz.test.service;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;


public class StorageServiceStartRule implements
		TestRule {

	@Override
	public Statement apply(Statement base, Description description) {
		return new Statement() {
			
			@Override
			public void evaluate() throws Throwable {
				base.evaluate();
			}
		};
	}

}
