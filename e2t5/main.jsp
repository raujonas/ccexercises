<%@ page import = "java.io.*,java.util.*" %>

<html>
   <head>
      <title>e2t5</title>
   </head>
   
   <body>
      <%
         Integer hits = (Integer)application.getAttribute("hits");
         if( hits ==null || hits == 0 ) {
            hits = 1;
         } else {
            hits += 1;
         }
         application.setAttribute("hits", hits);
      %>
      <center>
         <p>Total number of visits: <%= hits%></p>
      </center>
   
   </body>
</html>