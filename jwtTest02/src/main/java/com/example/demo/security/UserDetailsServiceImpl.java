package com.example.demo.security;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;

// 스프링시큐리티 인증처리를 위하여 UserDetailsService를 구현한 클래스를 만들어 준다.
// @Service 애너테이션은 이 클래스가 서비스 레이어의 컴포넌트임을 나타냅니다. 
// UserDetailsService 인터페이스를 구현하여 사용자 인증 정보를 제공하는 역할을 합니다.
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	// 데이터베이스로 부터 회원의 정보를 갖고 오기 위하여 Repository를 선언한다.
	// @Autowired 애너테이션을 사용하여 MemberRepository를 주입받습니다. 
	// 이 리포지토리는 데이터베이스에서 회원 정보를 조회하는 데 사용됩니다.
	@Autowired
	private MemberRepository memberRepository;	
	

	// 이 메소드는 사용자가 입력한 아이디(username)를 기반으로 사용자 정보를 로드합니다. 
	// Spring Security가 사용자 인증을 위해 이 메소드를 자동으로 호출합니다.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// MemberRepository를 사용하여 데이터베이스에서 사용자 아이디로 회원 정보를 조회합니다.
		// findById 메소드는 Optional을 반환하므로, orElse(null)를 사용하여 해당 회원이 없으면 null을 반환합니다.
		// Member member = memberRepository.findById(username).get();
		Member member = memberRepository.findById(username).orElse(null);
				
		// 조회된 회원 정보가 없으면 UsernameNotFoundException 예외를 발생시켜 인증 실패를 처리합니다.
        if (member == null) {
            throw new UsernameNotFoundException("User not found");
        }
        
        // 해당회원이 있으면 시큐리티 인증완료를 위하여 UserDetails 객체를 생성하여 반환.
        // 이 객체는 Spring Security가 사용자 인증과 권한 부여를 위해 사용하는 사용자 정보입니다
        return org.springframework.security.core.userdetails.User
                .withUsername(member.getId()) //사용자 아이디를 설정합니다.
                .password(member.getPassword()) //사용자의 비밀번호를 설정합니다.
                .authorities(member.getRole()) //사용자의 권한을 설정합니다.
                .build();
	}
}


/*
해당 회원이 없으면 예외발생 [람다식]
@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				
		Member member = memberRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
				
        return org.springframework.security.core.userdetails.User
                .withUsername(member.getId())
                .password(member.getPassword())
                .authorities(member.getRole())
                .build();
	}
	

		
	해당 회원이 없으면 예외발생 [익명클래스 이용]
	Member member = memberRepository.findById(username).orElseThrow(new Supplier<UsernameNotFoundException>() {
    @Override
    public UsernameNotFoundException get() {
        return new UsernameNotFoundException("User not found");
    }
});
	
	해당 회원이 없으면 예외발생 [일반 조건문을 사용하는 경우]
	Optional<Member> optionalMember = memberRepository.findById(username);
	if (!optionalMember.isPresent()) {
	    throw new UsernameNotFoundException("User not found");
	}
	Member member = optionalMember.get();

 */


