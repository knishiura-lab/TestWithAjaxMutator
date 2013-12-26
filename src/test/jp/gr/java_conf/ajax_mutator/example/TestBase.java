package jp.gr.java_conf.ajax_mutator.example;

import jscover.Main;
import org.junit.AfterClass;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated;

/**
 * Base class for webdriver test cases that use JSCover to calculate coverage.
 * Note: this implementation use single WebDriver instance, i.e., test case cannot run in parallel.
 */
public class TestBase {
    private static final String COVERAGE_REPORT_DIR_PATH = "js-cover-report";
    private static final int DEFAULT_WAIT_LIMIT_SEC = 10;
    private static Thread server;
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static boolean calcCoverage;

    private static String[] jsCoverArgs = new String[]{
            "-ws",
            "--port=3129",
            "--proxy",
            "--local-storage",
            "--no-instrument-reg=.*jquery.*",
            "--report-dir=" + COVERAGE_REPORT_DIR_PATH
    };

    public TestBase(boolean calcCoverage) {
        this.calcCoverage = calcCoverage;
    }

    @Before
    public void setUpBrowser() {
        if (driver != null) {
            return;
        }
        if (calcCoverage) {
            launchProxyToMeasureCoverage();
        }
        driver = initDriver();
        driver.manage().timeouts().pageLoadTimeout(DEFAULT_WAIT_LIMIT_SEC, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, DEFAULT_WAIT_LIMIT_SEC);
    }

    static protected WebDriver initDriver() {
        if (!calcCoverage) {
            return new FirefoxDriver();
        }
        Proxy proxy = new Proxy().setHttpProxy("localhost:3129");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(CapabilityType.PROXY, proxy);
        return new FirefoxDriver(capabilities);
    }

    static private void launchProxyToMeasureCoverage() {
        server = new Thread(new Runnable() {
            public void run() {
                try {
                    Main.main(jsCoverArgs);
                } catch (IOException e) {
                    throw new RuntimeException("Error while running jsCover proxy.", e);
                }
            }
        });
        server.start();
    }

    @AfterClass
    static public void terminateBrowser() throws Exception {
        if (!calcCoverage) {
            driver.close();
            return;
        }
        driver.get("http://localhost/jscoverage.html");
        driver.switchTo().window(driver.getWindowHandle());
        wait.until(elementToBeClickable(By.id("storeTab"))).click();
        Thread.sleep(1000);
        wait.until(elementToBeClickable(By.id("storeButton"))).click();
        wait.until(textToBePresentInElementLocated(By.id("storeDiv"), "Coverage data stored at"));
        driver.get("file:///" + new File(COVERAGE_REPORT_DIR_PATH + "/jscoverage.html")
                .getAbsolutePath());
        printCoverage();
    }

    private static void printCoverage() {
        driver.findElement(By.id("summaryTab")).click();
        wait.until(textToBePresentInElementLocated(By.id("summaryTotal"), "%"));
        System.out.println("Statement coverage: "
                + driver.findElement(By.id("summaryTotal")).getText() + "%");
    }

    protected static WebDriver getDriver() {
        return driver;
    }

    protected static WebDriverWait getWait() {
        return wait;
    }
}
