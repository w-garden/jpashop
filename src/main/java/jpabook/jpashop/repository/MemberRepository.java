package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository //repository? 엔티티를 찾아주는 역할
@RequiredArgsConstructor
public class MemberRepository {

   // @PersistenceContext
    //스프링 부트에서는 이 어노테이션으로 Jpa엔티티를 관리
    //엔티티 매니저를 주입시켜줌
    //    @PersistenceUnit 를사용하면 엔티티 팩토리를 주입
    private final EntityManager em;

    public void save(Member member){
        em.persist(member);
    }

    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        return em.createQuery("select m from Member m", Member.class).getResultList();
        //JPQL은 객체를 대상으로 쿼리함
    }

    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name= :name", Member.class)
                .setParameter("name",name)
                //:name -> 파라미터를 바인딩
                .getResultList();
    }
}
