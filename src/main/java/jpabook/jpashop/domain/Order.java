package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //다른곳에서 객체 생성하지 못하도록 설정
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

    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderState.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //== 비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderState.CANCEL);
        for(OrderItem orderItem : this.orderItems){
            orderItem.cancel();
        }
    }

    //==조회 로직 ==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
     /*  int totalPrice =0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        아래코드로 변경 가능
      */
        int totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        return totalPrice;
    }
}
