package com.relipa.religram.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "messages")
@Getter
@Setter
@ToString
public class Message extends AbstractAuditableEntity<Long> implements Serializable {
    @Column(name = "from_id")
    private Long fromId;

    @Column(name = "to_id")
    private Long toId;

    @Column(length = 3000)
    private String content;
}