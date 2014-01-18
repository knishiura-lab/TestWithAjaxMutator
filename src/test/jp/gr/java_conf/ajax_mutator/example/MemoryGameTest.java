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

public class MemoryGameTest extends TestBase {
    public MemoryGameTest() {
        super(
                /* calc coverage */ true,
                /* run in background*/ false);
    }

    @Test
    public void testSomething() {
        // Please implement here
        openUrl("http://localhost/ex/p3.monkeyaround.biz/index.php");
        // Please implement here
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
