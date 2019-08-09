package com.mastercard.atmn.test.pdm.base.reportng;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.reportng.HTMLReporter;

public class EssReportNgHtmlReporter extends HTMLReporter {
    
    private static final Logger logger = LoggerFactory.getLogger(EssReportNgHtmlReporter.class);
  
    protected static final EssReportNgUtils SS_UTILS = new EssReportNgUtils();

    // Must match private key specified in org.uncommons.reportng.AbstractReporter
    private static final String UTILS_KEY ="utils";
    
    @Override
	protected VelocityContext createContext()
    {   
      
        VelocityContext context = super.createContext();
        context.put(UTILS_KEY, SS_UTILS);
        return context;
    }
}
