<html>
<head>
    <title>Reading Cookies</title>
</head>

<body>
<p>Content of database-cart: ${cart}</p>

<!-- TODO: Task 5 - Show the cart items from cookies -->

<%
    // TODO: Task 5 - Set cookies from parameter
    Cookie[] cookies = request.getCookies();

    for (Cookie cookie : cookies) {
        if (!cookie.getName().equals("JSESSIONID")) {
            out.println(cookie.getValue() + " --- ");
        }
    }
%>

<form action = "/set" method = "POST">
    Item to add: <input type = "text" name = "item"><br />
    <input type = "submit" value = "Submit" />
</form>

</body>

</html>