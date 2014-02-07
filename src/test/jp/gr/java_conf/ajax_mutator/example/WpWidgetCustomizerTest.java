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
import org.openqa.selenium.interactions.Actions;

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
		text = findDisplayedElement(findElements(By.className("widefat")));
		assertNull(text);

		checkUnmodified();
	}


	@Test
	public void reorderWidgetsInSidebar() throws Exception {

		getDriver().switchTo().frame(findElement(By.tagName("iframe")));
		String firstWidgetTitle = findElement(By.className("widget-title"))
				.getText();

		before();
		WebElement menu = openMenu(1);
		List<WebElement> widgets = getWidgets(menu);
		String firstText = widgets.get(0).getText();
		(new Actions(getDriver())).dragAndDrop(widgets.get(0),
				widgets.get(widgets.size() - 1)).perform();
		sleep(2);

		checkModified();
		widgets = getWidgets(menu);
		assertEquals(firstText, widgets.get(widgets.size() - 2).getText());

		getDriver().switchTo().frame(findElement(By.tagName("iframe")));
		String firstWidgetTitleInPreview = findElement(
				By.className("widget-title")).getText();
		assertFalse(firstWidgetTitle.equals(firstWidgetTitleInPreview));

		before();
		menu = openMenu(1);
		widgets = getWidgets(menu);
		firstText = widgets.get(0).getText();
		(new Actions(getDriver())).dragAndDrop(widgets.get(0),
				widgets.get(widgets.size() - 1)).perform();
		sleep(2);

		save();
		widgets = getWidgets(menu);
		assertEquals(firstText, widgets.get(widgets.size() - 2).getText());
		checkUnmodified();

		getDriver().switchTo().frame(findElement(By.tagName("iframe")));
		assertEquals(firstWidgetTitleInPreview,
				findElement(By.className("widget-title")).getText());
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
		sleep();
		setUniqueText();
	}

	private void sleep(int times) throws InterruptedException {
		Thread.sleep(700 * times);
	}

	private void sleep() throws InterruptedException {
		Thread.sleep(700);
	}

	private List<WebElement> getWidgets(WebElement menu) {
		return menu.findElements(By.className("customize-control-widget_form"));
	}

	private void checkUnmodified() {
		assertEquals("Saved", findElement(By.id("save")).getAttribute("value"));
		assertEquals("Close", findElement(By.className("back")).getText());
	}

	private void checkModified() {
		assertEquals("Save & Publish",
				findElement(By.id("save")).getAttribute("value"));
		assertEquals("Cancel", findElement(By.className("back")).getText());
	}

	private void removeLastWidget(int menuIndex, String uniqueString)
			throws Exception, InterruptedException {
		WebElement menu = openMenu(menuIndex);
		openLastWidget(menu);
		ArrayList<WebElement> removes = findDisplayedElements(findElements(By
				.className("widget-control-remove")));
		removes.get(removes.size() - 1).click();
		sleep();
		assertTextNotExistence(uniqueString);
	}

	private String addArchive(int menuIndex) throws InterruptedException {
		WebElement menu = openMenu(menuIndex);
		menu.findElement(By.className("add-new-widget")).click();
		sleep();
		WebElement finder = findElement(By.id("available-widgets-filter"))
				.findElement(By.tagName("input"));
		finder.sendKeys("arc");
		sleep();

		ArrayList<WebElement> candidateWidgets = findDisplayedElements(findElement(
				By.id("available-widgets")).findElements(
				By.className("widget-tpl")));
		assertEquals(2, candidateWidgets.size());

		findElement(By.id("widget-1_archives-__i__")).click();
		sleep();
		List<WebElement> items = menu.findElements(By.tagName("li"));
		assertEquals("customize-control customize-control-sidebar_widgets",
				items.get(items.size() - 1).getAttribute("class"));
		return setUniqueText();
	}

	private String setUniqueText() throws InterruptedException {
		WebElement text = findDisplayedElement(findElements(By
				.className("widefat")));
		String uniqueString = getUniqueString();
		setText(text, uniqueString);
		updateAndSave();
		assertTextExistence(uniqueString);
		return uniqueString;
	}

	private void openLastWidget(WebElement menu) throws InterruptedException {
		List<WebElement> widgets = getWidgets(menu);
		WebElement widget = widgets.get(widgets.size() - 1);
		WebElement widgetTitle = widget.findElement(By
				.className("widget-title"));
		widgetTitle.click();
		waitUntil(ExpectedConditions.visibilityOf(widget.findElement(By.className("widget-control-save"))));
	}

	private void closeLastWidget(WebElement menu) throws InterruptedException {
		List<WebElement> widgets = getWidgets(menu);
		WebElement widget = widgets.get(widgets.size() - 1);
		WebElement closeButton = widget.findElement(By
				.className("widget-control-close"));
		closeButton.click();
		sleep();
	}

	private WebElement openMenu(int menuIndex) throws InterruptedException {
		WebElement menu = menus.get(menuIndex);
		menu.findElement(By.tagName("h3")).click();
		sleep();
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

	private void updateAndSave() throws InterruptedException {
		List<WebElement> updates = findElements(By.name("savewidget"));
		WebElement update = findDisplayedElement(updates);
		update.click();
		sleep();
		save();
	}

	private void save() throws InterruptedException {
		checkModified();
		findElement(By.id("save")).click();
		sleep();
		checkUnmodified();
	}

	private void setText(WebElement input, String text)
			throws InterruptedException {
		for (int i = 0; i < 20; i++) {
			input.sendKeys(Keys.BACK_SPACE);
		}
		input.sendKeys(text);
		sleep();
	}

	private String getUniqueString() {
		int randomInt = new Random().nextInt();
		return uniqueStringPrefix + randomInt + ":"
				+ System.currentTimeMillis();
	}

	private void assertTextExistence(String text) throws InterruptedException {
		getDriver().switchTo().frame(findElement(By.tagName("iframe")));
		sleep();
		String bodyText = findElement(By.tagName("body")).getText();
		assertTrue("Text not found!", bodyText.contains(text));
	}

	private void assertTextNotExistence(String text)
			throws InterruptedException {
		getDriver().switchTo().frame(findElement(By.tagName("iframe")));
		sleep();
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
