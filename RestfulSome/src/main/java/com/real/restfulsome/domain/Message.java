package com.real.restfulsome.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Entity
public class Message {
    @Id
    @GenericGenerator(name = "increment")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String text;
    private String tag;
    public Message(){}
    public Message(String text, String tag) {
        this.text = text;
        this.tag = tag;
    }
}
