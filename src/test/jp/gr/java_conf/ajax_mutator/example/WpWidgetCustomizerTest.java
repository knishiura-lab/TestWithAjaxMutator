package jp.gr.java_conf.ajax_mutator.example;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class WpWidgetCustomizerTest extends TestBase {
    public WpWidgetCustomizerTest() {
        super(/* calc coverage */false, /* run in background*/false);
    }

    @Test
    public void testTransition() throws InterruptedException {
        openUrl("http://localhost/ex/wp/wp-admin/customize.php?theme=twentyfourteen");
    	handleLogin();
    	Thread.sleep(5000);
    	waitAndClick("#accordion-section-sidebar-widgets-sidebar-3 > h3:nth-child(1)");
    	waitAndClick("#accordion-section-sidebar-widgets-sidebar-1 > h3:nth-child(1)");
    	waitAndClick("#accordion-section-title_tagline > h3:nth-child(1)");
    	waitAndClick("#accordion-section-colors > h3:nth-child(1)");
    	waitAndClick("#accordion-section-background_image > h3:nth-child(1)");
    	waitAndClick("#accordion-section-static_front_page > h3:nth-child(1)");
    	waitAndClick("#accordion-section-featured_content > h3:nth-child(1)");
    	
    	// save
       	waitAndClick("#accordion-section-sidebar-widgets-sidebar-3 > h3:nth-child(1)");
    	waitAndClick("#widget-14_archives-8 > div:nth-child(1) > div:nth-child(2) > h4:nth-child(1)");
    	waitUntil(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#widget-archives-8-title"))).sendKeys("archive");
    	waitAndClick("#widget-archives-8-savewidget");

    	// drag
    	waitAndClick("#accordion-section-sidebar-widgets-sidebar-1 > h3:nth-child(1)");
    	Thread.sleep(1000);
    	Actions builder = new Actions(getDriver());
    	for (int i = 0; i < 50; i++) builder.sendKeys(Keys.UP);
    	builder.clickAndHold(findElement(By.cssSelector("#widget-18_archives-2 > div:nth-child(1) > div:nth-child(2) > h4:nth-child(1)")));
    	builder.moveByOffset(0, -120);
    	builder.pause(1000);
    	builder.release();
    	builder.perform();
    	
    	// wait
    	Thread.sleep(3000);
    }
    
    @Test
    public void testWidgetSearch() throws InterruptedException {
        openUrl("http://localhost/ex/wp/wp-admin/customize.php?theme=twentyfourteen");
    	handleLogin();
    	Thread.sleep(5000);
    	waitAndClick("#accordion-section-sidebar-widgets-sidebar-3 > h3:nth-child(1)");
    	
    	// add and delete
    	waitAndClick("#customize-control-sidebars_widgets-sidebar-3 > span:nth-child(1) > b:nth-child(1)");
    	waitUntil(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#available-widgets-filter > input:nth-child(1)")));
    	findElement(By.cssSelector("#available-widgets-filter > input:nth-child(1)")).sendKeys("archive");
    	findElement(By.cssSelector("#available-widgets-filter > input:nth-child(1)")).sendKeys("\n");
    	waitAndClick("#widget-1_archives-10 > div:nth-child(2) > div:nth-child(1) > div:nth-child(9) > div:nth-child(1) > a:nth-child(1)");
    	
    	// search failure
    	waitAndClick("#customize-control-sidebars_widgets-sidebar-3 > span:nth-child(1) > b:nth-child(1)");
    	waitUntil(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#available-widgets-filter > input:nth-child(1)")));
    	findElement(By.cssSelector("#available-widgets-filter > input:nth-child(1)")).sendKeys("\n");
    	findElement(By.cssSelector("#available-widgets-filter > input:nth-child(1)")).sendKeys("  ");
    	findElement(By.cssSelector("#available-widgets-filter > input:nth-child(1)")).sendKeys("\n");
    	findElement(By.cssSelector("#available-widgets-filter > input:nth-child(1)")).sendKeys(Keys.ESCAPE);

    	// select and reset
    	waitAndClick("#customize-control-sidebars_widgets-sidebar-3 > span:nth-child(1) > b:nth-child(1)");
    	waitUntil(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#available-widgets-filter > input:nth-child(1)")));
    	findElement(By.cssSelector("#available-widgets-filter > input:nth-child(1)")).sendKeys("archive");
    	for (int i = 0; i < 10; i++) {
    		findElement(By.cssSelector("#available-widgets-filter > input:nth-child(1)")).sendKeys(Keys.BACK_SPACE);
    	}
    	findElement(By.cssSelector("#customize-control-sidebars_widgets-sidebar-3 > span:nth-child(1) > b:nth-child(1)")).sendKeys("abc");
    	waitAndClick("#customize-control-sidebars_widgets-sidebar-3 > span:nth-child(1) > b:nth-child(1)");
    	
    	// key up down
    	waitAndClick("#customize-control-sidebars_widgets-sidebar-3 > span:nth-child(1) > b:nth-child(1)");
    	waitUntil(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#available-widgets-filter > input:nth-child(1)")));
    	findElement(By.cssSelector("#available-widgets-filter > input:nth-child(1)")).sendKeys("a");
    	for (int i = 0; i < 10; i++) {
        	Thread.sleep(100);
    		new Actions(getDriver()).sendKeys(Keys.DOWN).perform();
    	}
    	for (int i = 0; i < 10; i++) {
        	Thread.sleep(100);
    		new Actions(getDriver()).sendKeys(Keys.UP).perform();
    	}
    	findElement(By.cssSelector("#widget-1_archives-__i__ > div:nth-child(1) > div:nth-child(2) > h4:nth-child(1)")).sendKeys(" ");
    	Thread.sleep(1000);
    }

    @Test
    public void testFocusPreview() throws InterruptedException {
        openUrl("http://localhost/ex/wp/wp-admin/customize.php?theme=twentyfourteen");
    	handleLogin();
    	Thread.sleep(5000);
    	getDriver().switchTo().frame(findElement(By.cssSelector("iframe")));
    	for (int i = 0; i < 100; i++) {
    		new Actions(getDriver()).sendKeys(Keys.DOWN).perform();
    	}
    	new Actions(getDriver()).moveToElement(findElement(By.cssSelector("#archives-8 > h1:nth-child(1)"))).perform();
    	Thread.sleep(1000);
    }
    
    private void waitAndClick(String sel) {
    	waitUntil(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(sel)));
    	findElement(By.cssSelector(sel)).click();
    }
    /**
     * attempt login if user is current logged out.
     */
    private void handleLogin() {
        if (findElements(By.id("loginform")).isEmpty()) {
            // User is already logged in.
            return;
        }
        findElement(By.id("user_login")).clear();
        findElement(By.id("user_login")).sendKeys("admin");
        findElement(By.id("user_pass")).clear();
        findElement(By.id("user_pass")).sendKeys("admin");
        if (!findElement(By.id("rememberme")).isSelected()) {
            findElement(By.id("rememberme")).click();
        }
        findElement(By.id("wp-submit")).click();
    }

    @Override
    protected String getOriginalFileRoot() {
        return "/var/www/ex/wp/wp-content/plugins/wp-widget-customizer";
    }

    @Override
    protected String getInstrumentedFileRoot() {
        return "/var/www/ex/js-cover/instrumented/wp/wp-content/plugins/wp-widget-customizer";
    }

    @Override
    protected String[] getExtraJsCoverArgs() {
        return new String[] {"--only-instrument-reg=.*widget-customizer.*"};
    }

    @Override
    protected String getInstrumentedUrl(String url) {
        return "http://localhost/ex/js-cover/instrumented/wp/wp-admin/customize.php?theme=twentyfourteen";
    }

    @Override
    protected String getJsCoverageUrl() {
        return "http://localhost/ex/js-cover/instrumented/wp/wp-content/plugins/wp-widget-customizer/jscoverage.html";
    }
}
