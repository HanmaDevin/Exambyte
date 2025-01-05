package de.propra.exambyte.architecture;

import com.sun.tools.javac.Main;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.library.Architectures.onionArchitecture;

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
}
