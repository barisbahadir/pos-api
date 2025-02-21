package com.bahadir.pos.security;

//@Component
//@Order(2) // Spring Security’den sonra çalıştır
//public class LoggingFilter extends OncePerRequestFilter {
//
//    private final ApiLogService apiLogService;
//    private final JwtTokenProvider jwtTokenProvider;
//    public LoggingFilter(ApiLogService apiLogService, JwtTokenProvider jwtTokenProvider) {
//        this.apiLogService = apiLogService;
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        // Response'u sarmalayan wrapper
//        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
//
//        filterChain.doFilter(request, wrappedResponse);
//
//        // Yalnızca belirli endpoint’leri logla (opsiyonel)
//        if (request.getRequestURI().startsWith("/api/") &&
//                !request.getRequestURI().startsWith("/api/log") &&
//                !request.getRequestURI().startsWith("/error") &&
//                response.getStatus() != HttpStatus.OK.value()
//        ) {
//            byte[] responseArray = wrappedResponse.getContentAsByteArray();
//            String responseBody = new String(responseArray, StandardCharsets.UTF_8);
//
//            String requestUri = request.getRequestURI();
//            String method = request.getMethod();
//
//            // Kullanıcı bilgilerini alalım
//            String username = request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "Anonymous";
//            String token = ApiUtils.getJwtFromRequest(request);
//            apiLogService.saveApiLog(token, username, requestUri, method, responseBody, response.getStatus());
//        }
//
//        // Response'u geri yaz
//        wrappedResponse.copyBodyToResponse();
//    }
//}

