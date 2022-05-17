package cz.cvut.fel.rsp.ReservationSystem.dao.service;

import cz.cvut.fel.rsp.ReservationSystem.dao.CategoryRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.EventRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.SourceRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.testutil.Generator;
import cz.cvut.fel.rsp.ReservationSystem.exception.ReservationSystemException;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Address;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Category;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.CategoryServiceImpl;
import cz.cvut.fel.rsp.ReservationSystem.service.impl.SourceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class SourceServiceImplTest {
    @Autowired
    private SourceServiceImpl sourceService;

    @Autowired
    private CategoryServiceImpl categoryService;

    @Autowired
    private SourceRepository sourceRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void createSource_createRegularSource_sourceCreated() {
        Source source = new Source();
        source.setActive(true);
        source.setName("try");

        ReservationSystem reservationSystem = Generator.generateReservationSystem(null, null);

        sourceService.createSource(source, reservationSystem);

        Source result = sourceRepository.findById(source.getId()).get();
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isActive());
        Assertions.assertEquals(result.getName(), "try");
    }

    @Test
    void createSource_createRegularSource_sourceHasOneCategory() {
        Source source = new Source();
        source.setActive(true);
        source.setName("try");

        ReservationSystem reservationSystem = Generator.generateReservationSystem(null, null);

        sourceService.createSource(source, reservationSystem);

        Source result = sourceRepository.findById(source.getId()).get();
        Assertions.assertEquals(result.getCategories().size(), 1);
    }

    @Test
    void addCategory_addNewCategory_sourceHasNewCategory() {
        Category newCategory = Generator.generateCategory();
        Source source = Generator.generateSource(null, null);
        Source originalSource = Generator.generateSource(null, null);


        sourceService.createSource(source, null);
        sourceService.createSource(originalSource, null);
        categoryService.createCategory(newCategory, originalSource);

        sourceService.addCategory(source, newCategory);

        Source result = sourceRepository.findById(source.getId()).get();
        List categories = result.getCategories();

        Assertions.assertEquals(categories.contains(newCategory),true);
    }

    @Test
    void addCategory_addPickedCategory_returnReservationSystemException(){
        Category newCategory = Generator.generateCategory();
        ReservationSystem reservationSystem = Generator.generateReservationSystem(null, null);
        Source source = Generator.generateSource(reservationSystem, null);

        sourceService.createSource(source, null);
        categoryService.createCategory(newCategory, source);

        Source result = sourceRepository.findById(source.getId()).get();

        Assertions.assertThrows(ReservationSystemException.class, () -> sourceService.addCategory(result, newCategory));
    }

    @Test
    void removeCategory_removeWrongCategory_returnReservationSystemException(){
        Category newCategory = Generator.generateCategory();
        Source source = Generator.generateSource(null, null);

        sourceService.createSource(source, null);

        Source result = sourceRepository.findById(source.getId()).get();

        Assertions.assertThrows(ReservationSystemException.class, () -> sourceService.removeCategory(result, newCategory));
    }

    @Test
    void removeCategory_removeMainCategory_returnReservationException(){
        Source source = Generator.generateSource(null, null);
        sourceService.createSource(source, null);

        Source result = sourceRepository.findById(source.getId()).get();
        Category mainCategory = result.getCategories().get(0);

        Assertions.assertEquals(mainCategory.getName(), "Main events");
        Assertions.assertThrows(ReservationSystemException.class, () -> sourceService.removeCategory(result, mainCategory));

    }

    @Test
    void removeCategory_removeSomeCategory_removesPickedCategory(){
        Category newCategory = Generator.generateCategory();
        Source source = Generator.generateSource(null, null);

        sourceService.createSource(source, null);
        categoryService.createCategory(newCategory, source);

        Source result = sourceRepository.findById(source.getId()).get();
        Assertions.assertEquals(result.getCategories().size(), 2);

        Category categoryToRemove = categoryRepository.getById(newCategory.getId());
        sourceService.removeCategory(result, categoryToRemove);
        result = sourceRepository.findById(source.getId()).get();

        boolean isRemovedCategoryIn = result.getCategories().contains(newCategory);
        Assertions.assertFalse(isRemovedCategoryIn);
    }

    @Test
    void removeCategory_removeSomeCategoryWithEvents_eventsChangeCategoryToMainCategory(){
        Category newCategory = Generator.generateCategory();
        Source source = Generator.generateSource(null, null);
        Event event = Generator.generateIntervalEventWithoutRepetition();

        sourceService.createSource(source, null);
        categoryService.createCategory(newCategory, source);
        categoryService.addEventToCategory(event, newCategory);
        categoryService.update(newCategory);

        Source result = sourceRepository.findById(source.getId()).get();
        Assertions.assertEquals(result.getCategories().size(), 2);

        Category categoryToRemove = categoryRepository.getById(newCategory.getId());
        sourceService.removeCategory(result, categoryToRemove);
        result = sourceRepository.findById(source.getId()).get();

        Category mainCategory = result.getCategories().get(0);
        Event resultEvent = mainCategory.getEvents().get(0);

        boolean isRemovedCategoryIn = result.getCategories().contains(newCategory);
        Assertions.assertFalse(isRemovedCategoryIn);

        boolean isEventCategoryChanged = result.getCategories().contains(mainCategory);
        Assertions.assertTrue(isEventCategoryChanged);
    }

    @Test
    void exists_existsExistedSource_returnTrue(){
        Source source = Generator.generateSource(null, null);
        sourceService.createSource(source, null);

        boolean exists = sourceService.exists(source);
        Assertions.assertTrue(exists);
    }

    @Test
    void exists_notPersistedSource_returnFalse(){
        Source source = Generator.generateSource(null, null);
        source.setId(1);

        boolean exists = sourceService.exists(source);
        Assertions.assertFalse(exists);
    }

    @Test
    void removeAddress_removeAddress_SourceChangedAddressToNull(){
        Source source = Generator.generateSource(null, null);
        sourceService.createSource(source, null);

        sourceService.removeAddress(source);
        Source result = sourceRepository.findById(source.getId()).get();

        Address resultAddress = result.getAddress();
        Assertions.assertNull(resultAddress);
    }

    @Test
    void addAddress_addNewAddress_SourceHasNewAddress(){
        Source source = Generator.generateSource(null, null);
        source.setAddress(null);
        sourceService.createSource(source, null);

        Address address = Generator.generateAddress();
        sourceService.addAddress(source, address);

        Source result = sourceRepository.findById(source.getId()).get();
        Address resultAddress = result.getAddress();

        Assertions.assertEquals(resultAddress, address);
    }

    @Test
    void addAddress_addNewAddressToSourceWithAddress_returnReservationSystemException(){
        Source source = Generator.generateSource(null, null);
        Address originAddress = Generator.generateAddress();
        source.setAddress(originAddress);
        sourceService.createSource(source, null);

        Address address = Generator.generateAddress();

        Assertions.assertThrows(ReservationSystemException.class, () -> sourceService.addAddress(source, address));
    }
}
