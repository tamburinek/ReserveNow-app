package cz.cvut.fel.rsp.ReservationSystem.service.impl;

import cz.cvut.fel.rsp.ReservationSystem.dao.CategoryRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.EventRepository;
import cz.cvut.fel.rsp.ReservationSystem.dao.SourceRepository;
import cz.cvut.fel.rsp.ReservationSystem.exception.ReservationSystemException;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Category;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.events.Event;
import cz.cvut.fel.rsp.ReservationSystem.model.reservation.Source;
import cz.cvut.fel.rsp.ReservationSystem.service.interfaces.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository dao;
    private final SourceRepository sourceDao;
    private final EventRepository eventDao;

    @Autowired
    public CategoryServiceImpl(CategoryRepository dao, SourceRepository sourceDao, EventRepository eventDao) {
        this.dao = dao;
        this.sourceDao = sourceDao;
        this.eventDao = eventDao;
    }

    @Override
    public void createCategory(Category category, Source source) {
        List<Event> events = new ArrayList<>();
        category.setEvents(events);
        category.setSources(new ArrayList<>(Collections.singletonList(source)));
        dao.save(category);
        source.getCategories().add(category);
        sourceDao.save(source);
    }

    @Override
    public void addEventToCategory(Event event, Category category) {
        if (category.getEvents().contains(event)){
            throw new ReservationSystemException("Category " + category.getName() + " already has event " + event.getName());
        }

        category.getEvents().add(event);
        event.setCategory(category);
        eventDao.save(event);
        dao.save(category);
    }

    @Override
    public void removeEventFromCategory(Event event, Category category) {

    }

    @Override
    public void update(Category category) {

    }

    @Override
    public void remove(Category toRemove, Category moveEventsTo) {

    }

    @Override
    public Category find(Integer id) {
        return dao.getById(id);
    }
}
