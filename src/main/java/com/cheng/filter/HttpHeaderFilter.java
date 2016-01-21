package com.cheng.filter;


import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Cheng on 2016/1/21 0021.
 */
public class HttpHeaderFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(HttpHeaderFilter.class);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Servlet======init=====");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest reqe = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if("POST".equals(reqe.getMethod())) {
            chain.doFilter(reqe,resp);
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ETagResponseWrapper responseWrapper = new ETagResponseWrapper(resp,baos);
        chain.doFilter(reqe,responseWrapper);

        byte[] bytes = baos.toByteArray();
        String token = DigestUtils.md5Hex(reqe.getInputStream());
        resp.setHeader("ETag",token);
        String previousToken = reqe.getHeader("If-None-Match");

        if(previousToken!=null && previousToken.equals(token)) {
            log.info("ETag match: reteuning 304 NOT MoDIFIED");
            //resp.sendError(HttpServletResponse.SC_NOT_MODIFIED);
            resp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            String ims = reqe.getHeader("If-Modified-Since");
            resp.setHeader("Last-Modifide", ims);
        }else {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.MILLISECOND, 0);
            Date lastModified = cal.getTime();
            resp.setDateHeader("Last-Modified", lastModified.getTime());
            log.info("Writing body content");
            ServletOutputStream sos = resp.getOutputStream();
            sos.write(bytes);
            resp.setContentLength(bytes.length);
            sos.flush();
            sos.close();
        }
    }

    @Override
    public void destroy() {
        System.out.println("Servlet======destroy=====");
    }
}
