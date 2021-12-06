package ru.kravchenkoei.stocktaking.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kravchenkoei.stocktaking.data.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

}