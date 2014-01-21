package jp.gr.java_conf.ajax_mutator.example;

import com.google.common.base.Predicate;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class MediaWikiBookCreatorTest extends TestBase {
    public MediaWikiBookCreatorTest() {
        super(/* calc coverage */true, /* run in background */false);
    }

    @Test
    public void testSomething() throws Exception {
        openUrl("http://localhost/ex/mediawiki/index.php/Main_Page");
        handleLogin();
        handleStart();
        // please implement here
        waitUntil(ExpectedConditions.presenceOfElementLocated(By.id("siteNotice")));
        findElement(By.id("coll-add_article")).click();
        Actions action = new Actions(getDriver());
        action.moveToElement(findElement(By.id("mw-content-text")).findElements(By.tagName("ul")).get(1).findElement(By.tagName("li")).findElement(By.tagName("a")));
        action.pause(1000);
        action.moveToElement(findElement(By.id("collectionpopup")).findElement(By.tagName("a")));
        action.pause(1000);
        action.perform();
        Thread.sleep(1000);
        Assert.assertTrue(findElements(By.className("collection-creatorbox-iconlink")).get(0).getText().equals(" Show book (1 page)"));

        action.moveToElement(findElement(By.id("siteNotice")));
        action.pause(1000);
        action.perform();
        waitUntil(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver webDriver) {
                return !findElement(By.id("collectionpopup")).isDisplayed();
            }
        });
        Thread.sleep(1000);
        action = new Actions(getDriver());
        action.moveToElement(findElement(By.id("mw-content-text")).findElements(By.tagName("ul")).get(1).findElement(By.tagName("li")).findElement(By.tagName("a")));
        action.pause(1000);

        action.perform();
        Thread.sleep(1000);
        Actions action2 = new Actions(getDriver());
        action2.moveToElement(findElement(By.id("mw-content-text")).findElements(By.tagName("ul")).get(1).findElements(By.tagName("li")).get(4).findElement(By.tagName("a")));
        action2.pause(1000);
        action2.perform();
        Thread.sleep(1000);
        action.moveToElement(findElement(By.id("mw-content-text")).findElements(By.tagName("ul")).get(1).findElement(By.tagName("li")).findElement(By.tagName("a")));
        action.pause(1000);
        action.perform();
        Thread.sleep(1000);
//        findElement(By.id("mw-content-text")).findElements(By.tagName("ul")).get(1).findElement(By.tagName("li")).findElements(By.tagName("a")).get(1).click();
        findElement(By.id("collectionpopup")).findElement(By.tagName("a")).click();
        Thread.sleep(1000);
        System.out.println(findElements(By.className("collection-creatorbox-iconlink")).get(0).getText());
        Assert.assertTrue(findElements(By.className("collection-creatorbox-iconlink")).get(0).getText().equals(" Show book (2 pages)"));
        waitUntil(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver webDriver) {
                return (findElement(By.id("collectionpopup")).findElements(By.tagName("img")).size()!=0);
            }
        });
    }

    /**
     * Start book creator
     */
    private void handleStart() {
        if (findElements(By.id("coll-create_a_book")).size() == 0) {
            return;
        }
        findElement(By.id("coll-create_a_book")).findElement(By.tagName("a")).click();
        waitUntil(ExpectedConditions.presenceOfElementLocated(By.className("collection-button")));
        List<WebElement> collectionButtons = findElements(By.className("collection-button"));
        for (WebElement element:collectionButtons) {
            if (element.getAttribute("class").contains("ok")) {
                element.findElement(By.tagName("a")).click();
                break;
            }
        }
    }

    /**
     * attempt login if user is current logged out.
     */
    private void handleLogin() {
        if (findElements(By.id("pt-login")).isEmpty()) {
            // User is already logged in.
            return;
        }
        findElement(By.id("pt-login")).findElement(By.tagName("a")).click();
        waitUntil(ExpectedConditions.presenceOfElementLocated(By.id("wpName1"))).sendKeys("testuser");
        findElement(By.id("wpPassword1")).sendKeys("test");
        if (!findElement(By.id("wpRemember")).isSelected()) {
            findElement(By.id("wpRemember")).click();
        }
        findElement(By.id("wpLoginAttempt")).click();
    }

    @Override
    protected String getOriginalFileRoot() {
        return "/var/www/ex/mediawiki/extensions/Collection";
    }

    @Override
    protected String getInstrumentedFileRoot() {
        return "/var/www/ex/js-cover/instrumented/mediawiki/extensions/Collection";
    }

    @Override
    protected String[] getExtraJsCoverArgs() {
        return new String[] {"--only-instrument-reg=.*bookcreator.*"};
    }

    @Override
    protected String getInstrumentedUrl(String url) {
        return url.replace("/mediawiki/", "/js-cover/instrumented/mediawiki/");
    }

    @Override
    protected String getJsCoverageUrl() {
        return "http://localhost/ex/js-cover/instrumented/mediawiki/extensions/Collection/jscoverage.html";
    }
}
