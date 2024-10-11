package com.netcoretech.netfaulttracker.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitingFilter implements Filter {

    private static final long ALLOWED_REQUESTS_PER_MINUTE = 60; // 분당 최대 요청 수
    private static final long TIME_FRAME = TimeUnit.MINUTES.toMillis(1); // 1분 시간 간격

    private final Map<String, RequestCount> requestCounts = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String clientIp = httpRequest.getRemoteAddr(); // 클라이언트 IP 주소 가져오기

        long currentTime = System.currentTimeMillis();
        requestCounts.putIfAbsent(clientIp, new RequestCount(0, currentTime));

        RequestCount requestCount = requestCounts.get(clientIp);

        // 일정 시간 내 요청 횟수 초과 시 차단
        if (currentTime - requestCount.timestamp < TIME_FRAME) {
            if (requestCount.count >= ALLOWED_REQUESTS_PER_MINUTE) {
                response.getWriter().write("Too many requests. Please try again later.");
                return;
            } else {
                requestCount.count++;
            }
        } else {
            // 시간 프레임을 넘으면 카운트 초기화
            requestCount.count = 1;
            requestCount.timestamp = currentTime;
        }

        chain.doFilter(request, response); // 요청 처리
    }

    @Override
    public void destroy() {
    }

    // 요청 수와 타임스탬프를 저장하는 클래스
    private static class RequestCount {
        long count;
        long timestamp;

        RequestCount(long count, long timestamp) {
            this.count = count;
            this.timestamp = timestamp;
        }
    }
}
