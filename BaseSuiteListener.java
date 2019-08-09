package com.mastercard.atmn.test.pdm.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;

public class BaseSuiteListener implements ISuiteListener{

	private static final Logger logger = LoggerFactory.getLogger(BaseSuiteListener.class);

	public BaseSuiteListener() {
		
		super();
		logger.trace("Constructor: {}", this.getClass());
	}
	
	@Override
	public void onStart(ISuite suite) {
		
		logger.trace("Start Suite [{}], listener instance: {}. TestNG ISuiteListener onStart: {}", suite.getName(), this.hashCode(), this.getClass());
		
		//log run location if using remote TestNG framework
		if(suite.getHost() != null) {
		  logger.info("Suite '{}' running remotely on '{}'.", suite.getName(), suite.getHost());
		}
	}
	
	@Override
	public void onFinish(ISuite suite) {
		
		logger.trace("Finish Suite [{}]. TestNG ISuiteListener onFinish: {}", suite.getName(), this.getClass());
	}
}
