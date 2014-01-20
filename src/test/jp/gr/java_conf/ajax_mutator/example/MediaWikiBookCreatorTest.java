package jp.gr.java_conf.ajax_mutator.example;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class MediaWikiBookCreatorTest extends TestBase {
    public MediaWikiBookCreatorTest() {
        super(/* calc coverage */true, /* run in background */false);
    }

    @Test
    public void testOnHoverRegularPage() {
        before();
        By xpath = By.xpath("//*[@id=\"mw-content-text\"]/ul[2]/li[1]/a");
        waitUntil(ExpectedConditions.presenceOfElementLocated(xpath));
		WebElement findElement = findElement(xpath);
		testOnCase(findElement,true);
    }
    @Test
    public void testOnHoverTitleWithColon() {
        before();
        By xpath = By.xpath("//*[@id=\"mw-content-text\"]/ul[2]/li[5]/a");
        waitUntil(ExpectedConditions.presenceOfElementLocated(xpath));
		WebElement findElement = findElement(xpath);
		testOnCase(findElement,false);
    }
    @Test
    public void testOnHoverPageNotExist() {
        before();
        By xpath = By.xpath("//*[@id=\"mw-content-text\"]/ul[2]/li[6]/a");
        waitUntil(ExpectedConditions.presenceOfElementLocated(xpath));
        WebElement findElement = findElement(xpath);
		testOnCase(findElement,false);
    }
    @Test
    public void testAddRemovePage() {
        before();
        By xpath = By.xpath("//*[@id=\"mw-content-text\"]/ul[2]/li[1]/a");
        waitUntil(ExpectedConditions.presenceOfElementLocated(xpath));
        WebElement targetPage = findElement(xpath);
        Actions action = new Actions(getDriver());
		action.moveToElement(targetPage);
		action.perform();
		By popuplocator = By.xpath("//*[@id=\"collectionpopup\"]");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		WebElement popup = findElement(popuplocator);
		assertEquals(popup.isDisplayed(), true);
		By popupclicker = By.xpath("//*[@id=\"collectionpopup\"]/a");
		WebElement clicker_e = findElement(popupclicker);
		String s = clicker_e.getText();
		clicker_e.click();
		action.perform();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		action.moveToElement(targetPage);
		action.perform();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		String s2 = clicker_e.getText();
		assertEquals(s.equals(s2), false);
    }
    
    @Test
    public void testAddRemoveThisPage(){
        before();
        Actions action = new Actions(getDriver());
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        By xpath = By.xpath("//*[@id=\"coll-remove_article\" or @id=\"coll-add_article\"]");
        WebElement targetPage = findElement(xpath);
        String s = targetPage.getAttribute("title");
		action.moveToElement(targetPage);
		action.click();
		action.perform();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		xpath = By.xpath("//*[@id=\"coll-add_article\" or @id=\"coll-remove_article\"]");
		targetPage = findElement(xpath);
		String s2 = targetPage.getAttribute("title");
		System.out.println(s+"="+s2);
		assertEquals(s.equals(s2), false);
    }
    
    private boolean isNear(Point a, Point b, int e){
    	return a.x-e<b.x && a.y-e<b.y && a.x+e>b.x && a.y+e>b.y;
    }
    
	private void testOnCase(WebElement targetPage,Object...expects) {
		Actions action = new Actions(getDriver());
		action.moveToElement(targetPage);
		action.perform();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		By popuplocator = By.xpath("//*[@id=\"collectionpopup\"]");
		//waitUntil(ExpectedConditions.visibilityOfElementLocated(popuplocator));
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		WebElement popup = findElement(popuplocator);
		assertEquals(popup.isDisplayed(), expects[0]);
		if (!popup.isDisplayed())
			return;
		assertEquals(isNear(popup.getLocation(),new Point(targetPage.getLocation().x+targetPage.getSize().width/2+2,targetPage.getLocation().y+targetPage.getSize().height/2+2),5),true);
		int fuzz=5,e=10;
		int left = popup.getLocation().x;
		int width = popup.getSize().width;
		int top = popup.getLocation().y;
		int height = popup.getSize().height;
		int[] x_positions = {left-fuzz-e,left-fuzz+e,left+width+fuzz+e};
		int[] y_positions = {top-fuzz-e,top-fuzz+e,top+height+fuzz+e};
		for (int x:x_positions){
			for (int y:y_positions){
				action.moveToElement(targetPage);
				action.perform();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				action.moveToElement(popup);
				action.moveByOffset(x-(left+width/2), y-(top+height/2));
				action.perform();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (x==x_positions[1]&&y==y_positions[1]){
					//inside
					assertEquals(popup.isDisplayed(), true);
				}else{
					//outside
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					assertEquals(popup.isDisplayed(), false);
				}
			}
		}
		
		
	}

	private void before() {
		openUrl("http://localhost/ex/mediawiki/index.php/Main_Page");
        handleLogin();
        handleStart();
        waitUntil(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("collection-creatorbox")));
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
