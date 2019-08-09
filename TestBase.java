package com.mastercard.atmn.test.pdm;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import com.aventstack.extentreports.ExtentReports;
import com.mastercard.atmn.aon.config.DynamicConfigBuilder;
import com.mastercard.atmn.aon.ta.extent.ExtentReportsManager;
import com.mastercard.atmn.platform.pdm.db.PdmDbConfigurationBuilder;
import com.mastercard.atmn.platform.pdm.db.PdmSchema;

public class TestBase {

  private static final Logger logger = LoggerFactory.getLogger(TestBase.class);

  protected static boolean buildInfo = false;

  // FIXME: Remove, where did this come from? Test data dir is context configuration
  @Deprecated
  public static File testDataDir;

  protected Map<String, String> contextParameters;

  public TestBase() {

    logger.trace("Constructor: {}", this.getClass());
  }

  @BeforeSuite(alwaysRun = true)
  protected void beforeSuite(ITestContext context) {

    logger.trace("BeforeSuite: {}", context.getSuite().getName());
    this.initContextParameters(context);
  }

  @BeforeTest(alwaysRun = true)
  protected void beforeTest(ITestContext context) {

    logger.trace("BeforeTest: {}", context.getName());
    this.initContextParameters(context);
    fetchEibBuildInfo(context, contextParameters);
  }


  @BeforeGroups() // alwaysRun does not apply to BeforeGroups
  protected void beforeGroups(ITestContext context) {

    logger.trace("BeforeGroups");
  }

  @BeforeClass(alwaysRun = true)
  protected void beforeClass(ITestContext context) {

    logger.trace("BeforeClass: {}", this.getClass());
    initContextParameters(context);
  }

  /**
   * 
   * @deprecated use {@link #beforeMethod(ITestContext, Method, Object[])}
   * @param context
   * @param method
   */
  @BeforeMethod(alwaysRun = true)
  protected void beforeMethod(ITestContext context, Method method) {

    logger.trace("BeforeMethod: {}", method.getName());
  }

  @BeforeMethod(alwaysRun = true)
  protected void beforeMethod(ITestContext context, Method method, Object[] parameters) {

    logger.trace("BeforeMethod: {}", method.getName());
  }

  @AfterMethod(alwaysRun = true)
  protected void afterMethod(ITestContext context, ITestResult result, Method method) {

    logger.trace("AfterMethod: {}, Result: {}", method.getName(),
        (result.isSuccess() ? "Success" : (result.getStatus() == ITestResult.FAILURE ? "Fail" : "Skipped")));
  }

  @AfterClass(alwaysRun = true)
  protected void afterClass(ITestContext context) {

    logger.trace("AfterClass: {}", this.getClass());
  }

  @AfterGroups(alwaysRun = true)
  protected void afterGroups(ITestContext context) {

    logger.trace("AfterGroups");
  }

  @AfterTest(alwaysRun = true)
  protected void afterTest(ITestContext context) {

    logger.trace("AfterTest: {}", context.getName());
  }

  @AfterSuite(alwaysRun = true)
  protected void afterSuite(ITestContext context) {

    logger.trace("AfterSuite: {}", context.getSuite().getName());
  }

  private synchronized void initContextParameters(ITestContext context) {

    if (contextParameters == null) {
      Map<String, String> contextParameters = new HashMap<String, String>();
      contextParameters.putAll(context.getCurrentXmlTest().getAllParameters());
      contextParameters.put(DynamicConfigBuilder.PARAMETER_MAP_ORIGIN_KEY,
          "TestContext [" + context.getName()
              + "] of Suite [" + context.getSuite().getXmlSuite().getFileName() + "]");
      this.contextParameters = contextParameters;
    }
  }

  private static synchronized void fetchEibBuildInfo(ITestContext context, Map<String, String> contextParameters) {

    // TODO this needs to happen somewhere else

    if (!buildInfo) {
      EibBuildInfo bld = null;

      try {
        bld = new EibBuildInfo(
            new PdmDbConfigurationBuilder()
                .withContextParameters(contextParameters)
                .build(PdmSchema.RDR).getHostname(), 2222);
        
        bld.uploadBuildInfoScripts();
        bld.uploadBuildSysInfoScripts();

        Map<String, String> buildInfoMap = bld.getBuildInfo();
        Map<String, String> sysInfoMap = bld.getSysInfo();

        ExtentReports reports = ExtentReportsManager.getInstance()
            .getExtentReports(context.getSuite().getName());

        buildInfoMap.forEach((k, v) -> {
          reports.setSystemInfo(k, v);
        });

        sysInfoMap.forEach((k, v) -> {
          reports.setSystemInfo(k, v);
        });

        buildInfo = true;

      } catch (Exception e) {
        logger.warn("Error fetching build info, message: {}", e.getMessage() );
      } finally {
        if (bld != null) {
          bld.close();
        }
      }
    }
  }
}
