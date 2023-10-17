package com.sumalukasz.achunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.syntax.elements.ClassesShouldConjunction;
import com.tngtech.archunit.lang.syntax.elements.FieldsShouldConjunction;
import com.tngtech.archunit.lang.syntax.elements.MethodsShouldConjunction;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.core.domain.properties.HasName.Predicates.nameEndingWith;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class ArchitectureTest {

    private static final JavaClasses PACKAGE_TO_TEST = new ClassFileImporter().importPackages("com.sumalukasz.testing");
    private static final Architectures.LayeredArchitecture LAYERED_ARCHITECTURE = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Constant").definedBy("..constant..")
            .layer("Controller").definedBy("..controller..")
            .layer("Exception").definedBy("..exception..")
            .layer("Filter").definedBy("..filter..")
            .layer("Model").definedBy("..model..")
            .layer("Repository").definedBy("..repository..")
            .layer("Service").definedBy("..service..")
            .layer("Utility").definedBy("..utility..");

    @Test
    void shouldControllersBeInControllerPackageAndShouldNotBeAvailableAnywhereAndShouldContainServiceField() {
        ClassesShouldConjunction controllers = classes().that()
                .resideInAPackage("..controller..")
                .should()
                .beAnnotatedWith(RestController.class)
                .andShould()
                .haveSimpleNameEndingWith("Controller");
        controllers.check(PACKAGE_TO_TEST);

        FieldsShouldConjunction fieldsInController = fields().that()
                .haveNameEndingWith("Service")
                .should()
                .beFinal()
                .andShould()
                .bePrivate()
                .andShould()
                .notBeStatic()
                .andShould()
                .beDeclaredInClassesThat()
                .areAnnotatedWith(RestController.class);
        fieldsInController.check(PACKAGE_TO_TEST);

        Architectures.LayeredArchitecture controllerLayer = LAYERED_ARCHITECTURE
                .whereLayer("Controller").mayNotBeAccessedByAnyLayer();
        controllerLayer.check(PACKAGE_TO_TEST);
    }

    @Test
    void shouldServicesBeInServicePackageAndShouldBeAvailableOnlyInControllerPackageAndShouldContainRepositoryField() {
        ClassesShouldConjunction services = classes().that()
                .resideInAPackage("..service..")
                .should()
                .beAnnotatedWith(Service.class)
                .andShould()
                .haveSimpleNameEndingWith("Service");
        services.check(PACKAGE_TO_TEST);

        FieldsShouldConjunction fieldsInService = fields().that()
                .haveNameEndingWith("Repository")
                .should()
                .beFinal()
                .andShould()
                .bePrivate()
                .andShould()
                .notBeStatic()
                .andShould()
                .beDeclaredInClassesThat()
                .areAnnotatedWith(Service.class);
        fieldsInService.check(PACKAGE_TO_TEST);

        Architectures.LayeredArchitecture serviceLayer = LAYERED_ARCHITECTURE
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller");
        serviceLayer.check(PACKAGE_TO_TEST);
    }

    @Test
    void shouldRepositoriesBeInRepositoryPackageAndShouldBeAvailableOnlyInServicePackageAndShouldContainJdbcTemplateOrNamedParameterJdbcTemplateField() {
        ClassesShouldConjunction repositories = classes().that()
                .resideInAPackage("..repository..")
                .should()
                .beAnnotatedWith(Repository.class)
                .andShould()
                .haveSimpleNameEndingWith("Repository");
        repositories.check(PACKAGE_TO_TEST);

        FieldsShouldConjunction fieldsInRepository = fields().that()
                .haveRawType(NamedParameterJdbcTemplate.class)
                .should()
                .beFinal()
                .andShould()
                .bePrivate()
                .andShould()
                .notBeStatic()
                .andShould()
                .beDeclaredInClassesThat()
                .areAnnotatedWith(Repository.class);

        fieldsInRepository.check(PACKAGE_TO_TEST);

        Architectures.LayeredArchitecture repositoryLayer = LAYERED_ARCHITECTURE
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service");
        repositoryLayer.check(PACKAGE_TO_TEST);

    }

    @Test
    void shouldDtosBeInDtoPackageAndEndOfClassNameShouldBeEndingWithDtoAndBeFinalAndBePublic() {
        ClassesShouldConjunction dtos = classes().that()
                .resideInAPackage("..model.dto..")
                .should()
                .haveSimpleNameEndingWith("Dto")
                .andShould()
                .bePublic()
                .andShould()
                .haveModifier(JavaModifier.FINAL);
        dtos.check(PACKAGE_TO_TEST);
    }

    @Test
    void shouldEntitiesBeRecordAndShouldBePublic() {
        ClassesShouldConjunction records = classes().that()
                .resideInAPackage("..model.entity..")
                .should()
                .bePublic()
                .andShould()
                .beRecords();
        records.check(PACKAGE_TO_TEST);
    }

    @Test
    void shouldRequestDtosBePublicAndShouldContainConstructorWithAllFieldsAndShouldHaveNoSetters() {
        ClassesShouldConjunction requests = classes().that()
                .resideInAPackage("..model.request..")
                .should()
                .bePublic()
                .andShould()
                .haveOnlyFinalFields();
        requests.check(PACKAGE_TO_TEST);
    }

    @Test
    void shouldFiltersBeInFilterPackageAndShouldBeEndingWithFilter() {
        ClassesShouldConjunction filters = classes().that()
                .resideInAPackage("..filter..")
                .should()
                .bePublic()
                .andShould()
                .beAnnotatedWith(Component.class)
                .andShould()
                .haveSimpleNameEndingWith("Filter");
        filters.check(PACKAGE_TO_TEST);
    }

    @Test
    void shouldExceptionsBeInExceptionPackageAndShouldBeEndingWithExceptionAndShouldExtendingRuntimeExceptionClass() {
        ClassesShouldConjunction exceptions = classes().that()
                .resideInAPackage("..exception..")
                .and()
                .haveSimpleNameEndingWith("Exception")
                .should()
                .bePublic()
                .andShould()
                .beAssignableTo(RuntimeException.class);
        exceptions.check(PACKAGE_TO_TEST);
    }

    @Test
    void shouldUtilitiesBeInUtilityPackageAndShouldBeEndingWithUtils() {
        ClassesShouldConjunction utilities = classes().that()
                .resideInAPackage("..utility..")
                .should()
                .haveSimpleNameEndingWith("Utils")
                .andShould()
                .bePublic()
                .andShould()
                .haveModifier(JavaModifier.FINAL)
                .andShould()
                .haveOnlyPrivateConstructors();
        utilities.check(PACKAGE_TO_TEST);
    }

    @Test
    void shouldMethodsInUtilitiesBePublicAndStatic() {
        MethodsShouldConjunction methodsInUtils = methods().that()
                .areDeclaredInClassesThat(nameEndingWith("Utils"))
                .should()
                .bePublic()
                .andShould()
                .beStatic();
        methodsInUtils.check(PACKAGE_TO_TEST);
    }

    @Test
    void shouldConstantsBeInConstantPackageAndShouldBeEndingWithConstant() {
        ClassesShouldConjunction constants = classes().that()
                .resideInAPackage("..constant..")
                .should()
                .haveSimpleNameEndingWith("Constant")
                .andShould()
                .bePublic()
                .andShould()
                .beEnums();
        constants.check(PACKAGE_TO_TEST);
    }
}
