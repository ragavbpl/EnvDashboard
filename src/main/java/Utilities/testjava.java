package Utilities;

/*import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;*/

/**
 *
 * @author
 */
public class testjava {
    public static void main(String[] args) throws Exception {
        PoIExcelUtils.convertXLXStoCSV("Resources\\EnvList.xlsx","EnvList");
    }
}
