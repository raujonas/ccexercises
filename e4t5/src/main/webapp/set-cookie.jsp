<%
    // TODO: Task 5 - Set cookies from parameter
    Cookie[] cookies = request.getCookies();
    Cookie newCookie = new Cookie(request.getParameter("item"), request.getParameter("item"));

    for (Cookie cookie : cookies) {
        response.addCookie(cookie);
    }
    response.addCookie(newCookie);
%>

<html>
<head>
    <title>Setting Cookies</title>
</head>

<body>

<a href="/">Back</a>

</body>
</html>