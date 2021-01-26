/**
 * @author Raghvendra Gupta
 * @author Last updated by Raghvendra Gupta: $
 * @version : $
 */


import Utilities.DataService;
import Utilities.PoIExcelUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class MainRunner {

    private String envName = "";
    private String envURL = "";
    private String userName = "";
    private String password = "";
    private String ExcelFile = "";
    private String ExcelSheet = "";
    private String dbConnectionString = "";
    private String dbUsernamePwd = "";
    private String envGAVersion = "";
    private String dumpType="";
    private Map<String, String> globalVarsData;
    public static WebDriver driver;


    public MainRunner(String env, String url, String userName_pwd, String dbConnString, String dbUserPwd, String dumpType, String envGAVersion) {
        this.envName = env;
        this.envURL = url;
        this.userName = userName_pwd;
        this.dbConnectionString = dbConnString;
        this.dbUsernamePwd = dbUserPwd;
        this.envGAVersion = envGAVersion;
        this.dumpType = dumpType;
    }


    @BeforeClass
    public void importGlobalVars(ITestContext context) throws Exception {

        ExcelFile = context.getCurrentXmlTest().getAllParameters().get("ExcelFile");
        ExcelSheet = context.getCurrentXmlTest().getAllParameters().get("ExcelSheet");

    }

    @BeforeTest
    public static void setDriver() {
        System.setProperty("webdriver.chrome.driver", "Drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        //driver = new HtmlUnitDriver(DesiredCapabilities.htmlUnit());


    }


    @Test(priority = 0)
    public void isUrlReachable()  {
        try {
            PoIExcelUtils.cleanSystemGeneratedDataInExcel(ExcelFile,ExcelSheet,envName);
            System.out.println("Cleaned excel sheet for env="+envName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.get(envURL);
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        if (/*driver.getTitle().equalsIgnoreCase("JDA : Login") &&*/ driver.findElements(By.xpath("//*[@id=\"loginBox\"]/input[@name=\"USER_NAME\"]")).size() > 0) {
            System.out.println("****Opened browser for " + envName);
            try {
                PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "URL Reachable?", envName, "UP");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("****Opened browser for " + envName + " but couldn't navigate to URL");

            try {
                PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "URL Reachable?", envName, "DOWN");
                PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Basic Nodepool Running?", envName, "DOWN");
                PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Collector Nodepool Running?", envName, "DOWN");
                PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "PromoWS Nodepool Running?", envName, "DOWN");
                PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "PromoNG Nodepool Running?", envName, "DOWN");
                PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Current SCPO Build Date", envName, "DOWN");
                PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Current JDA Platform Build Date", envName, "DOWN");
                PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Current SCPO Install Type", envName, "DOWN");
                PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Current Platform Installation Type", envName, "DOWN");
                PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "SCPO Build Label", envName, "DOWN");
                PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Platform Build Label", envName, "DOWN");
            } catch (Exception e) {
                e.printStackTrace();
            }
            Assert.assertTrue(driver.findElement(By.xpath("//*[@id=\"loginBox\"]/span[2]/span")).isDisplayed());
        }
    }

    @Test(dependsOnMethods = "isUrlReachable", priority = 2)
    public void getNodePoolStatus() throws Exception {
        ResultSet rs1 = null;
        String[] splitDBUserPwd = dbUsernamePwd.split("/");
        String dbUsername = splitDBUserPwd[0];
        String dbpwd = splitDBUserPwd[1];
        DataService dbService = new DataService(dbConnectionString, dbUsername, dbpwd);
        String hostname = new URL(envURL).getHost();

        try {
            rs1 = DataService.getResultSet("select pool_name,host_name,pool_state from sre_node_pool where host_name='" + hostname.toUpperCase() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        while (rs1.next()) {
            String pool_name = rs1.getString("pool_name");
            String host_name = rs1.getString(2);
            int pool_state = rs1.getInt(3);
            System.out.println(pool_name + " " + host_name + " " + pool_state);
            System.out.println("---------------");
            if (pool_name.equalsIgnoreCase("Basic")) {
                if (pool_state == 1) {
                    try {
                        PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Basic Nodepool Running?", envName, "UP");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (pool_state == 0) {
                    try {
                        PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Basic Nodepool Running?", envName, "DOWN");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (pool_state == 2) {
                    try {
                        PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Basic Nodepool Running?", envName, "SHUTTING DOWN");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (pool_name.equalsIgnoreCase("C") || pool_name.equalsIgnoreCase("Collector")) {

                if (pool_state == 1) {
                    try {
                        PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Collector Nodepool Running?", envName, "UP");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (pool_state == 0) {
                    try {
                        PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Collector Nodepool Running?", envName, "DOWN");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (pool_state == 2) {
                    try {
                        PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Collector Nodepool Running?\n", envName, "SHUTTING DOWN");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else if (pool_name.equalsIgnoreCase("PromoWSRMI")) {

                if (pool_state == 1) {
                    try {
                        PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "PromoWS Nodepool Running?", envName, "UP");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (pool_state == 0) {
                    try {
                        PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "PromoWS Nodepool Running?", envName, "DOWN");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (pool_state == 2) {
                    try {
                        PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "PromoWS Nodepool Running?", envName, "SHUTTING DOWN");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (pool_name.equalsIgnoreCase("PromoNG")) {

                if (pool_state == 1) {
                    try {
                        PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "PromoNG Nodepool Running?", envName, "UP");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (pool_state == 0) {
                    try {
                        PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "PromoNG Nodepool Running?", envName, "DOWN");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (pool_state == 2) {
                    try {
                        PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "PromoNG Nodepool Running?", envName, "SHUTTING DOWN");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    @Test(dependsOnMethods = "isUrlReachable", priority = 1)
    public void getBuildData() throws Exception {
        String[] splitUserPwd = userName.split("/");
        String username = splitUserPwd[0];
        String password = splitUserPwd[1];

        driver.findElement(By.xpath("//*[@id=\"loginBox\"]/input[@name=\"USER_NAME\"]")).sendKeys(username);
        driver.findElement(By.xpath("//*[@id=\"loginBox\"]/input[@name=\"PASSWORD\"]")).sendKeys(password);
        driver.findElement(By.xpath("//*[@id=\"loginBox\"]/span[2]/span")).click();
        Thread.sleep(3000);
        URL url = new URL(envURL);
        String hostname = url.getHost();
        int port = url.getPort();
        String aboutPageURL = "/jda/home/VP?frame=vpAbout&LaunchParm=Initialize";
        String aboutURL = hostname + ":" + port + aboutPageURL;
        driver.get(aboutURL);
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
        Thread.sleep(5000);

        String wholeAboutSection = driver.findElement(By.xpath("//*[@id=\"AboutPopupText\"]")).getText();
        String[] splitAboutSection = wholeAboutSection.split("\n");
        int counterBuildDate = 0;
        int counterInstallType = 0;
        int counterBuildLabel=0;
        String scpoBuildDate = "null:null";
        String platformBuildDate = "null:null";
        String scpoInstallType = "null:null";
        String platformInstallType = "null:null";
        String scpoBuildLabel = "null:null";
        String platformBuildLabel = "null:null";
        for (String tempStr : splitAboutSection) {
            if (tempStr.contains("Build Date:")) {
                counterBuildDate++;
                    if (counterBuildDate == 1) {
                        scpoBuildDate = tempStr;
                    } else if (counterBuildDate == 2) {
                        platformBuildDate = tempStr;

                }
            } else if (tempStr.contains("Install Type:")) {
                counterInstallType++;
                if (counterInstallType == 1) {
                    scpoInstallType = tempStr;
                } else if (counterInstallType== 2) {
                    platformInstallType = tempStr;

                }
            } else if (tempStr.contains("Build Label:")) {
                counterBuildLabel++;
                if (counterBuildLabel== 1) {
                    scpoBuildLabel= tempStr;
                } else if (counterBuildLabel == 2) {
                    platformBuildLabel= tempStr;

                }
            }
        }

        try {

            PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Current SCPO Build Date", envName, scpoBuildDate.split(":")[1]);
            System.out.println("Writing scpo build date");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while writing scpo build date");
        }
        try {
            PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Current JDA Platform Build Date", envName, platformBuildDate.split(":")[1]);
            System.out.println("Writing platform build date");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while Writing platform build date");
        }
        try {
            PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Current SCPO Install Type", envName, scpoInstallType.split(":")[1]);
            System.out.println("Writing current scpo install type");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while Writing current scpo install type");
        }
        try {
            PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Current Platform Installation Type", envName, platformInstallType.split(":")[1]);
            System.out.println("Writing current platform install type");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while writing platform install type");
        }
        try {
            PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "SCPO Build Label", envName, scpoBuildLabel.split(":")[1]);
            System.out.println("Writing SCPO Build Label");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while writing SCPO Build Label");
        }
        try {
            PoIExcelUtils.write_data(ExcelFile, ExcelSheet, "Platform Build Label", envName, platformBuildLabel.split(":")[1]);
            System.out.println("Writing Platform Build Label");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error while writing platform build label");
        }
    }

    @AfterTest
    public void closeBrowser() throws InterruptedException {
        System.out.println("Closing browser for env " + envName);
        Thread.sleep(2000);
        driver.quit();
        try {
            PoIExcelUtils.convertXLXStoCSV(ExcelFile, ExcelSheet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            PoIExcelUtils.copyFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

/*"C:/Apache24/htdocs/EnvDashboard/data/EnvList.csv"*/