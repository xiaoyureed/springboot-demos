```
# rest-api-scaffold

## module structure

code-generator - crud code generator
common - common module
demo

## naming conventions

- DTO 命名规范 : resource name + function number + "Req/Resp/Input/Output" + "M/S"

    - resource name 可能可表对应, 也可能不对应
    
    - function number - F01/F02/F03/F04 基本的增删改查, 可以继续添加
    
    - Req/Resp/Input/Output 对应 请求/响应/sql输入/sql输出, DTO 分为两层, web 层 请求/响应 (Req/Resp), 数据层 输入/输出 (Input/Output)
    
    - M/S 表示响应主对象/子对象, 主对象和子对象是一对多的关系
    
- service 命名: resource name + function number + "Service"

```