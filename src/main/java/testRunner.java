




import Utilities.PoIExcelUtils;
import org.testng.TestListenerAdapter;
import org.testng.TestNG;
import org.testng.collections.Lists;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Raghvendra Gupta
 * @author Last updated by Raghvendra Gupta: $
 * @version : $
 */

public class testRunner {
    public static void main(String[] args) throws Exception {
        //PoIExcelUtils.cleanSystemGeneratedDataInExcel("src\\main\\resources\\EnvList.xlsx","EnvList","Pep01");
        //PoIExcelUtils.get_data("EnvList.xlsx","EnvList",3);
        PoIExcelUtils.convertXLXStoCSV("src\\main\\resources\\EnvList.xlsx","EnvList");
        //PoIExcelUtils.copyFile();
        /*TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testng = new TestNG();
        List<String> suites = Lists.newArrayList();
        suites.add("getEnvStatus.xml");//path to xml..
        //suites.add("c:/tests/testng2.xml");
        testng.setTestSuites(suites);
        testng.run();*/

       /* XmlSuite suite = new XmlSuite();
        suite.setName("EnvDashboard");
        XmlTest test = new XmlTest(suite);
        test.setName("envStatus");
        test.setGroupByInstances(true);

        Map<String,String> params = new HashMap<String,String>();
        params.put("ExcelFile","EnvList.xlsx");
        params.put("ExcelSheet","EnvList");
        test.setParameters(params);

        List<XmlClass> classes = new ArrayList<XmlClass>();
        classes.add(new XmlClass("DataFactory"));
        test.setXmlClasses(classes) ;

        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        suites.add(suite);
        TestNG tng = new TestNG();
        tng.setXmlSuites(suites);
        tng.run();*/
    }
}
