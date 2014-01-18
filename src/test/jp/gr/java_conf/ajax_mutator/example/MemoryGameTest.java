package jp.gr.java_conf.ajax_mutator.example;

import org.junit.Test;
import org.openqa.selenium.By;

public class MemoryGameTest extends TestBase {
    public MemoryGameTest() {
        super(
                /* calc coverage */ true,
                /* run in background*/ false);
    }

    @Test
    public void testSomething() throws InterruptedException {
        // Please implement here
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        // Please implement here
        Thread.sleep(3000);
        findElements(By.className("ui-button")).get(6).click();
        Thread.sleep(3000);
        findElements(By.className("ui-button")).get(6).click();//you have to test another button later
        Thread.sleep(3000);
        findElement(By.id("start_stop")).click();
        findElement(By.id("imageKeywords")).sendKeys("computer");
        Thread.sleep(1000);
        findElement(By.id("loadUserImages")).click();
        Thread.sleep(15000);
        findElement(By.id("cheat")).click();
        for(int i=0;i<20;i++){
            for(int j=i+1;j<20;j++){
                findElement(By.id("box_"+ i)).click();
                findElement(By.id("box_"+ j)).click();
            }
        }
        Thread.sleep(10000);
        findElements(By.className("ui-button")).get(1).click();
        Thread.sleep(4000);
    }

    @Test
    public void testSomething2() throws InterruptedException {
        // Please implement here
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        // Please implement here
        Thread.sleep(3000);
        findElements(By.className("ui-button")).get(6).click();
        Thread.sleep(3000);
        findElements(By.className("ui-button")).get(5).click();//you have to test another button later
        Thread.sleep(3000);
        findElement(By.id("start_stop")).click();
        Thread.sleep(20000);
        findElement(By.id("imageKeywords")).sendKeys("monkey");
        Thread.sleep(1000);
        findElement(By.id("loadUserImages")).click();
        findElement(By.id("start_stop")).click();
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
}
