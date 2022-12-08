package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch){



        //language = JPAQL
        String jpql ="select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if(orderSearch.getOrderState()!= null){
            if(isFirstCondition){
                jpql += " where";
                isFirstCondition=false;
            } else {
                jpql +=" and";
            }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())){
            if(isFirstCondition){
                jpql +=" where";
                isFirstCondition=false;
            }else{
                jpql +=" and";
            }
            jpql +=" m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000); //최대 1000건

        if(orderSearch.getOrderState() != null){
            query = query.setParameter("status", orderSearch.getOrderState());
        }
        if(StringUtils.hasText(orderSearch.getMemberName())){
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();

    }

    /**
     *  JPA Criteria
     */
    public List<Order> findAllCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if(orderSearch.getOrderState() !=null){
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderState());
            criteria.add(status);
        }
        //회원 이름검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() +"%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
        return query.getResultList();

    }

}
