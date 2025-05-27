package com.oss.ossv1.test;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;
import org.junit.platform.engine.support.descriptor.ClassSource;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class SourceTrackingListener implements TestExecutionListener {

    private final Set<String> classNames = new HashSet<>();

    @Override
    public void executionStarted(TestIdentifier testIdentifier) {
        testIdentifier.getSource().ifPresent(source -> {
            if (source instanceof ClassSource classSource) {
                classNames.add(classSource.getClassName());
            }
        });
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        try (FileWriter writer = new FileWriter("target/sources.txt")) {
            for (String className : classNames) {
                writer.write(className + "\n");
            }
            System.out.println(" sources.txt written to target/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
