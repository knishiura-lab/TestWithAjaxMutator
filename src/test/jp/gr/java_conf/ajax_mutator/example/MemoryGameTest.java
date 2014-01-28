package jp.gr.java_conf.ajax_mutator.example;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
    public void testSomething() {
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        
        waitUntil(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.id("instructions")));
        TimeOut(1200); // Wait for animation
        findElement(By.xpath("/html/body/div[10]/div[3]/div/button")).click();
        //TimeOut(5);
        waitUntil(ExpectedConditions.visibilityOf(findElement(By.id("musicConfirmation"))));
        TimeOut(1200); // Wait for animation
        findElement(By.xpath("/html/body/div[10]/div[3]/div/button")).click();
        
        // Use existing image keyword
        findElement(By.id("imageKeywords")).sendKeys("rats");
        TimeOut(600); // Wait for validation
        
        // Find custom images
        findElement(By.id("imageKeywords")).sendKeys(" and bears");
        WebElement imgSearchBtn = findElement(By.id("loadUserImages"));
        waitUntil(ExpectedConditions.visibilityOf(imgSearchBtn));
        imgSearchBtn.click();
        String statusText = findElement(By.id("status")).getText();
        assertEquals(true, statusText.contains("Searching images..."));
        
        WebElement startStop = findElement(By.id("start_stop"));
        waitUntil(ExpectedConditions.visibilityOf(startStop));
        startStop.click();
        
        /*waitUntil(ExpectedConditions.elementToBeClickable(startStop));
        startStop.click();
        WebElement status = findElement(By.id("status"));
        waitUntil(ExpectedConditions.textToBePresentInElement(status, "Game stopped!"));
        TimeOut(3000);
        
        startStop.click()*/
        
        final int numOfBoxes = 20;
        waitUntil(new Predicate<WebDriver>() {
        	@Override
        	public boolean apply(WebDriver driver) {
        		return driver.findElements(By.className("box")).size() == numOfBoxes;
        	}
        });
        ImageBox[] boxes = new ImageBox[numOfBoxes];
        List<WebElement> boxElms = findElements(By.className("box"));

        for(int i = 0; i < 20; i++) {
        	WebElement box = boxElms.get(i);
        	boxes[i] = new ImageBox(box, box.getCssValue("background-image"));
        }
        
        // Fetch corresponding images
        ImageBox target = boxes[0];
        ImageBox corresponding = null;
        ImageBox wrong = null;
    	for(int i = 1; i < numOfBoxes; i++) {
    		String cur = boxes[i].getImageUrl();
    		String tar = target.getImageUrl();
    		if(boxes[i].getImageUrl().equals(target.getImageUrl())) {
    			corresponding = boxes[i];
    			
    			if(i == numOfBoxes - 1)
    				wrong = boxes[--i];
    			else
    				wrong = boxes[++i];
    			break;
    		}
    	}
    	assertNotNull(corresponding);
        
        TimeOut(12000); // Wait for countdown to finish
        
        // Select wrong image combination
        target.getWebElement().click();
        wrong.getWebElement().click();        
        TimeOut(1000); // Wait for fade logic
        assertEquals("none", target.getWebElement().getCssValue("background-image"));
        assertEquals("none", wrong.getWebElement().getCssValue("background-image"));
        
        // Select right image combination
        target.getWebElement().click();
        corresponding.getWebElement().click();
        TimeOut(2000);
        
        // Cheat
        findElement(By.id("cheat")).click();
        for(int i = 0; i < numOfBoxes; i++) {
        	assertNotSame("none", boxes[i].getWebElement().getCssValue("background-image"));
        }
        TimeOut(2000);
        for(int i = 0; i < numOfBoxes; i++) {
        	assertEquals("none", boxes[i].getWebElement().getCssValue("background-image"));
        }
        
        
        // Win game
        for(int i = 0; i < numOfBoxes; i++) {
        	ImageBox tar = boxes[i];
        	ImageBox corr = null;
        	while(corr == null) {
	        	for(int j = 0; j < numOfBoxes; j++) {
	        		if(i != j) {
	        			if(tar.getImageUrl().equals(boxes[j].getImageUrl())) {
	        				corr = boxes[j];
	        			}
	        		}
	        	}
        	}
        	tar.getWebElement().click();
        	corr.getWebElement().click();        	
        	TimeOut(700);
        	assertNotSame("none", tar.getWebElement().getCssValue("background-image"));
        	assertNotSame("none", corr.getWebElement().getCssValue("background-image"));
        }
        
        
        
    }
    
    private void TimeOut(int ms) {
    	try {
    		Thread.sleep(ms);
    	}
    	catch(Exception ex) {}
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
    
    public static class ImageBox {
    	private WebElement _webElement;
    	private String _imageUrl;
    	
    	public ImageBox(WebElement webElement, String imageUrl) { 
    		_webElement = webElement;
    		_imageUrl = imageUrl;
    	}
    	
    	public WebElement getWebElement() {
    		return _webElement;
    	}
    	
    	public String getImageUrl() {
    		return _imageUrl;
    	}
    	
    	public void setWebElement(WebElement webElement) {
    		_webElement = webElement;
    	}
    	
    	public void setImageUrl(String imageUrl) {
    		_imageUrl = imageUrl;
    	}
    }

}
