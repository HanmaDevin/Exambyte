package de.propra.exambyte.architecture;

import com.sun.tools.javac.Main;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.Configuration.consideringOnlyDependenciesInDiagram;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.adhereToPlantUmlDiagram;

@Disabled
public class OnionArchitectureTest {

    private final JavaClasses klassen =
            new ClassFileImporter().importPackagesOf(Main.class);

    @Test
    @DisplayName("Die Exambyte Anwendung hat eine Onion Architektur")
    public void onionArchitektur() {
        ArchRule rule = onionArchitecture()
                .domainModels("..domain..")
                .domainServices("..services..");
                //.applicationServices("mwst.services..")
                //.adapter("web", "mwst.web")
                //.adapter("persistence", "mwst.katalog")
                //.adapter("kalender", "mwst.kalender");
        rule.check(klassen);
    }

    @Test
    @DisplayName("Die Architektur ist wie im UML Diagramm dargestellt")
    void test_(){
        // PlantUML-Datei liegt unter src/test/resources
        URL uml = getClass().getResource("/architecture.puml");
        ArchRule rule = classes().should(adhereToPlantUmlDiagram(
                uml,consideringOnlyDependenciesInDiagram()));
        rule.check(klassen);
    }
}
