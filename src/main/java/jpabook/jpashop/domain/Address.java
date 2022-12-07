package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable //JPA의 내장타입임을 명시
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address(){} //protected 생성자를 생성해놈. JPA를 위해

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
