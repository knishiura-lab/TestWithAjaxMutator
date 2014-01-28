package jp.gr.java_conf.ajax_mutator.example;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.google.common.base.Predicate;

public class WpWidgetCustomizerTest extends TestBase {
	public WpWidgetCustomizerTest() {
		super(/* calc coverage */true, /* run in background */false);
	}

	void clickandwait(WebElement e) {
		waitUntil(ExpectedConditions.visibilityOf(e));
		e.click();
		_wait(500);
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
			WebElement e = findElement(By.xpath(xpath));
			clickandwait(e);
			WebElement ev = findElement(By.xpath(vxpath));
			assertEquals(ev.getAttribute("class").contains("open"), true);
			clickandwait(e);
			assertEquals(ev.getAttribute("class").contains("open"), false);
		}
	}

	@Test
	public void test_click_open_widget() {
		on_start();
		test_click_open_widget_helper("//*[@id=\"accordion-section-sidebar-widgets-sidebar-3\"]/h3");
		test_click_open_widget_helper("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/h3");
	}

	public void test_click_open_widget_helper(String xpath) {
		WebElement widgetlabel = findElement(By.xpath(xpath));
		clickandwait(widgetlabel);
		Iterator<WebElement> vit = widgetlabel.findElements(
				By.className("widget-inside")).iterator();
		Iterator<WebElement> it1 = widgetlabel.findElements(
				By.className("widget-title")).iterator();
		Iterator<WebElement> it2 = widgetlabel.findElements(
				By.className("widget-title-action")).iterator();
		while (it1.hasNext() && it2.hasNext() && vit.hasNext()) {
			WebElement e1 = it1.next();
			clickandwait(e1);
			WebElement ve = vit.next();
			assertEquals(ve.getAttribute("display").contains("block"), true);
			clickandwait(e1);
			assertEquals(ve.getAttribute("display").contains("block"), false);
			WebElement e2 = it2.next();
			clickandwait(e2);
			assertEquals(ve.getAttribute("display").contains("block"), true);
			clickandwait(e2);
			assertEquals(ve.getAttribute("display").contains("block"), false);
		}
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
