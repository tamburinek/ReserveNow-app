package cz.cvut.fel.rsp.ReservationSystem.service.impl;

import cz.cvut.fel.rsp.ReservationSystem.dao.CategoryRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.EventRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.SourceRepository;
import cz.cvut.fel.rsp.ReservationSystem.exception.ReservationSystemException;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Address;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Category;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.ReservationSystem;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SourceServiceImpl implements SourceService {

    private final SourceRepository sourceRepository;

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    @Autowired
    public SourceServiceImpl(SourceRepository sourceRepository, CategoryRepository categoryRepository, EventRepository eventRepository) {
        this.sourceRepository = sourceRepository;
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
    }

    //show all sources is implemented in reservationSystemServiceImpl !!!

    @Override
    @Transactional
    public void createSource(Source source, ReservationSystem reservationSystem) {
        source.setReservationSystem(reservationSystem);

        Category initialCategory = new Category();
        initialCategory.setName("Main events");
        initialCategory.setSources(new ArrayList<>(Collections.singletonList(source)));
        initialCategory.setEvents(new ArrayList<>());
        categoryRepository.save(initialCategory);

        source.setCategories(new ArrayList<>(Collections.singletonList(initialCategory)));

        sourceRepository.save(source);
    }

    @Override
    @Transactional
    public void createSource(Source source, ReservationSystem reservationSystem, String name) {
        source.setReservationSystem(reservationSystem);

        Category initialCategory = new Category();
        initialCategory.setName(name);
        initialCategory.setSources(new ArrayList<>(Collections.singletonList(source)));
        initialCategory.setEvents(new ArrayList<>());
        categoryRepository.save(initialCategory);

        source.setCategories(new ArrayList<>(Collections.singletonList(initialCategory)));

        sourceRepository.save(source);
    }

    @Override
    @Transactional
    public void removeAddress(Source source) {
        if (source.getAddress()!=null){
            source.setAddress(null);
            sourceRepository.save(source);
        }
    }

    @Override
    @Transactional
    public void addAddress(Source source, Address address) {
        if (source.getAddress()== null){
            source.setAddress(address);
            sourceRepository.save(source);
        }
        else{
            throw new ReservationSystemException("Source already has address " + source.getAddress());
        }
    }

    @Override
    public Source find(Integer id) {
        return sourceRepository.getById(id);
    }

    @Transactional
    public void addCategory(Source source, Category category){
        if (source.getCategories().contains(category)){
            throw new ReservationSystemException("Source already has category with this name " + category.getName());
        }

        source.getCategories().add(category);
//        category.setSources(source);
        category.getSources().add(source);
        categoryRepository.save(category);
        sourceRepository.save(source);
    }

    @Transactional
    public void removeCategory(Source source, Category category){
        if (!source.getCategories().contains(category)){
            throw new ReservationSystemException("Source " + source.getName() + " does not have this category " + category.getName());
        }

        if (category.getName().equals("Main events")){
            throw new ReservationSystemException("You can not delete main category");
        }
        Category helperCategory = null;
        for (Category category1: source.getCategories()) {
            if (category1.getName().equals("Main events")){
                helperCategory = categoryRepository.getById(category1.getId());
                break;
            }
        }
        helperCategory.getEvents().addAll(category.getEvents());
        categoryRepository.save(helperCategory);
        for (Event event: category.getEvents()) {
            event.setCategory(helperCategory);
            eventRepository.save(event);
        }
        source.getCategories().remove(category);
        sourceRepository.save(source);
    }

    @Transactional
    public boolean exists(Source source){
        return sourceRepository.existsById(source.getId());
    }
}
