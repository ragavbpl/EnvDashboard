


import Utilities.PoIExcelUtils;
import Utilities.variablesProvider;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

/**
 * @author Raghvendra Gupta
 * @author Last updated by Raghvendra Gupta: $
 * @version : $
 */

public class DataFactory {
    @DataProvider()
    public Object[][] envList(ITestContext context) throws Exception {
        String FileType = context.getCurrentXmlTest().getAllParameters().get("FileFormat");
        if (FileType != null && FileType.equalsIgnoreCase("XML")) {
            return null;
        } else {
            String ExcelFile = context.getCurrentXmlTest().getAllParameters().get("ExcelFile");
            String ExcelSheet = context.getCurrentXmlTest().getAllParameters().get("ExcelSheet");
            Object[][] testObjArray = PoIExcelUtils.get_data(ExcelFile, ExcelSheet, variablesProvider.dataProviderWithValuesCols);
            return (testObjArray);
        }
    }

    @Factory(dataProvider = "envList")
    public Object[] createInstances(String env, String url, String userName_pwd, String dbConnString, String dbUserPwd, String dumpType, String envGAVersion) {
        return new Object[]{
                new MainRunner(env, url, userName_pwd, dbConnString, dbUserPwd, dumpType, envGAVersion)
        };
    }
}
