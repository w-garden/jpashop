package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)//스프링과 함께 실행한다는 의미
@SpringBootTest //테스트시 스프링부트 실행
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {

        //given
        Member member= new Member();
        member.setName("shin");

        //when
        Long saveid = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(saveid));

    }

    @Test(expected = IllegalStateException.class)
    //(expected = IllegalStateException.class) -> 아래의 try catch 문을 없앨 수 있음
    public void 중복_회원_예외() throws  Exception {
        //given
        Member member1 = new Member();
        member1.setName("shin");
        
        Member member2= new Member();
        member2.setName("shin");
        //when
        memberService.join(member1);
        memberService.join(member2);


/*
        try{
            memberService.join(member2); //예외가 발생해야 한다
        }catch (IllegalStateException e){
            return;
        }
*/
        //then
        fail("예외가 발생해야 한다");
    }
}