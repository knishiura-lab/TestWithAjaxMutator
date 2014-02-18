package jp.gr.java_conf.ajax_mutator.example;

import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class WpWidgetCustomizerTest extends TestBase {

	public WpWidgetCustomizerTest() {
		super(/* calc coverage */true, /* run in background */false);
	}

	@Test
	public void testSidebarWidget() throws InterruptedException {
		openUrl("http://localhost/ex/wp/wp-admin/customize.php?theme=twentyfourteen");
		handleLogin();
		click("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/h3");

		List<WebElement> firstWidgets = findElements(By
				.xpath("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/ul/li"));
		int firstLength = firstWidgets.size() - 2;
		addWidget("//*[@id=\"widget-7_recent-comments-__i__\"]/div[1]/div[2]/h4");
		addWidget("//*[@id=\"widget-8_recent-posts-__i__\"]/div[1]/div[2]/h4");

		addWidget("//*[@id=\"widget-10_search-__i__\"]/div[1]/div[2]/h4");

		List<WebElement> addedWidgets = findElements(By
				.xpath("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/ul/li"));
		for (int i = firstLength + 1; i < addedWidgets.size() - 1; i++) {
			WebElement addedWidget = addedWidgets.get(i);
			if (!addedWidget.getAttribute("style").contains("none")) {
				closeWidgetWithArrow(addedWidget);
				openWidgetWithArrow(addedWidget);
				updateWidget(addedWidget);
				closeWidget(addedWidget);
				mouseOver(addedWidget);

				WebElement addedWidget1;
				if (i > firstLength + 1) {
					addedWidget1 = addedWidgets.get(i - 1);
					dragAndDrop(addedWidget, addedWidget1);
				} else {

				}
			}
		}

		getDriver().switchTo().frame(
				findElement(By.xpath("//*[@id=\"customize-preview\"]/iframe")));
		List<WebElement> asides = findElements(By.tagName("aside"));
		try {
			for (WebElement aside : asides) {
				if (aside.getAttribute("id").contains("recent-posts")) {
					click(aside, "ul/li/a");
				} else if (aside.getAttribute("id").contains("search")) {
					WebElement input = aside.findElement(By
							.xpath("form/label/input"));
					input.sendKeys("title");
					Thread.sleep(1000);
					input.sendKeys("\n");
					Thread.sleep(1000);
				} else if (aside.getAttribute("id").contains("recent-")) {
					click(aside, "ul/li/a[1]");
					click(aside, "ul/li/a[2]");
				}
			}
		} catch (Exception e) {

		}

		addedWidgets = findElements(By
				.xpath("//*[@id=\"accordion-section-sidebar-widgets-sidebar-1\"]/ul/li"));
		for (int i = 1; i < addedWidgets.size() - 1; i++) {
			WebElement addedWidget = addedWidgets.get(i);
			try {
				if (!addedWidget.getAttribute("style").contains("none")) {
					closeWidgetWithArrow(addedWidget);
					openWidgetWithArrow(addedWidget);
					removeWidget(addedWidget);
				}
			} catch (Exception e) {

			}
		}
		System.out.println();

	}

	public void removeWidget(WebElement widget) throws InterruptedException {
		Thread.sleep(1000);
		WebElement remove = widget.findElement(By
				.xpath("div/div[2]/div[1]/div[2]/div[1]/a[1]"));
		remove.click();
	}

	public void clickDocumentAside(WebElement aside) {

	}

	public void dragAndDrop(WebElement widget, WebElement widget1)
			throws InterruptedException {
		Thread.sleep(1000);
		(new Actions(getDriver())).dragAndDrop(widget, widget1).perform();
		Thread.sleep(1000);
	}

	public void mouseOver(WebElement widget) {
		WebElement widgetTop = widget.findElement(By.xpath("div/div[1]"));
		Actions action = new Actions(getDriver());
		action.moveToElement(widgetTop);
		action.pause(1000);
		action.perform();
	}

	public void click(String xpath) throws InterruptedException {
		WebElement element = waitUntil(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
		element.click();
	}

	public void click(WebElement elem, String xpath)
			throws InterruptedException {
		Thread.sleep(1000);
		WebElement ele = elem.findElement(By.xpath(xpath));
		ele.click();
	}

	public void addWidget(String xpath) throws InterruptedException {
		click("//*[@id=\"customize-control-sidebars_widgets-sidebar-1\"]/span");
		click(xpath);
	}

	public void updateWidget(WebElement widget) throws InterruptedException {
		Thread.sleep(1000);
		String id = widget.getAttribute("id");
		WebElement title, article, visible, visible1, visible2, comment;
		if (id.contains("search")) {
			WebElement input = widget.findElement(By
					.xpath("div/div[2]/div[1]/div[1]/p/label/input"));
			input.sendKeys("search");
		} else if (id.contains("recent-posts")) {
			title = widget.findElement(By
					.xpath("div/div[2]/div[1]/div[1]/p[1]/input"));
			title.sendKeys("title");
			article = widget.findElement(By
					.xpath("div/div[2]/div[1]/div[1]/p[2]/input"));
			article.sendKeys("0");
			visible = widget.findElement(By
					.xpath("div/div[2]/div[1]/div[1]/p[3]/input"));
			visible.click();
		} else if (id.contains("recent-comments")) {
			title = widget.findElement(By
					.xpath("div/div[2]/div[1]/div[1]/p[1]/input"));
			title.sendKeys("title");
			comment = widget.findElement(By
					.xpath("div/div[2]/div[1]/div[1]/p[2]/input"));
			comment.sendKeys("0");
		} else if (id.contains("archives")) {
			title = widget.findElement(By
					.xpath("div/div[2]/div[1]/div[1]/p[1]/input"));
			title.sendKeys("title");
			visible = widget.findElement(By
					.xpath("div/div[2]/div[1]/div[1]/p[2]/input[1]"));
			visible.click();
			visible1 = widget.findElement(By
					.xpath("div/div[2]/div[1]/div[1]/p[2]/input[2]"));
			visible1.click();
		} else if (id.contains("categories")) {
			/*
			 * title = widget.findElement(By
			 * .xpath("div/div[2]/div[1]/div[1]/p[1]/input"));
			 * title.sendKeys("title"); visible = widget.findElement(By
			 * .xpath("div/div[2]/div[1]/div[1]/p[2]/input[1]"));
			 * visible.click(); visible1 = widget.findElement(By
			 * .xpath("div/div[2]/div[1]/div[1]/p[2]/input[2]"));
			 * visible1.click(); visible2 = widget.findElement(By
			 * .xpath("div/div[2]/div[1]/div[1]/p[2]/input[3]"));
			 * visible2.click();
			 */
		} else if (id.contains("meta")) {
			/*
			 * title = widget.findElement(By
			 * .xpath("div/div[2]/div[1]/div[1]/p/input"));
			 * title.sendKeys("title");
			 */
		} else if (id.contains("text")) {
			title = widget.findElement(By
					.xpath("div/div[2]/div[1]/div[1]/p[1]/input"));
			title.sendKeys("title");
			article = widget.findElement(By
					.xpath("div/div[2]/div[1]/div[1]/textarea"));
			article.sendKeys("0");
			visible = widget.findElement(By
					.xpath("div/div[2]/div[1]/div[1]/p[2]/input"));
			visible.click();
		} else if (id.contains("rss")) {

		} else if (id.contains("twentyfourteen_ephemera")) {

		} else if (id.contains("nav_menu")) {

		} else if (id.contains("calendar")) {

		} else if (id.contains("tag_cloud")) {

		} else if (id.contains("pages")) {

		}

		WebElement update = widget.findElement(By
				.xpath("div/div[2]/div[1]/div[2]/div[2]/input"));
		update.click();
	}

	public void openWidgetWithArrow(WebElement widget)
			throws InterruptedException {
		Thread.sleep(1000);
		WebElement open = widget.findElement(By.xpath("div/div[1]/div[2]/h4"));
		open.click();
	}

	public void closeWidgetWithArrow(WebElement widget)
			throws InterruptedException {
		Thread.sleep(1000);
		WebElement close = widget.findElement(By.xpath("div/div[1]/div[2]/h4"));
		close.click();
	}

	public void closeWidget(WebElement widget) throws InterruptedException {
		// *[@id="widget-14_recent-posts-2"]/div[2]/div/div[2]/div[1]/a[2]
		Thread.sleep(1000);
		WebElement close = widget.findElement(By
				.xpath("div/div[2]/div[1]/div[2]/div[1]/a[2]"));
		close.click();
	}

	public void testTitleTagline() {
		// *[@id="customize-control-sidebars_widgets-sidebar-1"]/span
	}

	public void testColors() {

	}

	public void testBackgroundImage() {

	}

	public void testStaticFrontPage() {

	}

	public void testFeaturedContent() {

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
