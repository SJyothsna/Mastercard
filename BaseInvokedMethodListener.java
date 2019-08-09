package com.mastercard.atmn.test.pdm.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener2;
import org.testng.ITestContext;
import org.testng.ITestResult;

public class BaseInvokedMethodListener implements IInvokedMethodListener2 {

  private static Logger logger =  LoggerFactory.getLogger(BaseInvokedMethodListener.class);
  
  @Override
  public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    
    logger.trace("beforeInvocation");
  }

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
    
    logger.trace("afterInvocation");
  }

  @Override
  public void beforeInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
    
    logger.trace("beforeInvocation(IInvokedMethod, ITestResult, ITestContext)");
  }

  @Override
  public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
    
    logger.trace("afterInvocation(IInvokedMethod, ITestResult, ITestContext)");
  }

}
