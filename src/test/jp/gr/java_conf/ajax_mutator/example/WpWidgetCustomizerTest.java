package jp.gr.java_conf.ajax_mutator.example;

import org.junit.Test;
import org.openqa.selenium.By;

public class WpWidgetCustomizerTest extends TestBase {
    public WpWidgetCustomizerTest() {
        super(/* calc coverage */true, /* run in background*/false);
    }

    @Test
    public void hoge() {
        openUrl("http://localhost/ex/wp/wp-admin/customize.php?theme=twentyfourteen");
        handleLogin();
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
