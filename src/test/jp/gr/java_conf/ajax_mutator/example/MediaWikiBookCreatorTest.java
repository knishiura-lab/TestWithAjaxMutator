package jp.gr.java_conf.ajax_mutator.example;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MediaWikiBookCreatorTest extends TestBase {
    public MediaWikiBookCreatorTest() {
        super(/* calc coverage */false, /* run in background */true);
    }
    
    @Before
    public void setUp() {
    	openUrl("http://localhost/ex/mediawiki/index.php/Main_Page");
        handleLogin();
        handleStart();
        waitUntil(ExpectedConditions.presenceOfElementLocated(By.className("collection-creatorbox")));
    }

    @Test
    public void toggleArticle_shouldToggleArticleLink() {        
    	findElement(By.id("coll-add_article")).click();
        waitUntil(ExpectedConditions.presenceOfElementLocated(By.id("coll-remove_article")));
        findElement(By.id("coll-remove_article")).click();
        waitUntil(ExpectedConditions.presenceOfElementLocated(By.id("coll-add_article")));
    }
    
    @Test
    public void toggleLinkedPage_shouldToggleLinkedPage() throws Exception {
    	Actions actions = new Actions(getDriver());
        actions.moveToElement(findElement(By.xpath("//*[@id='mw-content-text']/ul[2]/li[1]/a"))).build().perform();
        
        WebElement popupElement = findElement(By.id("collectionpopup"));
        waitUntil(ExpectedConditions.visibilityOf(popupElement));
        assertTrue(popupElement.findElement(By.xpath("//*[@id='collectionpopup']/a/img")).getAttribute("src")
        		.endsWith("mediawiki/extensions/Collection/images/silk-add.png"));
        
        actions.moveToElement(popupElement.findElement(By.tagName("a"))).build().perform();
        popupElement.findElement(By.tagName("a")).click();        
        
        actions.moveToElement(findElement(By.xpath("//*[@id='mw-content-text']/ul[1]/li[1]/a"))).build().perform();
        sleep(2);
        
        actions.moveToElement(findElement(By.xpath("//*[@id='mw-content-text']/ul[2]/li[1]/a"))).build().perform();
        waitUntil(ExpectedConditions.visibilityOf(popupElement));        
        assertTrue(popupElement.findElement(By.xpath("//*[@id='collectionpopup']/a/img")).getAttribute("src")
        		.endsWith("/mediawiki/extensions/Collection/images/silk-remove.png"));
        
        actions.moveToElement(popupElement.findElement(By.tagName("a"))).build().perform();
        popupElement.findElement(By.tagName("a")).click();
        
        actions.moveToElement(popupElement.findElement(By.tagName("a"))).build().perform();
        popupElement.findElement(By.tagName("a")).click();        
        
        actions.moveToElement(findElement(By.xpath("//*[@id='mw-content-text']/ul[1]/li[1]/a"))).build().perform();
        sleep(2);
        
        actions.moveToElement(findElement(By.xpath("//*[@id='mw-content-text']/ul[2]/li[1]/a"))).build().perform();
        waitUntil(ExpectedConditions.visibilityOf(popupElement));        
        assertTrue(popupElement.findElement(By.xpath("//*[@id='collectionpopup']/a/img")).getAttribute("src")
        		.endsWith("/mediawiki/extensions/Collection/images/silk-add.png"));
        
    }
    
    private void sleep(int sec) {
    	try{
    		Thread.sleep(sec * 1000);
    	}
    	catch(Exception e) {
    		
    	}
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
