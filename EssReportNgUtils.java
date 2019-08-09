package com.mastercard.atmn.test.pdm.base.reportng;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.uncommons.reportng.ReportNGUtils;

import com.mastercard.atmn.aon.ta.log.MdcTestController;
import com.mastercard.atmn.aon.ta.metatest.tng.MetaTestManagerTng;
import com.mastercard.atmn.aon.web.BrowserType;
import com.mastercard.atmn.test.pdm.base.TestListener;

public class EssReportNgUtils extends ReportNGUtils {

	private static final String CSS = "<style type=\"text/css\">\n" +
	        "    .fullRowTestOutputDiv {\n" +
	        "        display: block;\n" +
	        "        width: 100%;\n" +
	        "        float: left;\n" +
	        "     }\n" +
	        "    .logDiv {\n" +
	        "        padding-bottom: 1em;\n" +
	        "        font-weight: bold;\n" +
	        "     }\n" +
	        "    .descriptionDiv {\n" +
	        "        padding-bottom: 1em;\n" +
	        "        font-color: black;\n" +
	        "     }\n" +
	        "     .screenshotDiv img {\n" +
	        "        display: block;\n" +
	        "        float: left;\n" +
	        "        min-width:150px;\n" +
	        "        max-width: 300px;\n" +
	        "        min-height: 150px;\n" +
	        "        max-height: 300px;\n" +
	        "        width: 25%;\n" +
	        "        height: auto;\n" +
	        "        cursor: pointer;\n" +
	        "        border: none;\n" +
	        "     }\n" +
	        "</style>\n" +
	        "\n";
	private static final String JAVASCRIPT = "<script type=\"text/javascript\">\n" +
	        "    function clickImageToggle(event, element)\n" +
	        "    {\n" +
	        "        if(element.style.width==\"auto\"){\n" +
	        "            element.style.maxHeight=\"300px\";\n" +
	        "            element.style.maxWidth=\"300px\";\n" +
	        "            element.style.width=\"25%\";\n" +
	        "            if(element.style.cursor==\"zoom-out\"){\n" +
	        "                element.style.cursor=\"zoom-in\";\n" +
	        "            }\n" +
	        "        }else{\n" +
	        "            element.style.maxHeight=\"100%\";\n" +
	        "            element.style.maxWidth=\"100%\";\n" +
	        "            element.style.width=\"auto\";\n" +
	        "            if(element.style.cursor==\"zoom-in\"){\n" +
	        "                element.style.cursor=\"zoom-out\";\n" +
	        "            }\n" +
	        "        };\n" +
	        "    }\n" +
	        "</script>\n" +
	        "\n";
	
	private static final String PNG_GOOGLE_CHROME = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAYAAAAf8/9hAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA3dJREFUeNpMk0lsGwUUhr/xjLc4dhY7Js7SEGdVmlihJZG6sJSUqlERggipHBrEAYlDkJDgxJUzEhKoKjd6CRKEHVQhSpsmSgqkJFBEKElMszi7Ezu2ZzyeGc8MLqgV//39+p7e+wTbtrmXAprPYxtj+mriZD6+QH5tFSMv4/KV46mN4OxowxftmRGQTvO/CPcKitjVhnnw7fJnnx8zv/kBc3wKdXcXV7GIJUrg91OsDxM8N0jLyMi21hi9VAZv/1dg2CFF3L76ye/jvenLV3jhyo8ISwm07hYqfCJiQSAfXycrp9EQsY8fJTr2KYG62jMOnFcF3cjMfrz59ZG1Em7TbzJnLo6jPOTju8pyZKGIOy9xKlRJ58QtlNs3OcDCO/Iygfc/2JBwHBaff+PsO+/NjbqlEpPDV0WFt55LaYFfO4dJRZ/lF9r5SdJoqlLR13fwlgp9d5cwn3kq4Ag3SWLri61vTSZn3aqQobG+nZ+XkkyaXYRjA/S3+fFEmlmQnOyZ89ScauVao5M/8iJZ1zaH+h4vkxbW4yhalqQoYOpZ1IKOmrfYT2vsVljkZCekM4hKgcHXHZzQF/lztpr9O7OkEzNIy6qMpuVwym7+qkhwuPkQvunrrPkjrMhH0ItzOG+M8XDMwGGtUpndpy8qs1Lewt7uClLYFplXNRxphQ3zNl1Hm3hiM8TUxEXy0+04xFUGWkJcuBChWLiBw4BUxiqRls5fDCE1+etQEzaWncFy6XyVuMbTp05w2tFEs+qns7ufR4/JeLXvMXNbaLqHglmFZjUiiUGkgY5zfHHnOivKFmbGQNqSWT7Y480nI/THEqCnMJUp7NKaDstFRgmi2BGyai91kRocJxuOjw51nqVCrCL9t8xjSivvPucrXWCaojyJeTAN+RyCLpLLglqsIa1WYYt9BGqbkTwu72uvxF7ybcrK8JQ5gz+gUheaAWMZSUmBJZRwqzF0DdVu5ECPsZ1soz3WQ6i27KN/XcCwxLnFWx9+uTQ+PL+zxCMBhaHeHTpc8dIr+8lZDewb1ewlo+wkOwg2dOs9/V2v+sq8l4X7NqIgLq4tjo5vzJ2/u7NMsKRYX1Si0p1HzbrJFWoxzRDhUEeqq7tzqMwnTDyw8UF0xPReanRrK3s+WdjEsE08dgC/VJLK7yUYDsRDwfJBp+iN3x/5R4ABAM8InXSShzr2AAAAAElFTkSuQmCC";
	private static final String PNG_FIREFOX = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAACplBMVEX///8AAAAIMXsKLnQtGC5OUTajfGAoFi8hQmsVNHMIMXvvszaieEMbCyzEk29sXihGOSsKAisTCiwfETPEjFlBKy5tS0M/PWWpdDR+TjbpoDhrQzO4iz5gIyNADSfHo4oZBCgnJDAAAABIDxwGBCIKDzUMEzcAAAAAAAAREjgVGDUAAAAAAAAAAAAGF0EIGD4AAAAAAAAAAAACChoGGEENGEEOGEIGGUYEFjcAAAAAAAAAAAAAAAAAAAAAAAACChgAAAAAAAAAAADscAuZcT/+vjcDeL3/vzfwpEL1oz7tkCnvjDL7oVmPb1sGa60Cf8QHgsYXh8cqkcwhhMDIljj0zKD2tFfyjy3ulC3piCjRZihPREcWUHr7vjv3tlwCfcIGfsMQgsQMXZW0gTX/wjnuqmTvy67uehzsiiPffSvUayx+UTQeXIixXyJ8UzkRVIUDfMADeb8ZSGv9pzT/y0GkiDyIjp7hWg/sfBXriTLljkjqhjngfS6Qtc5DRlMEcLQ/R0v/yUD/1kiRyOa1LwvpZwvvhxzxjTTrgzCYSSJHLzcJRXgDdrsFb7JMPkXXiDL/4m39z0iNxuaCv+KZiGDUOgYCe8Dxji7vfhPXdSZQQ0EZSHAjQ2R9RzP+jCz/2l7/zWKAVSpbYnyYiISJBwjQMQDdaRHuhxjugxf0dyLxcSHvbR3xdB/0oBz7vjzIby05j8JejbxIeK04UXeEAwrJDwDSMwLeWA3rbxjsdRjwdR7xeh35mBuzZCb6wXP2t1PxuYJvgqF8Wzw2DCeMAgi1AAC8AgDDGAHKPAWyQxJRKCZTcIxps9twuN/ncg5zueBotN2OjHH/wTgXFj4YFj3prDb+vzn2tV3zsVDwnjTtmUbqpHLqpXZCVWokkMwxmNFHotVQp9df30DrAAAAQ3RSTlMAAplkK3TNG4ShEv7lQey5jAowEe6VjRHnsvy18ciq50yyA+4tb8MEC8jgFwEVsNAlBRNIxfT53ngeBggcMT5MNyMNedkCrgAAAR5JREFUeF4lxtNyQwEUAMCTpLRt2zbC0rZt27Zt27ZtG3/SO9N9WgAySgAaDBpNRc7IxAwICkFO6r7+3dm5edonOnoAYHB08gpKysyvq2/Ae/KxACvb1vZO4N75xeX1zS3+jh04nl9e394/Pr++f35nHolcwG1KMjO3sLSytrG1s3cg8gCvs4urm7sHocnbx9fPPwAH/MEhoWHhEZFR0TGxcfEJiSCQnJKalp6BxWYRsnNy83AgVFBYVFxSWlZeUVlVXVMrDCKijVfNLa1t7R2dXd09vWIA4gODQ8Mjo2PjE5NT0xKSAFLSC4tLyyura+sbmzKyACAnr7B/cHh0fHJ6pqikDAiUiqqa+v2DhqaWtg7809XTNzA0MjZBIf8DDJlkrm0S7o8AAAAASUVORK5CYII=";
	private static final String PNG_INTERNET_EXPLORER = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAACT1BMVEUAAABjrdsztv/6/PMAQYgAUaQFhv/1rD4yV1cAofh31v+hgjoCp//ztRD90jkaqPH79scATK0AnuTw04pbjK3i6vQBdLURsP89lPGff0cscp8Utv9PXXGpnXdeOwtEV3MAoO8AouxKW28ATpxJV2u/19UDOnEwuP8BPn0AdLm+w8tJY3Tw1Y7/tySmwcH/qTXp7vWT2MIvw/8dv/8vp/4AnPkOdv8Al/YATZV71P/9+50CSYcxaJ5As/5TsPz8wDbZpGHIrmb6/O8PYdzi5OlycERYiahoYUf22IE8wf8AWJtawv77+91n0v+hlCAATo9Xy/x0e4cAIlwoneU9m/K/qibN0NHe4Hz911UFr/8ArvZOyP/+wEVrz//AnBP7vBXU3HDDxHOjjE37yn7p380Ls//7zDYAld40dbb7/NY4ca/4zkdbzf4Ak9pcy//m6/M1SEVMNiTe186eez0AoPwDj/YAmN7/100gVH5TodUXrf8rpuUAkuZzgmc/teX/1UNzjYb99JKVhVn7wyb69eXsp0X/tzL+3WYfX4yNiW5iclT83ra4xX3nrQvQ1d0AP4YAQo+Fp8D8+7v/3HXHups/YnQBbK3S2+P7zkQAUpv7/NNHaXopuvjGq14AkNdIcZu6zYGFgFD7/M7g4+fo6+ORnnSFo7mFh4NBhLKLnanv8veso1QXlvg/lc4AaKjpvEQqt//1xzgAeMMAluzZ4ekKnf/K33f69NkAofIAhc9ezv4AMGMDQIHBl0AAPYLxw0wpNC+PhDgzt/9w0v+jzeXuc8xTAAAAAXRSTlMAQObYZgAAAQBJREFUeF5FTsOORAEA69hc27Zt27Zt27Zt2zY+bPMyyWxPbdO0xT8eAj8uZHol/Leppf2+5GSrO9uUkHKk72ermQa/tg3ACwBI5PLNjriAzjB2obkakVdIyeSHVx3EuJWGFOsBOFtNyE9Xr8bRqW1kbBRQcTi6OPbJNFmZ87F3V1QFdvcSMypfHOxqWTetgjpdYDqr5yt6KVhEo+lMUt+1QXG+4ulHnO9I/4V2mQ1PGK0rjx9P0Q3uuBpYHrBwdXyqT9YsSwt6bH71xbZHjaXhtdjG6cc66dLFGCjqV0J8o7yWSl7B/OACAO9Uz9mRHM6QrP/+LQhAyOh7W8uVjvwBmBRMNfnvVUgAAAAASUVORK5CYII=";

	private static final Logger logger = LoggerFactory.getLogger(EssReportNgUtils.class);
	
	/**
	 * 
	 * Add a screenshot to the report if a failed test has been added to the attributes
	 * 
	 */	
    @Override
    public List<String> getTestOutput(ITestResult result)
    {
        
    	List<String> output = super.getTestOutput(result);    	
    	
    	System.setProperty("org.uncommons.reportng.escape-output", "false");
    	
    	// add all custom script and style
		output.add(JAVASCRIPT);
    	output.add(CSS);
    	
    	// add the log link
    	addLogLink(result, output);
        
    	// add description
    	addDescription(result, output);

    	// check if we've set the key for the screenshot in the result object
    	String key = TestListener.FAIL_SCREENSHOT_TESTNG_RESULT_ATTRIBUTE_KEY;
    	Object value = result.getAttribute(key);
    	
    	logger.trace("Attribute key-value for screenshot... Key: [{}], Value: [{}]", key, value);
    	// log screenshot
    	if (value != null && !value.toString().equalsIgnoreCase("NonUI")){

	    	addBrowserIconAndScreenshot(result, output, value);		        
    	}
    	
    	return output;
    }

	private void addBrowserIconAndScreenshot(ITestResult result, List<String> output, Object value) {
		
		//FIXME: browser type and version are no longer set by the listener
		Object browser = result.getAttribute("browser-type");
		String version = String.valueOf(result.getAttribute("browser-version"));
		
		String browserIcon = "";
		if(browser == null || !(browser instanceof BrowserType)) {
		  
		  browser = "<unknown browser>";
		} else {
		  
		    BrowserType browserType = (BrowserType) browser;
			if (browserType.equals(BrowserType.FIREFOX)){
				browserIcon = PNG_FIREFOX;
				browser = "Firefox";
			}else if (browserType.equals(BrowserType.INTERNET_EXPLORER)){
				browserIcon = PNG_INTERNET_EXPLORER;
				browser = "Internet Explorer";
			}else if (browserType.equals(BrowserType.GOOGLE_CHROME)){
				browserIcon = PNG_GOOGLE_CHROME;
				browser = "Google Chrome";
			}else if (browserType.equals(BrowserType.HTML_UNIT)) {
			  browser = "HTML Unit";
			}
		}
		
		if(version == null || version.equalsIgnoreCase("null")) {
		  version = "<unknown version>";
		}

		logger.trace("Browser type set on result: {}",  browser);
		
		output.add(
				" 	<img src=\"" + browserIcon + "\" title=\""+ browser + " : " + version+"\"/>\n"
				);
		
		String imageDivId = "imgDivId" + result.getMethod().getMethodName();
		String imageId = "imageId" + result.getMethod().getMethodName();
		String imgSrc = value.toString();
		
		output.add(
		        "    <div id=\""+imageDivId+"\" class=\"screenshotDiv fullRowTestOutputDiv\">\n" +
		        "        <img id=\""+imageId+"\" src=\"" + imgSrc + "\" title=\"click to enlarge/shrink\" style=\"cursor:zoom-in;\" onclick=\"clickImageToggle(event, this);\" alt=\"*missing screenshot*\"/>\n" +
		        "    </div>\n"
		        );
	}

	private void addDescription(ITestResult result, List<String> output) {
		String description;
    	if (result.getMethod().isTest()) {
    	  description = MetaTestManagerTng.getInstance().getOrAddByTestResult(result).getReportTestDescription();
    	} else {
    	  description = result.getMethod().getDescription();
    	}
    	String descDivId = "descDivId" + result.getMethod().getMethodName();
    	String desc = 
    			"<div id=\"" + descDivId + "\" class=\"descriptionDiv fullRowTestOutputDiv\">\n" +
    			"	<p><span style=\"font-weight: bold;\">Description: </span>" +
    				description +
    			"	</p>" +
    			"</div>";
    	
    	output.add(desc);
	}

	private void addLogLink(ITestResult result, List<String> output) {
		String logDivId = "logDivId" + result.getMethod().getMethodName();
        String logId = "logId" + result.getMethod().getMethodName();
        
        String logSrc = "#";
        if (result.getMethod().isTest()) {
          logSrc =  "../logs/" 
              + MdcTestController.MDC_TEST_LOG_SIFTING_VALUE_PREFIX 
              + MetaTestManagerTng.getInstance().getOrAddByTestResult(result).getMdcKey() + ".html";
        }
        
        String logLinkText = "Test Log";
        
        output.add(
        		" 	<div id=\""+logDivId+"\" class=\"logDiv fullRowTestOutputDiv\">\n" +
        		"		<a id=\""+logId+"\" href=\""+logSrc+"\" target=\"_blank\">" + logLinkText + "</a>\n"+
        		"	</div>\n"
        		);
	}
}
