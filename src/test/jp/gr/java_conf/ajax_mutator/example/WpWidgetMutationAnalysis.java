package jp.gr.java_conf.ajax_mutator.example;

public class WpWidgetMutationAnalysis {
    public static void main(String[] args) {
        MutationAnalysisHelper manager = new MutationAnalysisHelper();
        manager.generateMutations(
                "/var/www/ex/wp/wp-content/plugins/wp-widget-customizer/widget-customizer.js",
                false);
        manager.launchMutationViewer(
                        "/var/www/ex/wp/wp-content/plugins/wp-widget-customizer/mutants", 
                        "/var/www/ex/wp/wp-content/plugins/wp-widget-customizer");
        manager.doMutationAnalysis("/var/www/ex/wp/wp-content/plugins/wp-widget-customizer/widget-customizer.js", WpWidgetCustomizerTest.class);
    }
}
