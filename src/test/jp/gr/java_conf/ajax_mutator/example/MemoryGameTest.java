package jp.gr.java_conf.ajax_mutator.example;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.ajax_mutator.example.TestBase;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

public class MemoryGameTest extends TestBase {
    public MemoryGameTest() {
        super(
                /* calc coverage */ true,
                /* run in background*/ false);
    }

    @Test
    public void testWhole() throws InterruptedException {
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("#start_stop");
        Thread.sleep(15000);
//        waitAndClick("#timer");S
//        waitAndClick("#start_stop");
//        waitAndClick("#cheat");
        for (int i = 0; i < 20; i++) {
        	for (int j = 0; j < 20; j++) {
                waitAndClick("#box_" + i);
                waitAndClick("#box_" + j);
        	}
        }
        Thread.sleep(5000);
        waitAndClick("div.ui-dialog:nth-child(40) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
    }

    @Test
    public void testCheet() throws InterruptedException {
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("#start_stop");
        Thread.sleep(15000);
        waitAndClick("#cheat");
        Thread.sleep(5000);
    }

    @Test
    public void testStop() throws InterruptedException {
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("#start_stop");
        Thread.sleep(15000);
        waitAndClick("#start_stop");
    }
    
    @Test
    public void testUserKeyword() throws InterruptedException {
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
    	By keywordIn = By.cssSelector("#imageKeywords");
        waitUntil(ExpectedConditions.presenceOfElementLocated(keywordIn));
        findElement(keywordIn).sendKeys("dog");
        waitAndClick("#loadUserImages");
        waitAndClick("#loadUserImages");
        waitAndClick("#start_stop");
        Thread.sleep(2000);
    }
    
    // tests added after mutation analysis
    @Test
    public void testUserKeywordViable() throws InterruptedException {
    	// mutant #2
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
    	By keywordIn = By.cssSelector("#imageKeywords");
        waitUntil(ExpectedConditions.presenceOfElementLocated(keywordIn));
        findElement(keywordIn).sendKeys("dog");
        waitAndClick("#loadUserImages");
        waitUntil(ExpectedConditions.textToBePresentInElementLocated(By.id("status"), "Searching"));
    }
    
    @Test
    public void testCheatViable() throws InterruptedException {
    	// mutant #3
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("#start_stop");
        Thread.sleep(15000);
        waitAndClick("#cheat");
        waitUntil(new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver arg0) {
		        return findElement(By.id("box_0")).getAttribute("style").contains("background-image");
			}
		});
    }

    @Test(expected=TimeoutException.class)
    public void testImagePresent() throws InterruptedException {
    	// mutant #30
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("#start_stop");
        Thread.sleep(15000);
        waitAndClick("#cheat");
        waitUntil(new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver arg0) {
		        return findElement(By.id("box_0")).getAttribute("style").contains("url(\"undefined\")");
			}
		});
    }
    
    @Test(expected=TimeoutException.class)
    public void testBodyBackground() throws InterruptedException {
    	// mutant #44
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("#start_stop");
        waitUntil(new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver arg0) {
		        return findElement(By.tagName("body")).getAttribute("style").contains("background-image");
			}
		});
    }
    
    @Test
    public void testMatchCount() throws InterruptedException {
    	// mutant #48
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("#start_stop");
        Thread.sleep(15000);
       	assertEquals("Total matches: 0", findElement(By.id("matchCount")).getText());
       	for (int j = 0; j < 20; j++) {
            waitAndClick("#box_0");
            waitAndClick("#box_" + j);
        }
        waitUntil(new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver arg0) {
				return !"Total matches: 0".equals(findElement(By.id("matchCount")).getText());
			}
        });
    }

    @Test
    public void testCheatDisappear() throws InterruptedException {
    	// mutant #51
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("#start_stop");
        Thread.sleep(15000);
        outer: for (int i = 0; i < 20; i++) {
        	for (int j = 0; j < 20; j++) {
                waitAndClick("#box_" + i);
                waitAndClick("#box_" + j);
                if (!getDriver().findElements(By.cssSelector("div.ui-dialog:nth-child(40) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)")).isEmpty()) {
                	break outer;
                }
        	}
        }
        Thread.sleep(5000);
        waitAndClick("div.ui-dialog:nth-child(40) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        assertTrue(findElement(By.id("cheat")).getAttribute("style").contains("none"));
    }

    @Test
    public void testTimeElapsed() throws InterruptedException {
    	// mutant #53
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("#start_stop");
        Thread.sleep(15000);
        outer: for (int i = 0; i < 20; i++) {
        	for (int j = 0; j < 20; j++) {
                waitAndClick("#box_" + i);
                waitAndClick("#box_" + j);
                if (!getDriver().findElements(By.cssSelector("div.ui-dialog:nth-child(40) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)")).isEmpty()) {
                	break outer;
                }
        	}
        }
        waitUntil(new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver arg0) {
				return !"".equals(findElement(By.id("timeElapsed")).getText());
			}
        });
    }
    
    @Test
    public void testPositivePercentage() throws InterruptedException {
    	// mutant #54
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("#start_stop");
        Thread.sleep(15000);
        outer: for (int i = 0; i < 20; i++) {
        	for (int j = 0; j < 20; j++) {
                waitAndClick("#box_" + i);
                waitAndClick("#box_" + j);
                if (!getDriver().findElements(By.cssSelector("div.ui-dialog:nth-child(40) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)")).isEmpty()) {
                	break outer;
                }
        	}
        }
        waitUntil(new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver arg0) {
				return !"".equals(findElement(By.id("positivePercentage")).getText());
			}
        });
    }
    
    @Test(expected=TimeoutException.class)
    public void testUserImagesClick() throws InterruptedException {
    	// mutant #57
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
        waitAndClick("div.ui-dialog:nth-child(22) > div:nth-child(3) > div:nth-child(1) > button:nth-child(1)");
    	By keywordIn = By.cssSelector("#imageKeywords");
        waitUntil(ExpectedConditions.presenceOfElementLocated(keywordIn));
        findElement(keywordIn).sendKeys("dog");
        waitAndClick("#imageKeywords");
        waitUntil(ExpectedConditions.textToBePresentInElementLocated(By.id("status"), "Searching"));
    }
    
    private void waitAndClick(String sel) {
    	By button = By.cssSelector(sel);
        waitUntil(ExpectedConditions.visibilityOfElementLocated(button));
        findElement(button).click();
    }
    
    @Override
    protected String getOriginalFileRoot() {
        return "/var/www/ex/p3.monkeyaround.biz";
    }

    @Override
    protected String getInstrumentedFileRoot() {
        return "/var/www/ex/js-cover/instrumented/p3.monkeyaround.biz";
    }

    @Override
    protected String[] getExtraJsCoverArgs() {
        return new String[] {"--no-instrument-reg=.*jquery.*"};
    }

    @Override
    protected String getInstrumentedUrl(String url) {
        return url.replace("localhost/ex/p3.monkeyaround.biz", "localhost/ex/js-cover/instrumented/p3.monkeyaround.biz");
    }

    @Override
    protected String getJsCoverageUrl() {
        return "http://localhost/ex/js-cover/instrumented/p3.monkeyaround.biz/jscoverage.html";
    }
}
