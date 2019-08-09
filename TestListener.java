package com.mastercard.atmn.test.pdm.base;

import java.io.File;
import java.util.Base64;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.mastercard.atmn.aon.ta.extent.ExtentTestManager;
import com.mastercard.atmn.aon.ta.metatest.MetaTest;
import com.mastercard.atmn.aon.ta.metatest.tng.MetaTestManagerTng;
import com.mastercard.atmn.aon.web.AonDriver;
import com.mastercard.atmn.aon.web.AonDriverManager;
import com.mastercard.atmn.aon.web.DriverManager;


public class TestListener extends TestListenerAdapter {

  private static final Logger logger = LoggerFactory.getLogger(TestListener.class);

  public static final String FAIL_SCREENSHOT_TESTNG_RESULT_ATTRIBUTE_KEY = "fail.screenshot";
  
  static {
    
    if(!System.getProperties().containsKey("org.uncommons.reportng.title")) {
      System.setProperty("org.uncommons.reportng.title", "Results");
    }
    if(!System.getProperties().containsKey("org.uncommons.reportng.escape-output")) {
      System.setProperty("org.uncommons.reportng.escape-output", "false");
    }
  }
  
  @Override
  public void onTestFailure(ITestResult result) {

    boolean legacyDriverSet = DriverManager.driverIsSet();
    Set<AonDriverManager> managers = AonDriverManager.getAllInstances();
    
    MetaTest metaTest = MetaTestManagerTng.getInstance().getOrAddByTestResult(result);
    
    int screenshots = 0;
    
    // legacy thread-local driver manager
    if (legacyDriverSet) {

      AonDriver essDriver = DriverManager.getDriver();
      File screenshotFile = takeScreenshot(result, essDriver, 
          FAIL_SCREENSHOT_TESTNG_RESULT_ATTRIBUTE_KEY + "-" + metaTest.getMdcKey() + "(" + screenshots + ")" + "-legacy");

      if(screenshotFile != null) {
        logScreenshot(result, screenshotFile);
        screenshots++;
      }
    } 
    
    // new
    if(managers.size() > 0) {
      
      for(AonDriverManager manager : AonDriverManager.getAllInstances()) {
        
        if(manager != null) {
          
          for(AonDriver essDriver : manager.getAllThreadManagedDrivers()) {
            
            File screenshotFile = takeScreenshot(result, essDriver, 
                FAIL_SCREENSHOT_TESTNG_RESULT_ATTRIBUTE_KEY + "-" + metaTest.getMdcKey() + "(" + screenshots + ")");
            if(screenshotFile != null) {
              logScreenshot(result, screenshotFile);
              screenshots++;
            }
          }
        }
      }
    }
    
    if(screenshots == 0) {
      
      logger.trace("No screenshots taken on test fail (no drivers were active or error taking screenshot).");
      result.setAttribute(FAIL_SCREENSHOT_TESTNG_RESULT_ATTRIBUTE_KEY, "NonUI");
    }

  }
  
  private File takeScreenshot(ITestResult result, AonDriver essDriver, String name) {

    File screenshot = null;

    if (essDriver != null && essDriver.isLoaded()) {

      try {
        
        if(!(essDriver.getWrappedDriver() instanceof TakesScreenshot)) {
          
          logger.debug("Screenshot not possible, WebDriver implementation does not implement TakesScreenshot.");
          return null;
        }
        
        // FIXME: fix with EssDriver saveScreenshot api
        screenshot =
            ((TakesScreenshot) essDriver.getWrappedDriver()).getScreenshotAs(OutputType.FILE);

        // screenshot to test-output

        File switchFile = new File("./test-output/screenshots/" + name + ".png");

        for (int i = 0; switchFile.exists(); i++) {

          switchFile = new File("./test-output/screenshots/" + name + "(" + i + ")" + ".png");
        }

        logger.trace(switchFile.getAbsolutePath());

        FileUtils.copyFile(screenshot, switchFile);

        if (switchFile.exists()) {
          screenshot = switchFile;
        } else {
          screenshot = null;
        }

        logger.trace("Screenshot on test fail: " + screenshot.getAbsolutePath());

      } catch (Exception e) {
        logger.error("Error taking screenshot on test fail.", e);
      }

    } else {
      logger.trace("WebDriver session is inactive (not started or quit): " + essDriver);
    }

    return screenshot;
  }

  private boolean logScreenshot(ITestResult result, File screenshotFile) {

    boolean screenshotLogged = false;

    if (screenshotFile != null) {

      try {

        // log screenshot to extent
        //String base64Data = "data:image/png;base64,"
            //+ Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(screenshotFile));

        ExtentTest extentTest = ExtentTestManager.getInstance()
            .getTestWithKey(MetaTestManagerTng.getInstance().getOrAddByTestResult(result));
        if (extentTest != null) {
          extentTest.info("Screenshot on fail: ",
              MediaEntityBuilder.createScreenCaptureFromPath("screenshots/" + screenshotFile.getName(), "Screenshot").build());
              //MediaEntityBuilder.createScreenCaptureFromBase64String(base64Data).build());
        }

        // attach path for report use
        // FIXME: This is for reportNg, only supports one screenshot being set, 
        // will be replaced by screenshots on meta test for reports
        result.setAttribute(FAIL_SCREENSHOT_TESTNG_RESULT_ATTRIBUTE_KEY,
            "../screenshots/" + screenshotFile.getName());

        screenshotLogged = true;
      } catch (Exception e) {
        logger.error("Error logging screenshot on test fail.", e);
      }

    }

    return screenshotLogged;
  }
}
