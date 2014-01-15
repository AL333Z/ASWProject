<%    
    // redirecting to main page
    String site = getServletContext().getContextPath()+"/index.jsp";
    response.setStatus(response.SC_OK);
    response.sendRedirect(site);
    
%>
