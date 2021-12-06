package ru.kravchenkoei.stocktaking.data.entity;

import javax.persistence.Entity;
import ru.kravchenkoei.stocktaking.data.AbstractEntity;

@Entity
public class Company extends AbstractEntity {

    private String name;
    private String address;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

}
