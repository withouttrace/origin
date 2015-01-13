package org.blue.sys.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.blue.sys.factory.PryFactory;

public class AdminCheckServlet extends HttpServlet {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        int essayId = Integer
                .parseInt((String) session.getAttribute("essayId"));
        String useMark = request.getParameter("useMark");
        String checkTo = request.getParameter("checkTo");

        if("not".equals(checkTo)){
            useMark += "---not";

            if(0 != essayId && null != useMark){
                try{
                    if(PryFactory.getPryEssayCheckDao().checkMarkFromAdmin(essayId, useMark)){
                        response.sendRedirect("/sys/host/admin/adminHost.jsp");
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        if("pass".equals(checkTo)){
            if(0 != essayId && null != useMark){
                try{
                    if(PryFactory.getPryEssayCheckDao().checkMarkFromAdmin(essayId, useMark)){
                        response.sendRedirect("/sys/host/admin/adminHost.jsp");
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) {
        doGet(request, response);
    }
}
