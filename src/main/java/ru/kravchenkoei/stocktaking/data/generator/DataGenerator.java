package ru.kravchenkoei.stocktaking.data.generator;

import com.vaadin.exampledata.DataType;
import com.vaadin.exampledata.ExampleDataGenerator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import ru.kravchenkoei.stocktaking.data.entity.Company;
import ru.kravchenkoei.stocktaking.data.entity.Location;
import ru.kravchenkoei.stocktaking.data.entity.SamplePerson;
import ru.kravchenkoei.stocktaking.data.service.CompanyRepository;
import ru.kravchenkoei.stocktaking.data.service.LocationRepository;
import ru.kravchenkoei.stocktaking.data.service.SamplePersonRepository;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(SamplePersonRepository samplePersonRepository,
            CompanyRepository companyRepository, LocationRepository locationRepository) {
        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (samplePersonRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");

            logger.info("... generating 100 Sample Person entities...");
            ExampleDataGenerator<SamplePerson> samplePersonRepositoryGenerator = new ExampleDataGenerator<>(
                    SamplePerson.class, LocalDateTime.of(2021, 12, 6, 0, 0, 0));
            samplePersonRepositoryGenerator.setData(SamplePerson::setId, DataType.ID);
            samplePersonRepositoryGenerator.setData(SamplePerson::setFirstName, DataType.FIRST_NAME);
            samplePersonRepositoryGenerator.setData(SamplePerson::setLastName, DataType.LAST_NAME);
            samplePersonRepositoryGenerator.setData(SamplePerson::setEmail, DataType.EMAIL);
            samplePersonRepositoryGenerator.setData(SamplePerson::setPhone, DataType.PHONE_NUMBER);
            samplePersonRepositoryGenerator.setData(SamplePerson::setDateOfBirth, DataType.DATE_OF_BIRTH);
            samplePersonRepositoryGenerator.setData(SamplePerson::setOccupation, DataType.OCCUPATION);
            samplePersonRepositoryGenerator.setData(SamplePerson::setImportant, DataType.BOOLEAN_10_90);
            samplePersonRepository.saveAll(samplePersonRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Company entities...");
            ExampleDataGenerator<Company> companyRepositoryGenerator = new ExampleDataGenerator<>(Company.class,
                    LocalDateTime.of(2021, 12, 6, 0, 0, 0));
            companyRepositoryGenerator.setData(Company::setId, DataType.ID);
            companyRepositoryGenerator.setData(Company::setName, DataType.WORD);
            companyRepositoryGenerator.setData(Company::setAddress, DataType.ADDRESS);
            companyRepository.saveAll(companyRepositoryGenerator.create(100, seed));

            logger.info("... generating 100 Location entities...");
            ExampleDataGenerator<Location> locationRepositoryGenerator = new ExampleDataGenerator<>(Location.class,
                    LocalDateTime.of(2021, 12, 6, 0, 0, 0));
            locationRepositoryGenerator.setData(Location::setId, DataType.ID);
            locationRepositoryGenerator.setData(Location::setName, DataType.WORD);
            locationRepositoryGenerator.setData(Location::setAddress, DataType.ADDRESS);
            locationRepository.saveAll(locationRepositoryGenerator.create(100, seed));

            logger.info("Generated demo data");
        };
    }

}