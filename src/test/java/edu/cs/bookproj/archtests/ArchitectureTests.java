package edu.cs.bookproj.archtests;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

/*
@author Vitalii
@project book-proj
@class ArchitectureTests
@version 1.0.0
@since 13.09.24 - 9:59
*/

public class ArchitectureTests {

    // Завантаження класів проекту для тестування
    private final JavaClasses importedClasses = new ClassFileImporter().importPackages("edu.cs.bookproj");

    // 1. Перевірка, що класи у пакеті 'controller' мають анотацію @RestController
    @Test
    public void controllersShouldBeAnnotatedWithRestController() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().resideInAPackage("..controller..")
                .should().beAnnotatedWith(RestController.class);
        rule.check(importedClasses);
    }

    // 2. Перевірка, що класи у пакеті 'service' мають анотацію @Service
    @Test
    public void servicesShouldBeAnnotatedWithService() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().resideInAPackage("..service..")
                .should().beAnnotatedWith(Service.class);
        rule.check(importedClasses);
    }

    // 3. Перевірка, що класи у пакеті 'repository' мають анотацію @Repository
    @Test
    public void repositoriesShouldBeAnnotatedWithRepository() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().resideInAPackage("..repository..")
                .should().beAnnotatedWith(Repository.class);
        rule.check(importedClasses);
    }

    // 4. Перевірка, що сервіси знаходяться лише у пакеті 'service'
    @Test
    public void servicesShouldResideInServicePackage() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().areAnnotatedWith(Service.class)
                .should().resideInAPackage("..service..");
        rule.check(importedClasses);
    }

    // 5. Перевірка, що контролери знаходяться лише у пакеті 'controller'
    @Test
    public void controllersShouldResideInControllerPackage() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().areAnnotatedWith(RestController.class)
                .should().resideInAPackage("..controller..");
        rule.check(importedClasses);
    }

    // 6. Перевірка, що репозиторії знаходяться лише у пакеті 'repository'
    @Test
    public void repositoriesShouldResideInRepositoryPackage() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().areAnnotatedWith(Repository.class)
                .should().resideInAPackage("..repository..");
        rule.check(importedClasses);
    }

    // 7. Перевірка, що класи в пакеті 'model' не залежать від інших шарів
    @Test
    public void modelsShouldNotDependOnOtherLayers() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..model..")
                .should().dependOnClassesThat().resideInAnyPackage("..controller..", "..service..", "..repository..");
        rule.check(importedClasses);
    }

    // 8. Перевірка, що класи в пакеті 'controller' не залежать від 'repository'
    @Test
    public void controllersShouldNotDependOnRepositories() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAPackage("..repository..");
        rule.check(importedClasses);
    }

    // 9. Перевірка, що сервіси не залежать від контролерів
    @Test
    public void servicesShouldNotDependOnControllers() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..service..")
                .should().dependOnClassesThat().resideInAPackage("..controller..");
        rule.check(importedClasses);
    }

    // 10. Перевірка, що репозиторії не залежать від сервісів
    @Test
    public void repositoriesShouldNotDependOnServices() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..repository..")
                .should().dependOnClassesThat().resideInAPackage("..service..");
        rule.check(importedClasses);
    }

    // 11. Перевірка, що сервіси взаємодіють тільки з репозиторіями та моделями
    @Test
    public void servicesShouldOnlyDependOnRepositoriesAndModels() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().resideInAPackage("..service..")
                .should().onlyDependOnClassesThat().resideInAnyPackage("..repository..", "..model..", "java..");
        rule.check(importedClasses);
    }

    // 12. Перевірка, що контролери взаємодіють тільки з сервісами та моделями
    @Test
    public void controllersShouldOnlyDependOnServicesAndModels() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().resideInAPackage("..controller..")
                .should().onlyDependOnClassesThat().resideInAnyPackage("..service..", "..model..", "java..");
        rule.check(importedClasses);
    }

    // 13. Перевірка, що репозиторії взаємодіють тільки з моделями
    @Test
    public void repositoriesShouldOnlyDependOnModels() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().resideInAPackage("..repository..")
                .should().onlyDependOnClassesThat().resideInAPackage("..model..");
        rule.check(importedClasses);
    }

    // 14. Перевірка, що контролери мають доступ до сервісів, але не безпосередньо до репозиторіїв
    @Test
    public void controllersShouldAccessServicesButNotRepositories() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..controller..")
                .should().accessClassesThat().resideInAPackage("..repository..");
        rule.check(importedClasses);
    }

    // 15. Перевірка, що сервіси використовують репозиторії, але не контролери
    @Test
    public void servicesShouldUseRepositoriesButNotControllers() {
        // Перевірка, що сервіси залежать від репозиторіїв
        ArchRule servicesShouldDependOnRepositories = ArchRuleDefinition.classes()
                .that().resideInAPackage("..service..")
                .should().dependOnClassesThat().resideInAPackage("..repository..");
        servicesShouldDependOnRepositories.check(importedClasses);

        // Перевірка, що сервіси не залежать від контролерів
        ArchRule servicesShouldNotDependOnControllers = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..service..")
                .should().dependOnClassesThat().resideInAPackage("..controller..");
        servicesShouldNotDependOnControllers.check(importedClasses);
    }

    // 16. Перевірка, що класи в пакеті 'model' не мають залежностей на інші шари
    @Test
    public void modelsShouldNotHaveDependenciesOnOtherLayers() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..model..")
                .should().dependOnClassesThat().resideInAnyPackage("..service..", "..repository..", "..controller..");
        rule.check(importedClasses);
    }

    // 17. Перевірка, що класи з пакету 'repository' мають методи CRUD
    @Test
    public void repositoriesShouldHaveCrudMethods() {
        ArchRule rule = ArchRuleDefinition.methods()
                .that().areDeclaredInClassesThat().resideInAPackage("..repository..")
                .should().haveNameMatching("(save|find.*|delete.*|exists.*)");
        rule.check(importedClasses);
    }

    // 18. Перевірка, що класи з пакету 'service' не залежать від інфраструктури (MongoDB)
    @Test
    public void servicesShouldNotDependOnInfrastructure() {
        ArchRule rule = ArchRuleDefinition.noClasses()
                .that().resideInAPackage("..service..")
                .should().dependOnClassesThat().resideInAPackage("org.springframework.data.mongodb..");
        rule.check(importedClasses);
    }

    // 19. Перевірка, що сервіси використовують методи репозиторіїв
    @Test
    public void servicesShouldUseRepositoryMethods() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().resideInAPackage("..service..")
                .should().dependOnClassesThat().resideInAPackage("..repository..");
        rule.check(importedClasses);
    }

    // 20. Перевірка, що контролери використовують методи сервісів
    @Test
    public void controllersShouldUseServiceMethods() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAPackage("..service..");
        rule.check(importedClasses);
    }
}