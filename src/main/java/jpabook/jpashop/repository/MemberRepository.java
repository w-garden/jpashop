package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository //repository? 엔티티를 찾아주는 역할
public class MemberRepository {

    @PersistenceContext //스프링 부트에서는 이 어노테이션으로 Jpa엔티티를 관리
    EntityManager em;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Member find(Long id){
        return em.find(Member.class,id);
    }
}
