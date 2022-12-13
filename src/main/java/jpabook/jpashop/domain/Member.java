package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter //Setter 는 실무에서는 되도록 사용하지 않는다.
public class Member {
    @Id
    @GeneratedValue //시퀀스 값 사용
    @Column(name="member_id")
    private Long id;

//    @NotEmpty  //엔티티에 검증로직이 들어가있음. 좋지 않음
    private String name;

    @Embedded //내장타입임을 명시
    private Address address;


//    @JsonIgnore //엔티티에 검증로직이 들어가있는 것은 좋지 않음
    @OneToMany(mappedBy = "member") //일대다
    //mappedBy = "member" : 매핑된 값임을 명시. 읽기전용
    private List<Order> orders = new ArrayList<>();
}
