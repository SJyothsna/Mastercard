package com.mastercard.atmn.test.pdm.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IExecutionListener;

public class BaseExecutionListener implements IExecutionListener{

	private static Logger logger = LoggerFactory.getLogger(BaseExecutionListener.class);
	
	public BaseExecutionListener() {
		
		super();
	}
	
	@Override
	public void onExecutionStart() {
		
		logger.trace("TestNG IExecutionListener onExecutionStart: {}", this.getClass());
	}
	
	@Override
	public void onExecutionFinish() {
		
		logger.trace("TestNG IExecutionListener onExecutionFinish: {}", this.getClass());
	}

}
