package ru.kravchenkoei.stocktaking.data.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kravchenkoei.stocktaking.data.entity.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

}