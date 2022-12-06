package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {


    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional //엔티티 변경이 있을때는 트랜잭션이 꼭 전제되어져야함
    @Rollback(false)
    public void testMember(){
        Member member = new Member();
        member.setUsername("memberA");

        Long saveId = memberRepository.save(member);

        Member findMember = memberRepository.find(saveId);

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장
    }

}