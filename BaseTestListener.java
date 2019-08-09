package com.mastercard.atmn.test.pdm.base;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.*;

import com.mastercard.atmn.aon.ta.log.MdcTestController;
import com.mastercard.atmn.aon.ta.metatest.MetaTest;
import com.mastercard.atmn.aon.ta.metatest.MetaTestManager;
import com.mastercard.atmn.aon.ta.metatest.tng.MetaTestManagerTng;

public class BaseTestListener implements ITestListener {

	private static Logger log = LoggerFactory.getLogger(BaseTestListener.class);
	
	// check if processing tracking is required when running suites in parallel?
	// Yes, since 6.10 - Only one listener instance will be used per listener class when multiple suites are run
	// TODO: Remove multiple listener processed tracking
	private static List<Integer> processedTestSuccessFailureSkipped = new ArrayList<Integer>();
	private static List<Integer> processedTestStarts = new ArrayList<Integer>();

	public BaseTestListener() {
		
		super();
		log.trace("Constructor: {}", this.getClass());
	}
	
	@Override
	public void onFinish(ITestContext context) {

		log.trace("TestNG ITestListener onFinish: {}", context.getName());
	}

	@Override
	public void onStart(ITestContext context) {

		log.trace("TestNG ITestListener onStart: {}", context.getName());
	}

	@Override
	public void onTestStart(ITestResult tr) {
	  
	  MetaTest metaTest = MetaTestManagerTng.getInstance().getOrAddByTestResult(tr);
	  MdcTestController.startTestMdcLogging(metaTest.getMdcKey());
      log.trace("TestNG ITestListener onTestStart: {}", tr.getName());
  
      if (!processedTestStarts.contains(tr.hashCode())) {
        processedTestStarts.add(tr.hashCode());
  
        log.info("START: Suite: {}, Test Context: {}, Test Method: {}, Parameters: {}",
            tr.getTestContext().getSuite().getName(),
            tr.getTestContext().getName(),
            tr.getName(),
            tr.getParameters());
      }
	}
	
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult tr) {

	  log.trace("TestNG ITestListener onTestFailedButWithinSuccessPercentage: {}", tr.getName());
		
      if (!processedTestSuccessFailureSkipped.contains(tr.hashCode())) {
        processedTestSuccessFailureSkipped.add(tr.hashCode());
  
        ITestContext context = tr.getTestContext();
  
        log.warn(
            "PARTIAL FAILURE (Within Success percentage): Suite: {}, Test Context: {}, Test Method: {}, Parameters: {}",
            context.getSuite().getName(), context.getName(), tr.getName(), tr.getParameters());

      }
      MetaTestManager.getInstance().unsetCurrentThreadTest();
      MdcTestController.stopTestMdcLogging();
	}

	@Override
	public void onTestFailure(ITestResult tr) {

      log.trace("TestNG ITestListener onTestFailure: {}", tr.getName());
  
      if (!processedTestSuccessFailureSkipped.contains(tr.hashCode())) {
        processedTestSuccessFailureSkipped.add(tr.hashCode());
  
        ITestContext context = tr.getTestContext();
  
        //TODO: try to avoid double logging of throwables
        log.error(
            "FAILED: Suite: {}, Test Context: {}, Test Method: {}, Parameters: {}, ITestResult Throwable: {}",
            context.getSuite().getName(), context.getName(), tr.getName(), tr.getParameters(),
            tr.getThrowable());
      }
      MetaTestManager.getInstance().unsetCurrentThreadTest();
      MdcTestController.stopTestMdcLogging();
	}

	@Override
	public void onTestSkipped(ITestResult tr) {

      log.trace("TestNG ITestListener onTestSkipped: {}", tr.getName());
  
      if (!processedTestSuccessFailureSkipped.contains(tr.hashCode())) {
        processedTestSuccessFailureSkipped.add(tr.hashCode());
  
        ITestContext context = tr.getTestContext();
  
        log.info(
            "SKIPPED: Suite: {}, Test Context: {}, Test Method: {}, Parameters: {}, ITestResult Throwable: {}",
            context.getSuite().getName(), context.getName(), tr.getName(), tr.getParameters(),
            tr.getThrowable());

      }
      MetaTestManager.getInstance().unsetCurrentThreadTest();
      MdcTestController.stopTestMdcLogging();
	}

	@Override
	public void onTestSuccess(ITestResult tr) {
		
      log.trace("TestNG ITestListener onTestSuccess: {}", tr.getName());
  
      if (!processedTestSuccessFailureSkipped.contains(tr.hashCode())) {
        processedTestSuccessFailureSkipped.add(tr.hashCode());
  
        ITestContext context = tr.getTestContext();
  
        log.info("SUCCESS: Suite: {}, Test Context: {}, Test Method: {}, Parameters: {}",
            context.getSuite().getName(), context.getName(), tr.getName(), tr.getParameters());
  
      }
      MetaTestManager.getInstance().unsetCurrentThreadTest();
      MdcTestController.stopTestMdcLogging();
	}
}
