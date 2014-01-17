package jp.gr.java_conf.ajax_mutator.example;

import com.google.common.collect.ImmutableSet;

import jp.gr.java_conf.daisy.ajax_mutator.JUnitExecutor;
import jp.gr.java_conf.daisy.ajax_mutator.MutateVisitor;
import jp.gr.java_conf.daisy.ajax_mutator.MutateVisitorBuilder;
import jp.gr.java_conf.daisy.ajax_mutator.MutationTestConductor;
import jp.gr.java_conf.daisy.ajax_mutator.mutation_viewer.MutationViewer;
import jp.gr.java_conf.daisy.ajax_mutator.mutator.*;
import jp.gr.java_conf.daisy.ajax_mutator.mutator.replacing_among.*;
import jp.gr.java_conf.daisy.ajax_mutator.mutator.replacing_to_no_op.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Helper class that does initialization stuff for {@link jp.gr.java_conf.daisy.ajax_mutator.MutationTestConductor}.
 */
public class MutationAnalysisHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MutationAnalysisHelper.class);
    private final MutateVisitor visitor = initVisitor();

    /**
     * Generate mutation files (*.diff files) for specified JavaScript file.
     */
    public void generateMutations(String pathToJsFile, boolean shouldOverride) {
        File jsFile = new File(pathToJsFile);
        if (!jsFile.exists()) {
            throw new IllegalStateException("Invalid path: " + pathToJsFile);
        }
        if (isMutationFileExists(jsFile) && !shouldOverride) {
            return;
        }
        MutationTestConductor conductor = getMutationTestManager(pathToJsFile);

        LOGGER.info("There is no mutant exists for specified JS file, we first generate mutants...");
        // We first need to generate mutants
        Set<Mutator> mutators = ImmutableSet.<Mutator>of(
                new EventTargetRAMutator(visitor.getEventAttachments()),
                new EventTypeRAMutator(visitor.getEventAttachments()),
                new EventCallbackRAMutator(visitor.getEventAttachments()),

                new TimerEventDurationRAMutator(visitor.getTimerEventAttachmentExpressions()),
                new TimerEventCallbackRAMutator(visitor.getTimerEventAttachmentExpressions()),

                new AppendedDOMRAMutator(visitor.getDomAppendings()),
                new AttributeModificationTargetRAMutator(visitor.getAttributeModifications()),
                new AttributeModificationValueRAMutator(visitor.getAttributeModifications()),
                new DOMSelectionSelectNearbyMutator(),
                new DOMCreationToNoOpMutator(),
                new DOMRemovalToNoOpMutator(),
                new DOMReplacementSrcTargetMutator(),
                new DOMCloningToNoOpMutator(),
                new DOMNormalizationToNoOpMutator(),

                new FakeBlankResponseBodyMutator(),
                new ReplacingAjaxCallbackMutator(),
                new RequestOnSuccessHandlerRAMutator(visitor.getRequests()),
                new RequestMethodRAMutator(visitor.getRequests()),
                new RequestUrlRAMutator(visitor.getRequests()));
        conductor.generateMutations(mutators);
    }

    /**
     * Start mutation analysis (apply *.diff file, run test, reapply *.diff file...)
     */
    public void doMutationAnalysis(String pathToJsFile, Class<?> testClass) {
        File jsFile = new File(pathToJsFile);
        if (!jsFile.exists()) {
            throw new IllegalStateException("Invalid path: " + pathToJsFile);
        }
        if (!isMutationFileExists(jsFile)) {
            generateMutations(pathToJsFile, false);
        }

        MutationTestConductor conductor = getMutationTestManager(pathToJsFile);
        conductor.mutationAnalysisUsingExistingMutations(new JUnitExecutor(false, testClass));
    }

    /**
     */
    public void runTestForSpecificMutant(String pathToJsFile, String mutantFileName, Class<?> testClass) {
        File jsFile = new File(pathToJsFile);
        if (!jsFile.exists()) {
            throw new IllegalStateException("Invalid path: " + pathToJsFile);
        }
        if (!isMutationFileExists(jsFile)) {
            generateMutations(pathToJsFile, false);
        }

        MutationTestConductor conductor = getMutationTestManager(pathToJsFile);
        conductor.tryToKillSpecificMutant(mutantFileName,new JUnitExecutor(false, testClass));
    }

    public void launchMutationViewer(final String mutantsDir, final String baseDir) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                javafx.application.Application.launch(
                        MutationViewer.class, "--mutants_dir=" + mutantsDir, "--base_dir=" + baseDir);
            }
        }.start();
    }

    /**
     * Meature test's fault finding capability (only used by authors)
     */
    public void measureTestPerformance(String jsDirPath, Class<?> testClass, int startIndex, int endIndex) {
        int numOfKilled = 0;
        File directory = new File(jsDirPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalStateException("");
        }
        StringBuilder resultStrBuilder = new StringBuilder();
        JUnitExecutor executor = new JUnitExecutor(false, testClass);
        for (int i = startIndex; i <= endIndex; i++) {
            try {
                Runtime.getRuntime().exec("git checkout fault" + i, null, directory);
            } catch (IOException e) {
                System.err.println("Error at " + i);
                System.err.println(e);
                return;
            }
            resetState();
            boolean result = executor.execute();
            if (!result) {
                numOfKilled++;
            }
            resultStrBuilder.append(result ? 'x' : 'o').append(",");
            System.out.println((result ? "success" : "fail") + " at index " + i);
        }
        System.out.println(resultStrBuilder.toString());
        System.out.println(numOfKilled + " | " + (endIndex - startIndex + 1) + " | "
                + (numOfKilled * 1.0 / (endIndex - startIndex + 1)));
    }

    private boolean isMutationFileExists(File jsFile) {
        File[] siblings = jsFile.getParentFile().listFiles();
        for (File sibling: siblings) {
            if (sibling.getName().equals("mutants")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method for reset state of the SUT such as DB or session reset. Subclass can override this 
     * method.  Default implementation is do nothing.
     */
    protected void resetState() {
    }

    private MutateVisitor initVisitor() {
        MutateVisitorBuilder builder = MutateVisitor.defaultJqueryBuilder();
        MutateVisitor visitor = builder.build();
        return visitor;
    }

    private MutationTestConductor getMutationTestManager(String pathToJsFile) {
        MutationTestConductor conductor = new MutationTestConductor();
        conductor.setup(pathToJsFile, "http://ajax_mutator.java_conf.gr.jp", visitor);
        conductor.setSaveInformationInterval(2);
        return conductor;
    }
}
