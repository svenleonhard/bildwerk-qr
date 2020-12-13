package de.bildwerk.qr;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("de.bildwerk.qr");

        noClasses()
            .that()
            .resideInAnyPackage("de.bildwerk.qr.service..")
            .or()
            .resideInAnyPackage("de.bildwerk.qr.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..de.bildwerk.qr.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
