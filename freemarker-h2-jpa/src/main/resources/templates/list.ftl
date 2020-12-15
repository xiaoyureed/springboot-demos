<html>
<body>
    <h1>student list</h1>
    <ul>
        <#list students as stu>
            <ol>${stu.name}, ${stu.birth}, ${stu.createDate}, ${stu.gender}</ol>
        </#list>
    </ul>
</body>
</html>