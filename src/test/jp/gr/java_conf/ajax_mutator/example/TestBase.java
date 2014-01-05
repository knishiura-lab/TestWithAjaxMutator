package jp.gr.java_conf.ajax_mutator.example;

import jscover.Main;
import org.junit.AfterClass;
import org.junit.Before;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElementLocated;

/**
 * Base class for webdriver test cases that use JSCover to calculate coverage.
 * Note: this implementation use single WebDriver instance, i.e., test case cannot run in parallel.
 */
public abstract class TestBase {
    private static final int DEFAULT_WAIT_LIMIT_SEC = 10;
    private static WebDriver driver;
    private static WebDriverWait wait;
    private static boolean calcCoverage;
    private static boolean runInBackground;
    private static Process xvfbProcess;

    private final String[] JSCOVER_DEFAULT_ARGS = new String[]{
            "-fs",
            getOriginalFileRoot(),
            getInstrumentedFileRoot()
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
        if (calcCoverage) {
            try {
                String[] extraArgs = getExtraJsCoverArgs();
                String[] args = Arrays.copyOf(
                        JSCOVER_DEFAULT_ARGS, JSCOVER_DEFAULT_ARGS.length + extraArgs.length);
                System.arraycopy(
                        extraArgs, 0, args, JSCOVER_DEFAULT_ARGS.length, extraArgs.length);
                System.out.println(args);
                Main.main(args);
            } catch (IOException e) {
                throw new IllegalStateException(
                        "Fail to create instrumented JS files to calculate coverage",
                        e);
            }
        }
        driver = initDriver();
        driver.manage().timeouts().pageLoadTimeout(DEFAULT_WAIT_LIMIT_SEC, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, DEFAULT_WAIT_LIMIT_SEC);
        if (calcCoverage) {
            driver.get(getJsCoverageUrl());
        }
    }

    static protected WebDriver initDriver() {
        if (!runInBackground) {
            return new FirefoxDriver();
        }

        FirefoxBinary firefoxBinary = new FirefoxBinary();
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
        return new FirefoxDriver(firefoxBinary, null);
    }

    @AfterClass
    static public void terminateBrowser() throws Exception {
        if (calcCoverage) {
            driver.switchTo().window(driver.getWindowHandle());
            printCoverage();
        } else if (!runInBackground) {
            driver.close();
        }
        if (runInBackground) {
            driver.close();
            xvfbProcess.destroy();
        }
    }

    private static void printCoverage() {
        driver.findElement(By.id("summaryTab")).click();
        wait.until(textToBePresentInElementLocated(By.id("summaryTotal"), "%"));
        System.out.println("Statement coverage: "
                + driver.findElement(By.id("summaryTotal")).getText());
    }

    protected void openUrl(String url) {
        if (calcCoverage) {
            driver.switchTo().window(driver.getWindowHandle());
            WebElement location = driver.findElement(By.id("location"));
            location.clear();
            location.sendKeys(getInstrumentedUrl(url));
            for (WebElement elm: driver.findElements(By.tagName("button"))) {
                if (elm.getText().equals("Open in frame")) {
                    elm.click();
                    break;
                }
            }
            driver.switchTo().frame("browserIframe");
        } else {
            driver.get(url);
        }
    }

    /**
     * @return path to the directory where test target JavaScript file exists.
     */
    protected abstract String getOriginalFileRoot();

    /**
     * @return path to the directory where instrumented JavaScript file should be generated to
     * calculate coverage.
     */
    protected abstract String getInstrumentedFileRoot();

    /**
     * @return extra argument that passed to JSCover.
     */
    protected abstract String[] getExtraJsCoverArgs();

    /**
     * convert given url to it's instrumented version
     */
    protected abstract String getInstrumentedUrl(String url);

    /**
     * @return url of jscoverage.html
     */
    protected abstract String getJsCoverageUrl();

    protected static WebDriver getDriver() {
        return driver;
    }

    protected static WebDriverWait getWait() {
        return wait;
    }
}
