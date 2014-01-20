package jp.gr.java_conf.ajax_mutator.example;

import com.google.common.base.Predicate;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class MediaWikiBookCreatorTest extends TestBase {
    public MediaWikiBookCreatorTest() {
        super(/* calc coverage */true, /* run in background */false);
    }

    @Test
    public void testSomething() throws InterruptedException {
        openUrl("http://localhost/ex/mediawiki/index.php/Main_Page");
        handleLogin();
        handleStart();

        int count = getAddedBookCount();
        waitUntil(ExpectedConditions.presenceOfElementLocated(By.id("coll-add_article")));
        findElement(By.id("coll-add_article")).click();
        waitUntil(ExpectedConditions.presenceOfElementLocated(By.id("coll-remove_article")));
        final int count1 = getAddedBookCount();
        assertEquals(count + 1, count1);

        Actions action = new Actions(getDriver());
        action.moveToElement(findElement(By.xpath("//*[@id=\"mw-content-text\"]/ul[2]/li[1]/a[1]")));
        action.pause(1000);
        action.moveToElement(findElement(By.id("collectionpopup")));
        action.perform();
        assertFalse(findElement(By.id("collectionpopup")).getAttribute("style").contains("none"));

        action = new Actions(getDriver());
        action.moveToElement(findElement(By.xpath("//*[@id=\"mw-content-text\"]/ul[2]/li[1]/a[1]")));
        action.pause(1000);
        action.moveToElement(findElement(By.id("collectionpopup")));
        action.pause(1000);
        action.moveByOffset(100,100);
        action.pause(1000);
        assertFalse(findElement(By.id("collectionpopup")).getAttribute("style").contains("none"));

        action = new Actions(getDriver());
        action.moveToElement(findElement(By.xpath("//*[@id=\"mw-content-text\"]/ul[2]/li[1]/a[1]")));
        action.pause(1000);
        action.moveToElement(findElement(By.id("collectionpopup")));
        action.perform();
        findElement(By.id("collectionpopup")).findElement(By.xpath("a")).click();
        waitUntil(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver webDriver) {
                int count2 = getAddedBookCount();
                return count1 + 1 == count2;
            }
        });
        int count2 = getAddedBookCount();
        assertEquals(count1 + 1, count2);

    }

    private int getAddedBookCount(){
        int count = -1;
        String showBook = findElement(By.xpath("//*[@id=\"coll-book_creator_box\"]/a[2]")).getText();
        Pattern p = Pattern.compile("\\d");
        Matcher m = p.matcher(showBook);
        if(m.find()) {
            count = new Integer(m.group());
        }

        return count;
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
