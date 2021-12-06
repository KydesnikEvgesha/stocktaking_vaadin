package ru.kravchenkoei.stocktaking.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import ru.kravchenkoei.stocktaking.data.entity.Location;

@Service
public class LocationService extends CrudService<Location, Integer> {

    private LocationRepository repository;

    public LocationService(@Autowired LocationRepository repository) {
        this.repository = repository;
    }

    @Override
    protected LocationRepository getRepository() {
        return repository;
    }

}
