package io.github.pascalgrimaud.toto;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {

        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("io.github.pascalgrimaud.toto");

        noClasses()
            .that()
                .resideInAnyPackage("io.github.pascalgrimaud.toto.service..")
            .or()
                .resideInAnyPackage("io.github.pascalgrimaud.toto.repository..")
            .should().dependOnClassesThat()
                .resideInAnyPackage("..io.github.pascalgrimaud.toto.web..")
        .because("Services and repositories should not depend on web layer")
        .check(importedClasses);
    }
}
