package com.springboot.jwt_study.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("필터3");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 토큰을 만들었다고 가정 (코스)
        // id와 pw가 정상적으로 들어와서 로그인이 완료되면 토큰을 만들어주고 그걸 응답해 준다.
        // 요청 할 때 마다 header 내부 Authroization의 value 값으로 토큰을 갖고올 텐데
        // 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰인지 맞는지 검증만 하면 됨.
        if(request.getMethod().equalsIgnoreCase("post")) {
            System.out.println("POST 요청됨");
            String headerAuth = request.getHeader("Authorization");
            System.out.println(headerAuth);

            if(headerAuth.equals("cos")) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        PrintWriter out = response.getWriter();
        out.println("인증 안됨");
    }
}
