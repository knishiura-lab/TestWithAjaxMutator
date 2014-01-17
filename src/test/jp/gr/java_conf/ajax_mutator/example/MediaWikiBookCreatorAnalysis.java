package jp.gr.java_conf.ajax_mutator.example;

/**
 * @author Kazuki Nishiura
 */
public class MediaWikiBookCreatorAnalysis {
    public static void main(String[] args) {
        MutationAnalysisHelper manager = new MutationAnalysisHelper();
        manager.generateMutations("/var/www/ex/mediawiki/extensions/Collection/js/bookcreator.js",
                false);
        manager.launchMutationViewer(
                "/var/www/ex/mediawiki/extensions/Collection/js/mutants",
                "/var/www/ex/mediawiki/extensions/Collection/js");
        manager.doMutationAnalysis("/var/www/ex/mediawiki/extensions/Collection/js/bookcreator.js",
                MediaWikiBookCreatorTest.class);
    }
}
