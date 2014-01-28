package jp.gr.java_conf.ajax_mutator.example;

import jp.gr.java_conf.daisy.ajax_mutator.mutation_viewer.MutationViewer;

public class MemoryGameMutationAnalysis {
    public static void main(String[] args) {
        MutationAnalysisHelper manager = new MutationAnalysisHelper();
        // manager.generateMutations("/var/www/ex/p3.monkeyaround.biz/js/p3.js", false);
        manager.launchMutationViewer(
            "/var/www/ex/p3.monkeyaround.biz/js/mutants",
            "/var/www/ex/p3.monkeyaround.biz/js");
        //manager.doMutationAnalysis("/var/www/ex/p3.monkeyaround.biz/js/p3.js", MemoryGameTest.class);
//        manager.runTestForSpecificMutant(
//        		"/var/www/ex/p3.monkeyaround.biz/js/p3.js", "mutant1.diff", MemoryGameTest.class);
    }
}
