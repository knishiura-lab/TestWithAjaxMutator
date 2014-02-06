package jp.gr.java_conf.ajax_mutator.example;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

public class WpWidgetCustomizerTest extends TestBase {
	private List<WebElement> menus;
	private static final String uniqueStringPrefix = "SUPERUNIQUE";

	public WpWidgetCustomizerTest() {
		super(/* calc coverage */false, /* run in background */true);
	}

	@Before
	public void before() throws Exception {
		openUrl("http://localhost/ex/wp/wp-admin/customize.php?theme=twentyfourteen");
		handleLogin();
		final WebElement parent = findElement(By.id("customize-theme-controls"));
		new WebDriverWait(getDriver(), 15).until(new Predicate<WebDriver>() {
			@Override
			public boolean apply(WebDriver driver) {
				List<WebElement> menuCandidates = parent.findElements(By
						.className("accordion-section"));
				return menuCandidates.get(0).getText().contains("Widgets");
			}
		});
		List<WebElement> menuCandidates = parent.findElements(By
				.className("accordion-section"));
		menus = findDisplayedElements(menuCandidates);
	}

	@Test
	public void openAndCloseWidgets() throws Exception {
		WebElement menu = openMenu(0);
		openLastWidget(menu);
		WebElement text = findDisplayedElement(findElements(By
				.className("widefat")));
		assertNotNull(text);
		
		closeLastWidget(menu);
		text = findDisplayedElement(findElements(By
				.className("widefat")));
		assertNull(text);
	}

	@Test
	public void addAndRemoveArchiveInSidebar() throws Exception {
		String uniqueString = addArchive(1);
		before();
		removeLastWidget(1, uniqueString);
	}

	@Test
	public void addAndRemoveArchiveInFooter() throws Exception {
		String uniqueString = addArchive(0);
		before();
		removeLastWidget(0, uniqueString);
	}

	@Test
	public void changeFooterArchive() throws InterruptedException {
		WebElement menu = openMenu(0);
		openLastWidget(menu);
		Thread.sleep(1000);
		setUniqueText();
	}

	private void removeLastWidget(int menuIndex, String uniqueString)
			throws Exception, InterruptedException {
		WebElement menu = openMenu(menuIndex);
		openLastWidget(menu);
		ArrayList<WebElement> removes = findDisplayedElements(findElements(By
				.className("widget-control-remove")));
		removes.get(removes.size() - 1).click();
		Thread.sleep(1000);
		assertTextNotExistence(uniqueString);
	}

	private String addArchive(int menuIndex) throws InterruptedException {
		WebElement menu = openMenu(menuIndex);
		menu.findElement(By.className("add-new-widget")).click();
		Thread.sleep(1000);
		findElement(By.id("widget-1_archives-__i__")).click();
		Thread.sleep(1000);
		return setUniqueText();
	}

	private String setUniqueText() throws InterruptedException {
		WebElement text = findDisplayedElement(findElements(By
				.className("widefat")));
		String uniqueString = getUniqueString();
		setText(text, uniqueString);
		save();
		assertTextExistence(uniqueString);
		return uniqueString;
	}

	private void openLastWidget(WebElement menu) {
		List<WebElement> widgets = menu.findElements(By
				.className("customize-control-widget_form"));
		WebElement widget = widgets.get(widgets.size() - 1);
		WebElement widgetTitle = widget.findElement(By
				.className("widget-title"));
		widgetTitle.click();
		waitUntil(ExpectedConditions.visibilityOf(widget.findElement(By.className("widget-control-save"))));
	}

	private void closeLastWidget(WebElement menu) {
		List<WebElement> widgets = menu.findElements(By
				.className("customize-control-widget_form"));
		WebElement widget = widgets.get(widgets.size() - 1);
		WebElement closeButton = widget.findElement(By
				.className("widget-control-close"));
		closeButton.click();
	}

	private WebElement openMenu(int menuIndex) throws InterruptedException {
		WebElement menu = menus.get(menuIndex);
		menu.findElement(By.tagName("h3")).click();
		Thread.sleep(1000);
		return menu;
	}

	private WebElement findDisplayedElement(Iterable<WebElement> elements) {
		for (WebElement e : elements) {
			if (e.isDisplayed()) {
				return e;
			}
		}
		return null;
	}

	private ArrayList<WebElement> findDisplayedElements(
			Iterable<WebElement> elements) {
		ArrayList<WebElement> list = new ArrayList<WebElement>();
		for (WebElement e : elements) {
			if (e.isDisplayed()) {
				list.add(e);
			}
		}
		return list;
	}

	private void save() throws InterruptedException {
		List<WebElement> updates = findElements(By.name("savewidget"));
		WebElement update = findDisplayedElement(updates);
		update.click();
		Thread.sleep(1000);
		findElement(By.id("save")).click();
		Thread.sleep(1000);
	}

	private void setText(WebElement input, String text)
			throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			input.sendKeys(Keys.BACK_SPACE);
		}
		input.sendKeys(text);
		Thread.sleep(1000);
	}

	private String getUniqueString() {
		int randomInt = new Random().nextInt();
		return uniqueStringPrefix + randomInt + ":"
				+ System.currentTimeMillis();
	}

	private void assertTextExistence(String text) throws InterruptedException {
		getDriver().switchTo().frame(findElement(By.tagName("iframe")));
		Thread.sleep(1000);
		String bodyText = findElement(By.tagName("body")).getText();
		assertTrue("Text not found!", bodyText.contains(text));
	}

	private void assertTextNotExistence(String text)
			throws InterruptedException {
		getDriver().switchTo().frame(findElement(By.tagName("iframe")));
		Thread.sleep(1000);
		String bodyText = findElement(By.tagName("body")).getText();
		assertFalse("Text found!", bodyText.contains(text));
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
