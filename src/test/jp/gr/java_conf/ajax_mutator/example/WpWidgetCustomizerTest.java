package jp.gr.java_conf.ajax_mutator.example;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.google.common.base.Predicate;

public class WpWidgetCustomizerTest extends TestBase {
	public WpWidgetCustomizerTest() {
		super(/* calc coverage */false, /* run in background */false);
	}

	void clickandwait(WebElement e) {
		waitUntil(ExpectedConditions.visibilityOf(e));
		e.click();
		_wait(1000);
	}

	void _wait() {
		_wait(2000);
	}

	void _wait(int m) {
		try {
			Thread.sleep(m);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	@Test
	public void test_click_open() {
		on_start();
		String[] xpaths = { "//*[@id=\"customize-info\"]/div[1]/span/strong",
				"//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/h3",
				"//*[@id=\"accordion-section-title_tagline\"]/h3",
				"//*[@id=\"accordion-section-colors\"]/h3",
				"//*[@id=\"accordion-section-background_image\"]/h3",
				"//*[@id=\"accordion-section-static_front_page\"]/h3",
				"//*[@id=\"accordion-section-featured_content\"]/h3" };
		String[] vxpaths = { "//*[@id=\"customize-info\"]",
				"//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]",
				"//*[@id=\"accordion-section-title_tagline\"]",
				"//*[@id=\"accordion-section-colors\"]",
				"//*[@id=\"accordion-section-background_image\"]",
				"//*[@id=\"accordion-section-static_front_page\"]",
				"//*[@id=\"accordion-section-featured_content\"]" };

		for (int i = 0; i < xpaths.length; i++) {
			String xpath = xpaths[i];
			String vxpath = vxpaths[i];
			WebElement e = _e(xpath);
			clickandwait(e);
			WebElement ev = _e(vxpath);
			assertEquals(ev.getAttribute("class").contains("open"), true);
			clickandwait(e);
			assertEquals(ev.getAttribute("class").contains("open"), false);
		}
	}

	@Test
	public void test_click_open_widget() {
		on_start();
		test_click_open_widget_helper("//*[@id=\"accordion-section-sidebar-widgets-sidebar-3\"]/h3");
		//test_click_open_widget_helper("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/h3");
	}

	public void test_click_open_widget_helper(String xpath) {
		WebElement widgetlabel = _e(xpath);
		clickandwait(widgetlabel);
		WebElement title = _e("//*[@id=\"widget-14_archives-8\"]/div[1]/div[2]/h4");
		clickandwait(title);
		assertEquals(_e("//*[@id=\"widget-14_archives-8\"]/div[2]").isDisplayed(),true);
		clickandwait(title);
		assertEquals(_e("//*[@id=\"widget-14_archives-8\"]/div[2]").isDisplayed(),false);
		WebElement smalltri = _e("//*[@id=\"widget-14_archives-8\"]/div[1]/div[1]");
		clickandwait(smalltri);
		assertEquals(_e("//*[@id=\"widget-14_archives-8\"]/div[2]").isDisplayed(),true);
		clickandwait(smalltri);
		assertEquals(_e("//*[@id=\"widget-14_archives-8\"]/div[2]").isDisplayed(),false);

		clickandwait(smalltri);
		assertEquals(_e("//*[@id=\"widget-14_archives-8\"]/div[2]").isDisplayed(),true);
		clickandwait(_e("//*[@id=\"widget-14_archives-8\"]/div[2]/div/div[2]/div[1]/a[2]"));
		assertEquals(_e("//*[@id=\"widget-14_archives-8\"]/div[2]").isDisplayed(),false);
		
		
		
//		Iterator<WebElement> vit = findElements(
//				By.className("widget-inside")).iterator();
//		Iterator<WebElement> it1 = findElements(
//				By.className("widget-title")).iterator();
//		Iterator<WebElement> it2 = findElements(
//				By.className("widget-title-action")).iterator();
//		Iterator<WebElement> itclose = widgetlabel.findElements(By.className("widget-control-close")).iterator();
//		while (true) {
//			WebElement e1 = it1.next();
//			clickandwait(e1);
//			WebElement ve = vit.next();
//			assertEquals(ve.getAttribute("display").contains("block"), true);
//			clickandwait(e1);
//			assertEquals(ve.getAttribute("display").contains("block"), false);
//			
//			clickandwait(e1);
//			assertEquals(ve.getAttribute("display").contains("block"), true);
//			clickandwait(itclose.next());
//			assertEquals(ve.getAttribute("display").contains("block"), false);
//			
//			WebElement e2 = it2.next();
//			clickandwait(e2);
//			assertEquals(ve.getAttribute("display").contains("block"), true);
//			clickandwait(e2);
//			assertEquals(ve.getAttribute("display").contains("block"), false);
//			break;
//		}
	}

	@Test
	public void test_archive_update() {
		on_start();
		clickandwait(findElement(By
				.xpath("//*[@id=\"accordion-section-sidebar-widgets-sidebar-3\"]/h3")));
		clickandwait(findElement(By
				.xpath("//*[@id=\"widget-14_archives-8\"]/div[1]/div[2]")));
		findElement(By.xpath("//*[@id=\"widget-archives-8-title\"]")).click();
		getDriver().switchTo().frame(findElement(By.id("customize-preview")).findElement(By.tagName("iframe")));
		waitUntil(new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver input) {
				return input.findElement(By.xpath("//*[@id=\"archives-8\"]"))
						.getAttribute("class")
						.contains("widget-customizer-highlighted-widget");
			}
		});
		waitUntil(new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver input) {
				return !input.findElement(By.xpath("//*[@id=\"archives-8\"]"))
						.getAttribute("class")
						.contains("widget-customizer-highlighted-widget");
			}
		});
		List<WebElement> data = findElement(
				By.xpath("//*[@id=\"archives-8\"]/ul/li")).findElements(
				By.tagName("a"));
		ArrayList<String> hrefs = new ArrayList<>(data.size());
		ArrayList<String> texts = new ArrayList<>(data.size());
		for (WebElement e : data) {
			hrefs.add(e.getAttribute("href"));
			texts.add(e.getText());
		}
		backToRootFrame();
		WebElement title = findElement(By
				.xpath("//*[@id=\"widget-archives-8-title\"]"));
		title.clear();
		title.sendKeys("new_archive");
		findElement(By.xpath("//*[@id=\"widget-archives-8-dropdown\"]"))
				.click();
		findElement(By.xpath("//*[@id=\"widget-archives-8-count\"]")).click();
			
		findElement(By.xpath("//*[@id=\"widget-archives-8-savewidget\"]"))
				.click();
		_wait();
		getDriver().switchTo().frame(findElement(By.id("customize-preview")).findElement(By.tagName("iframe")));
		assertEquals(findElement(By.xpath("//*[@id=\"archives-8\"]/h1"))
				.getText(), "new_archive".toUpperCase());
		assertEquals(findElement(By.xpath("//*[@id=\"archives-8\"]/select"))
				.getAttribute("name"), "archive-dropdown");
		List<WebElement> data2 = findElement(
				By.xpath("//*[@id=\"archives-8\"]/select")).findElements(
				By.tagName("option"));

		Iterator<String> i11 = hrefs.iterator();
		Iterator<String> i12 = texts.iterator();
		Iterator<WebElement> i2 = data2.iterator();
		i2.next();
		while (i11.hasNext() && i12.hasNext() && i2.hasNext()) {
			WebElement d2 = i2.next();
			assertEquals(i11.next(), d2.getAttribute("value"));
			String text = i12.next();
			String text2 = d2.getText().trim();
			assertEquals(text2.startsWith(text + "  ("), true);
			assertEquals(text2.endsWith(")"), true);
		}
	}
	
	//broken@Test
	public void test_drag(){
		on_start();
		clickandwait(_e("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/h3"));
		WebElement e = _e("//*[@id=\"customize-control-widget_archives-6\"]");
		Actions action = new Actions(getDriver());
		WebElement _se = _e("//*[@id=\"widget-15_archives-6\"]/div[1]/div[1]");
		action.moveToElement(_se);
		action.clickAndHold(_se);
		action.pause(3000);
		action.moveByOffset(_se.getLocation().x+_se.getSize().width/2,_se.getLocation().y+_se.getSize().height/2+e.getSize().height*3/2);
		action.pause(3000);
		action.release();
		action.pause(3000);
//		action.moveToElement(e);
//		action.dragAndDropBy(e, 0, e.getSize().height*3/2);
		action.perform();
		Iterator<WebElement> i = _e("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/ul").findElements(By.tagName("li")).iterator();
		i.next();
		assertEquals(i.next().getAttribute("id"),"customize-control-widget_pages-3");
		assertEquals(i.next().getAttribute("id"),"customize-control-widget_archives-6");
		
		getDriver().switchTo().frame(findElement(By.id("customize-preview")).findElement(By.tagName("iframe")));
		i = _e("//*[@id=\"primary-sidebar\"]").findElements(By.tagName("aside")).iterator();
		assertEquals(i.next().getAttribute("id"),"pages-3");
		assertEquals(i.next().getAttribute("id"),"archives-6");

		backToRootFrame();

		e = _e("//*[@id=\"customize-control-widget_archives-6\"]");
		action.dragAndDropBy(e, 0, -e.getSize().height*3/2);
		action.perform();
		i = _e("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/ul").findElements(By.tagName("li")).iterator();
		i.next();
		assertEquals(i.next().getAttribute("id"),"customize-control-widget_archives-6");
		assertEquals(i.next().getAttribute("id"),"customize-control-widget_pages-3");
		getDriver().switchTo().frame(findElement(By.id("customize-preview")).findElement(By.tagName("iframe")));
		i = _e("//*[@id=\"primary-sidebar\"]").findElements(By.tagName("aside")).iterator();
		assertEquals(i.next().getAttribute("id"),"archives-6");
		assertEquals(i.next().getAttribute("id"),"pages-3");
	}

	@Test
	public void test_save(){
		on_start();
		clickandwait(_e("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/h3"));
		clickandwait(_e("//*[@id=\"widget-18_archives-2\"]/div[1]/div[2]"));
		WebElement e = _e("//*[@id=\"widget-archives-2-title\"]");
		e.clear();
		e.sendKeys("abc");
		clickandwait(_e("//*[@id=\"widget-archives-2-savewidget\"]"));
		WebElement save = _e("//*[@id=\"save\"]");
		assertEquals(save.getAttribute("value"),"Save & Publish");
		save.click();
		_wait(2000);
		assertEquals(save.getAttribute("value"),"Saved");

		openUrl("http://localhost/ex/wp/wp-admin/customize.php?theme=twentyfourteen");
		_wait(3000);
		getDriver().switchTo().frame(findElement(By.id("customize-preview")).findElement(By.tagName("iframe")));
		assertEquals(_e("//*[@id=\"archives-2\"]/h1").getText(),"ABC");

		backToRootFrame();
		
		clickandwait(_e("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/h3"));
		clickandwait(_e("//*[@id=\"widget-18_archives-2\"]/div[1]/div[2]"));
		e = _e("//*[@id=\"widget-archives-2-title\"]");

		e.clear();
		clickandwait(_e("//*[@id=\"widget-archives-2-savewidget\"]"));
		save = _e("//*[@id=\"save\"]");
		assertEquals(save.getAttribute("value"),"Save & Publish");
		save.click();
		_wait(2000);
		assertEquals(save.getAttribute("value"),"Saved");
	}
	
	@Test
	public void test_add_a_widget(){
		on_start();
		clickandwait(_e("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/h3"));
		WebElement add = _e("//*[@id=\"customize-control-sidebars_widgets-sidebar-1\"]/span/b");
		add.sendKeys(" ");
		_wait(1000);
		assertEquals(_e("/html/body").getAttribute("class").contains("adding-widget"),true);
		add.sendKeys(" ");
		_wait(1000);
		assertEquals(_e("/html/body").getAttribute("class").contains("adding-widget"),false);
		
		add.sendKeys("\n");
		_wait(1000);
		assertEquals(_e("/html/body").getAttribute("class").contains("adding-widget"),true);
		add.sendKeys("\n");
		_wait(1000);
		assertEquals(_e("/html/body").getAttribute("class").contains("adding-widget"),false);
		
		add.click();
		_wait(1000);
		assertEquals(_e("/html/body").getAttribute("class").contains("adding-widget"),true);
		add.click();
		_wait(1000);
		assertEquals(_e("/html/body").getAttribute("class").contains("adding-widget"),false);
		add.click();
		_wait(1000);
		
		WebElement search = _e("//*[@id=\"available-widgets-filter\"]/input");
		search.click();
		search.clear();
		search.sendKeys("ch");
		_wait(1000);
		assertEquals(_e("//*[@id=\"widget-tpl-archives-5\"]").getAttribute("class").contains("selected"),true);
		assertEquals(_e("//*[@id=\"widget-tpl-calendar-1\"]").getAttribute("class").contains("selected"),false);
		assertEquals(_e("//*[@id=\"widget-tpl-search-2\"]").getAttribute("class").contains("selected"),false);
		assertEquals(_e("//*[@id=\"widget-tpl-archives-5\"]").isDisplayed(),true);
		assertEquals(_e("//*[@id=\"widget-tpl-calendar-1\"]").isDisplayed(),false);
		assertEquals(_e("//*[@id=\"widget-tpl-search-2\"]").isDisplayed(),true);
		search.sendKeys("\t\t");
		_wait(1000);
		assertEquals(_e("//*[@id=\"widget-tpl-archives-5\"]").getAttribute("class").contains("selected"),false);
		assertEquals(_e("//*[@id=\"widget-tpl-search-2\"]").getAttribute("class").contains("selected"),true);
		
		_e("//*[@id=\"widget-tpl-search-2\"]").sendKeys(" ");
		_wait(2000);
		List<WebElement> findElements = _e("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/ul").findElements(By.tagName("li"));
		WebElement last = findElements.get(findElements.size()-2);
		assertEquals(last.getAttribute("id").contains("search"),true);
		_wait(1000);
		_e("//*[@id=\""+last.getAttribute("id")+"\"]/div[1]/div[2]/div/div[2]/div[1]/a[1]").click();
		_wait(1000);
		
		try{
			_e("//*[@id=\"widget-10_search-5\"]");
		}catch(NoSuchElementException e){
			add.click();
			_wait(1000);
			_e("//*[@id=\"widget-tpl-search-2\"]").click();
			findElements = _e("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/ul").findElements(By.tagName("li"));
			last = findElements.get(findElements.size()-2);
			assertEquals(last.getAttribute("id").contains("search"),true);
			_wait(1000);
			_e("//*[@id=\""+last.getAttribute("id")+"\"]/div[1]/div[2]/div/div[2]/div[1]/a[1]").click();
			_wait(1000);
			return;
		}
		assertEquals(true,false);
	}
	
	private WebElement _e(String string) {
		return findElement(By.xpath(string));
	}
	private void on_start() {
		openUrl("http://localhost/ex/wp/wp-admin/customize.php?theme=twentyfourteen");
		handleLogin();
		_wait();
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
		return new String[] { "--only-instrument-reg=.*widget-customizer.*" };
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
