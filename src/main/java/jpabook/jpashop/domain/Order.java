package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
public class Order {
    @Id
    @GeneratedValue
    @Column(name ="order_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY) //다대일 관계
    @JoinColumn(name ="member_id")  //FK 를 명시
    private Member member; //주문 회원
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)  //mappedBy = "order" 매핑된 컬럼임을 명시
    private List<OrderItem> orderItems = new ArrayList<>();
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) //일대일 매핑일때는 FK를 아무곳에서나 설정해도됨
    //cascade = CascadeType.ALL -> order를 저장하면 delivery도 persist 해줌
    @JoinColumn(name = "delivery_id") //연관관계의 주인임을 명시
    private Delivery delivery; //배송정보
    
    private LocalDateTime orderDate; //주문시간 
    //테이블 컬럼명은 order_date로 바뀜

    @Enumerated(EnumType.STRING)
    private OrderState status; //주문상태 [ORDER, CANCEL]



    //==연관관계 메서드 ==//
    public void setMember(Member member){
        this.member=member;
        member.getOrders().add(this);
    }
/*
        이런 코드를 위의 setMember로 처리
        Member member = new Member();
        Order order= new Order();
        member.getOrders().add(order);
        order.setMember(member);
 */

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery=delivery;
        delivery.setOrder(this);
    }
}
