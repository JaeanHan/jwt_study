# jwt easy version
jwt를 사용하는 이유 (key points)
<br/>
1. 세션의 단점 극복 (load balancing 사용시)
2. 웹 통신(TCP) CIA 보장 (confidentiality, integrity, availability)
3. RSA (publick Key는 private key로 해독 => 암호화, private key는 public key로 해독 => 인증) 
4. RFC 7519 웹표준임
5. header, payload, signiture 구조
6. 사용하는 Base64는 decode 가능 (비밀성 보장이 아닌 무결성을 보장하는 용도)

## jwt config 설정
### SecurityConfig extends WebSecurityConfigurerAdapter
1. 클래스 만들고
### configure(HttpSecurity http)
2. 메소드 오버라이드해서
### http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
3. http객체로 세부 설정을 한다. Spring Security를 써도 세션 사용 안하니까 모든 페이지 접근 가능
### .formLogin().disable()
4. form 태그 로그인 안되게 하고 (Ajax 요청 할 거니까)
### .httpBasic().disable()
5. Bearer 방식 사용 할 거니까 Basic disable 설정 후
### .addFilter(new JwtAuthenticationFiler(authenticationManager()))
6. jwt 인증 필터 추가
### .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
7. jwt 권한 필터 추가
<hr/>

#### JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter

<br/>

##### attemptAuthentication 메소드 :
유저 정보를 세션에 등록할 때 사용

<br/>

UsernamePasswordAuthenticationToken에 username, password 담아서
authenticationManager 객체를 통해 아이디 비밀번호 비교 및 Authentication 객체 생성
해당 객체 리턴하면 Security Session에 등록됨 (jwt 쓰면 세션 안써도 되는데 권한 관리 때문에 사용함)

<br/>

##### successfulAuthentication 메소드 :
로그인 성공해서 Security Session에 Authentication 객체가 등록되면 실행됨

<br/>

등록된 Authentication 객체를 통해 Jwt 생성후 response header에 jwt 주입

<br/>

#### JwtAuthorizationFilter extends BasicAuthenticationFilter

<br/>

##### doFilterInternal 메소드 :
인증이나 권한이 필요한 주소요청이 있을 때 해당 메소드를 거쳐감

<br/>

해당 메소드에서 jwt 토큰을 검사하고 유효하다면 토큰 내부의 username으로 강제로 로그인 시킴

<hr/>

#### Authentication is used by a server when the server needs to know exactly who is accessing their information or site.
#### Authorization is a process by which a server determines if the client has permission to use a resource or access a file.
