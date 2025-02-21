package com.bahadir.pos.security;

//@Component
//public class RequestResponseFilter implements Filter {
//
//    private final ApiLogService apiLogService;
//
//    @Autowired
//    public RequestResponseFilter(ApiLogService apiLogService) {
//        this.apiLogService = apiLogService;
//    }
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) request;
//
//        // Request parametrelerini alıyoruz
//        String requestParams = getRequestParameters(httpRequest);
//        String requestUri = httpRequest.getRequestURI();
//        String method = httpRequest.getMethod();
//
//        // Kullanıcı bilgilerini alalım
//        String username = httpRequest.getUserPrincipal() != null ? httpRequest.getUserPrincipal().getName() : "anonymous";
//        String sessionId = httpRequest.getSession().getId();
//
//        // ResponseWrapper ile response body'yi yakalayalım
//        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
//
//        // Filtreyi çalıştırıyoruz
//        chain.doFilter(request, responseWrapper);
//
//        // Response verisini aldıktan sonra işleme devam edebilirsiniz
//        byte[] responseBody = responseWrapper.getResponseData();
//        int status = ((HttpServletResponse) response).getStatus();
//
//        // Loglama işlemi
//        apiLogService.saveApiLog(sessionId, username, requestUri, method, requestParams, new String(responseBody), status);
//
//        // Yanıtı doğru şekilde yazmamız gerekiyor
//        response.getOutputStream().write(responseBody);
//    }
//
//
//    private String getRequestParameters(HttpServletRequest request) {
//        // URL parametrelerini alıyoruz
//        StringBuilder params = new StringBuilder();
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        Set<Map.Entry<String, String[]>> entrySet = parameterMap.entrySet();
//
//        for (Map.Entry<String, String[]> entry : entrySet) {
//            String key = entry.getKey();
//            String[] values = entry.getValue();
//            for (String value : values) {
//                params.append(key).append("=").append(value).append("&");
//            }
//        }
//
//        // Sonundaki "&" işaretini temizliyoruz
//        if (params.length() > 0) {
//            params.setLength(params.length() - 1);
//        }
//
//        return params.toString();
//    }
//}
