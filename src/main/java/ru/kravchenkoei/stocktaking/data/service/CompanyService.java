package ru.kravchenkoei.stocktaking.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vaadin.artur.helpers.CrudService;
import ru.kravchenkoei.stocktaking.data.entity.Company;

@Service
public class CompanyService extends CrudService<Company, Integer> {

    private CompanyRepository repository;

    public CompanyService(@Autowired CompanyRepository repository) {
        this.repository = repository;
    }

    @Override
    protected CompanyRepository getRepository() {
        return repository;
    }

}
