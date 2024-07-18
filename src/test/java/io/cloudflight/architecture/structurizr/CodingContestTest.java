package io.cloudflight.architecture.structurizr;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.util.FileSystemUtils.deleteRecursively;

@SpringBootTest(classes = CodingContestArchitecture.class)
@ActiveProfiles("codingcontest")
class CodingContestTest {

    private static File outputDirectory = new File("build/test/codingcontest");

    @BeforeAll
    static void cleanOutputDirectory() {
        if (outputDirectory.exists()) {
            deleteRecursively(outputDirectory);
        }
    }

    @Test
    void codingContestIsPrinted() {
        File codingContest = new File(outputDirectory, "/plantuml/codingcontest.puml");
        assertTrue(codingContest.exists());
    }

    @Test
    void sequenceDiagramsArePrinted() {
        File diagram = new File(outputDirectory, "/plantuml/admin-visit.puml");
        assertTrue(diagram.exists());
    }
}