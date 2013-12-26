package jp.gr.java_conf.ajax_mutator.example;

import jscover.Main;
import org.junit.AfterClass;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
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
    private static boolean runInBackground;
    private static Process xvfbProcess;

    private static String[] jsCoverArgs = new String[]{
            "-ws",
            "--port=3129",
            "--proxy",
            "--local-storage",
            "--no-instrument-reg=.*jquery.*",
            "--report-dir=" + COVERAGE_REPORT_DIR_PATH
    };

    public TestBase(boolean calcCoverage, boolean runInBackground) {
        this.calcCoverage = calcCoverage;
        this.runInBackground = runInBackground;
    }

    @Before
    public void setUpBrowser() {
        if (driver != null) {
            return;
        }
        driver = initDriver();
        driver.manage().timeouts().pageLoadTimeout(DEFAULT_WAIT_LIMIT_SEC, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, DEFAULT_WAIT_LIMIT_SEC);
    }

    static protected WebDriver initDriver() {
        if (!calcCoverage && !runInBackground) {
            return new FirefoxDriver();
        }

        FirefoxBinary firefoxBinary = new FirefoxBinary();
        DesiredCapabilities capabilities = new DesiredCapabilities();
        if (calcCoverage) {
            launchProxyToMeasureCoverage();
            Proxy proxy = new Proxy().setHttpProxy("localhost:3129");
            capabilities.setCapability(CapabilityType.PROXY, proxy);
        }
        if (runInBackground) {
            Runtime runtime = Runtime.getRuntime();
            try {
                xvfbProcess = runtime.exec("Xvfb :3156 -screen 0 1024x768x24");
            } catch (IOException e) {
                throw new IllegalStateException("Fail to lanch Xvfb", e);
            }
            String Xport = System.getProperty("lmportal.xvfb.id", ":3156");
            firefoxBinary.setEnvironmentProperty("DISPLAY", Xport);
        }
        return new FirefoxDriver(firefoxBinary, null, capabilities);
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
            if (runInBackground) {
                xvfbProcess.destroy();
            }
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
        if (runInBackground) {
            driver.close();
            xvfbProcess.destroy();
        }
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
