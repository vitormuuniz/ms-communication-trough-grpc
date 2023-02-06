package com.grpcflix.user.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "T_USER")
@ToString
@Getter
@Setter
public class User {
    @Id
    private String login;
    private String name;
    private String genre;
}
