package com.cheng.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Author: Cheng
 * Date: 2016/1/21 0021
 */
public class ServletHeader extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(ServletHeader.class);
    private String message;

    public void init() throws ServletException {
        // Do required initialization
        //message = "Hello World";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        log.info("执行doGet方法！！");

        String message = request.getParameter("msg");

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
        out.println("  <BODY>");
        out.println("<h1>你好==：" + message + "</h1>");
        out.println("  </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }

    public void destroy() {
        // do nothing.
    }
}
