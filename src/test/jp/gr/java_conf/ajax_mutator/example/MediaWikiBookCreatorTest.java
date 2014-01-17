package jp.gr.java_conf.ajax_mutator.example;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class MediaWikiBookCreatorTest extends TestBase {
    public MediaWikiBookCreatorTest() {
        super(/* calc coverage */true, /* run in background */false);
    }

    @Test
    public void testSomething() {
        openUrl("http://localhost/ex/mediawiki/index.php/Main_Page");
        handleLogin();
        handleStart();
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
