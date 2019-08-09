//aon / aon-ta-rally / src / main / java / com / mastercard / atmn / aon / ta / rally / result / ResultMappingUtils.java
package com.mastercard.atmn.aon.ta.rally.result;

import com.mastercard.atmn.aon.rally.entities.testresults.TestResultVerdict;
import com.mastercard.atmn.aon.ta.report.model.ResultStatus;

public class ResultMappingUtils {
  
  private ResultMappingUtils() {
    // static only
  }

  public static TestResultVerdict mapAonResultToRallyVerdict(ResultStatus resultStatus) {
    
    switch(resultStatus) {
      
      case PASSED : return TestResultVerdict.PASS;
      case FAILED : return TestResultVerdict.FAIL;
      case SKIPPED : return TestResultVerdict.BLOCKED;
      default : return TestResultVerdict.FAIL;
    }
  }
  
  public static ResultStatus mapRallyVerdictToAonResult(TestResultVerdict resultStatus) {
    
    switch(resultStatus) {
      
      case PASS : return ResultStatus.PASSED;
      case FAIL : return ResultStatus.FAILED;
      case BLOCKED : return ResultStatus.SKIPPED;
      case ERROR : return ResultStatus.FAILED;
      case INCONCLUSIVE : return ResultStatus.FAILED;
      default : return ResultStatus.FAILED;
    }
  }
}
