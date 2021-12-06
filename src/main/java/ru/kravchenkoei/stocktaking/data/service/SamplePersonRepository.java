package ru.kravchenkoei.stocktaking.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kravchenkoei.stocktaking.data.entity.SamplePerson;

public interface SamplePersonRepository extends JpaRepository<SamplePerson, Integer> {

}