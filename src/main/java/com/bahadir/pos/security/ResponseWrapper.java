package com.bahadir.pos.security;

//public class ResponseWrapper extends HttpServletResponseWrapper {
//
//    private ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//    private PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream));
//
//    public ResponseWrapper(HttpServletResponse response) {
//        super(response);
//    }
//
//    @Override
//    public ServletOutputStream getOutputStream() throws IOException {
//        return new ServletOutputStream() {
//            @Override
//            public void write(int b) throws IOException {
//                outputStream.write(b);
//            }
//
//            @Override
//            public void setWriteListener(WriteListener writeListener) {
//                // Bu metodu boş bırakabilirsiniz
//            }
//
//            @Override
//            public boolean isReady() {
//                return true;
//            }
//        };
//    }
//
//    @Override
//    public PrintWriter getWriter() throws IOException {
//        return writer;
//    }
//
//    public byte[] getResponseData() {
//        writer.flush();
//        return outputStream.toByteArray();
//    }
//}
