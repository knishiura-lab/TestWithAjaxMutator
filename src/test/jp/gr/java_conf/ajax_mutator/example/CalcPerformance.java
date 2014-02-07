package jp.gr.java_conf.ajax_mutator.example;

public class CalcPerformance {
	public static void main(String[] args) {
		MutationAnalysisHelper manager = new MutationAnalysisHelper();
		manager.measureTestPerformance(
				"/var/www/ex/wp/wp-content/plugins/wp-widget-customizer", 
				WpWidgetCustomizerTest.class, 
				1, 
				60);
	}
}
