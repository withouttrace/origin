package com.blue.sys.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.blue.sys.factory.PryFactory;

public class MessageFromAuthorServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) {
        HttpSession session = request.getSession();
        String pname = (String) session.getAttribute("user");
        String messagee = request.getParameter("messagee");

        Map<String, String> value = new HashMap<String, String>();

        value.put("user", pname);
        value.put("messagee", messagee);

        try{
            if(PryFactory.getPryInfoUpdateDao().doAuthorUpdate(value)){
                response.sendRedirect("/sys/success/messageSuccessAuthor.jsp");
            }
        } catch(SQLException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        } catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) {
        doGet(request, response);
    }
}