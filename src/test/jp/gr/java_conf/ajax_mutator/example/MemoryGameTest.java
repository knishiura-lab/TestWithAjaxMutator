package jp.gr.java_conf.ajax_mutator.example;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.ajax_mutator.example.TestBase;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class MemoryGameTest extends TestBase {
	public MemoryGameTest() {
		super(
		/* calc coverage */true,
		/* run in background */false);
	}

	@Before
	public void before() {
		openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
		sleep(4);
		okIGotIt("instructions");
		noKeepItQuietPlease();
		startGame();
		// check whether an embed tag for playing a BGM doesn't exists
		try {
			findElement(By.tagName("embed"));
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void playBgm() {
		openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
		sleep(4);
		okIGotIt("instructions");
		yesListenAndPlay();
		startGame();
		// check whether an embed tag for playing a BGM exists
		findElement(By.tagName("embed"));
	}

	@Test
	public void cheatToShowImages() {
		List<WebElement> boxes = getDisplayedBoxes();
		for (int i = 0; i < boxes.size(); i++) {
			boxes.get(0).click();
			sleepForAMoment();
			boxes.get(i).click();
			sleepForAMoment();
			sleep();
		}
		sleep();
		assertThat(IsSolved(boxes.get(0)), is(true));

		cheat();
		assertThat(IsSolved(boxes.get(0)), is(true));
		assertThat(IsSolved(boxes.get(boxes.size() - 1)), is(true));
		waitForHidingAllImages();

		assertThat(IsSolved(boxes.get(0)), is(true));
	}

	@Test
	public void loadUserImagesAfterStarting() {
		WebElement keywordsText = findElement(By.id("imageKeywords"));
		WebElement loadButton = findElement(By.id("loadUserImages"));
		assertThat(loadButton.isDisplayed(), is(false));
		keywordsText.sendKeys("4");
		sleep();
		assertThat(loadButton.isDisplayed(), is(false));

		keywordsText.sendKeys("2");
		sleep();
		assertThat(loadButton.isDisplayed(), is(true));

		loadButton.click();
		sleep();
		assertThat(getStatus(), is("Searching images..."));

		sleep(10);
		assertThat(getStatus().trim(), is(""));

		loadButton.click();
		sleep();
		assertThat(
				getStatus(),
				is("The word you entered is being used by this program, please type a different word"));

		stopGame();
		startGame();
	}

	@Test
	public void failToLoadUserImagesAfterStarting() {
		WebElement keywordsText = findElement(By.id("imageKeywords"));
		WebElement loadButton = findElement(By.id("loadUserImages"));
		keywordsText
				.sendKeys("ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooops");

		loadButton.click();
		sleep();
		assertThat(getStatus(), is("Searching images..."));

		sleep(10);
		assertThat(getStatus(), is("Searching images..."));

		stopGame();
		assertThat(getStatus(), is("Searching images... Game stopped!"));
	}

	@Test
	public void solveWithNoMistake() {
		List<WebElement> boxes = getDisplayedBoxes();
		cheat();
		List<String> styles = new ArrayList<String>();
		for (WebElement box : boxes) {
			styles.add(box.getAttribute("style"));
		}
		waitForHidingAllImages();
		for (int i = 0; i < boxes.size(); i++) {
			for (int j = i + 1; j < boxes.size(); j++) {
				if (IsSolved(boxes.get(i))) {
					break;
				}
				if (IsSolved(boxes.get(j))) {
					continue;
				}
				if (!styles.get(i).equals(styles.get(j))) {
					continue;
				}
				boxes.get(j).click();
				sleepForAMoment();
				boxes.get(i).click();
				sleepForAMoment();
				sleep();
			}
		}
		assertThat(getTimeElapsed(), greaterThan(0));
		assertThat(getClickCount(), is(20));
		assertThat(getMatchCount(), is(20));
		assertThat(getTotalTimeElapsed(), greaterThan(0.0));
		assertThat(getScore(), is(100.0));
	}

	@Test
	public void clickAllPairs() {
		assertThat(getTimeElapsed(), greaterThanOrEqualTo(0));
		assertThat(getClickCount(), is(0));
		assertThat(getMatchCount(), is(0));
		int clickedCount = 0, matchCount = 0;
		List<WebElement> boxes = getDisplayedBoxes();
		assertThat(boxes.size(), is(20));
		for (int i = 0; i < boxes.size(); i++) {
			for (int j = i + 1; j < boxes.size(); j++) {
				if (IsSolved(boxes.get(i))) {
					break;
				}
				if (IsSolved(boxes.get(j))) {
					continue;
				}
				boxes.get(j).click();
				sleepForAMoment();
				assertThat(getClickCount(), is(++clickedCount));
				boxes.get(j).click();
				boxes.get(i).click();
				sleepForAMoment();
				assertThat(getClickCount(), is(++clickedCount));
				String status = getStatus();
				if (!status.equals("Sorry, try again!")) {
					matchCount += 2;
					assertThat(getMatchCount(), is(matchCount));
					if (matchCount < 20) {
						assertThat(status,
								is("Congratulations! You have a good memory!"));
					}
				}
				sleep();
			}
		}
		assertThat(getTimeElapsed(), greaterThan(0));
		assertThat(getMatchCount(), is(20));
		assertThat(getTotalTimeElapsed(), greaterThan(0.0));
		assertThat(getScore(), greaterThan(0.0));
		assertThat(getScore(), lessThanOrEqualTo(100.0));
		okIGotIt("gameOver");
		try {
			getDisplayedButtonSet("gameOver");
			fail();
		} catch (Error e) {
		}
	}

	private void sleepForAMoment() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
	}

	private void sleep() {
		sleep(1);
	}

	private void sleep(int times) {
		try {
			Thread.sleep(500 * times);
		} catch (InterruptedException e) {
		}
	}

	private void yesListenAndPlay() {
		WebElement button = getDisplayedButtonSet("musicConfirmation")
				.findElements(By.tagName("button")).get(0);
		assertEquals("Yes, Listen and Play", button.getText());
		button.click();
		sleep(2);
	}

	private void noKeepItQuietPlease() {
		WebElement button = getDisplayedButtonSet("musicConfirmation")
				.findElements(By.tagName("button")).get(1);
		assertEquals("No, Keep It Quiet Please", button.getText());
		button.click();
		sleep(2);
	}

	private void okIGotIt(String id) {
		WebElement button = getDisplayedButtonSet(id).findElement(
				By.tagName("button"));
		assertEquals("OK, I Got It", button.getText());
		button.click();
		sleep(2);
	}

	private WebElement getDisplayedButtonSet(String id) {
		WebElement element = findElement(By.id(id));
		assertThat(element.isDisplayed(), is(true));
		return findDisplayedElements(
				findElements(By.className("ui-dialog-buttonset"))).get(0);
	}

	private void startGame() {
		WebElement button = findElement(By.id("start_stop"));
		assertEquals("Start Game", button.getText());
		button.click();
		sleep();
		assertThat(getWaitTime(), greaterThanOrEqualTo(0.0));
		List<WebElement> boxes = getDisplayedBoxes();
		assertThat(IsSolved(boxes.get(0)), is(true));
		assertThat(IsSolved(boxes.get(boxes.size() - 1)), is(true));
		waitForHidingAllImages();
	}

	private void waitForHidingAllImages() {
		int count = 0;
		List<WebElement> boxes = getDisplayedBoxes();
		boolean firstTime = true;
		while (getWaitTime() > 0.0 || !existsUnsolved()) {
			if (firstTime) {
				boxes.get(0).click();
				boxes.get(1).click();
				firstTime = false;
			}
			sleep();
			assertThat(count, lessThan(100));
		}
	}

	private boolean existsUnsolved() {
		List<WebElement> boxes = getDisplayedBoxes();
		return !IsSolved(boxes.get(0)) || !IsSolved(boxes.get(1))
				|| !IsSolved(boxes.get(boxes.size() - 1));
	}

	private String getStatus() {
		return findElement(By.id("status")).getText();
	}

	private void stopGame() {
		WebElement button = findElement(By.id("start_stop"));
		assertEquals("Stop Game", button.getText());
		button.click();
		sleep();
		assertThat(getStatus(), endsWith("Game stopped!"));
	}

	private void cheat() {
		WebElement button = findElement(By.id("cheat"));
		assertThat(button.isDisplayed(), is(true));
		button.click();
		sleep();
	}

	private double getWaitTime() {
		String[] strs = findElement(By.id("status")).getText().split(" ");
		try {
			if (strs.length >= 6) {
				return Double.parseDouble(strs[5]);
			}
		} catch (Exception e) {
		}
		return 0;
	}

	private double getTotalTimeElapsed() {
		String str = findElement(By.id("timeElapsed")).getText();
		return Double.parseDouble(str);
	}

	private double getScore() {
		String str = findElement(By.id("positivePercentage")).getText();
		return Double.parseDouble(str);
	}

	private int getTimeElapsed() {
		String str = findElement(By.id("timer")).getText().split(" ")[2];
		return Integer.parseInt(str);
	}

	private int getClickCount() {
		String[] strs = findElement(By.id("clickCount")).getText().split("\\s");
		return Integer.parseInt(strs[strs.length - 1]);
	}

	private int getMatchCount() {
		String[] strs = findElement(By.id("matchCount")).getText().split("\\s");
		return Integer.parseInt(strs[strs.length - 1]);
	}

	private List<WebElement> getDisplayedBoxes() {
		return findDisplayedElements(findElements(By.name("boxName")));
	}

	private boolean IsSolved(WebElement e) {
		return e.getAttribute("style").contains("background-image: url(\"http");
	}

	private List<WebElement> findDisplayedElements(Iterable<WebElement> elements) {
		ArrayList<WebElement> ret = new ArrayList<WebElement>();
		for (WebElement e : elements) {
			if (e.isDisplayed()) {
				ret.add(e);
			}
		}
		return ret;
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
		return new String[] { "--no-instrument-reg=.*jquery.*" };
	}

	@Override
	protected String getInstrumentedUrl(String url) {
		return url.replace("localhost/ex/p3.monkeyaround.biz",
				"localhost/ex/js-cover/instrumented/p3.monkeyaround.biz");
	}

	@Override
	protected String getJsCoverageUrl() {
		return "http://localhost/ex/js-cover/instrumented/p3.monkeyaround.biz/jscoverage.html";
	}
}
