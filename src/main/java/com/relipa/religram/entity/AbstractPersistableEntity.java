package com.relipa.religram.entity;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract  class AbstractPersistableEntity<ID> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
