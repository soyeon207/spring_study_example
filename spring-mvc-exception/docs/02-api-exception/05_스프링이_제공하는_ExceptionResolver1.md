# 05 스프링이 제공하는 ExceptionResolver 1

### 스프링에서 제공하는 ExceptionResolver 
HandlerExceptionResolverComposite 에 다음 순서로 등록되어 있다. 
1. ExceptionHandlerExceptionResolver
2. ResponseStatusExceptionResolver
3. DefaultHandlerExceptionResolver 

**ExceptionHandlerExceptionResolver**<br>
`@ExceptionHanlder` 를 처리한다. API 예외 처리는 대부분 이 기능으로 해결 

**ResponseStatusExceptionResolver**<br>
HTTP 상태 코드를 지정해준다. <br>
EX) @ResponseStatus(value = HttpStatus.NOT_FOUND)

**DefaultHandlerExceptionResolver**<br>
스프링 내부 기본 예외를 처리해준다. 

1 > 2 > 3 번 순으로 예외 처리를 시도한다. 

### ResponseStatusExceptionResolver
예외에 따라서 HTTP 상태 코드를 지정해주는 역할 

1. @ResponseStatus 가 달려 있는 예외 
2. ResponseStatusException 예외 

예외에 다음과 같이 `@ResponseStatus` 어노테이션을 적용해주면 HTTP 상태 코드를 변경해준다. 
```java
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 요청 오류")
public class BadRequestException extends RuntimeException {
}
```
내부적을 코드를 확인해보면 결국 response.sendError 를 호출하는 것을 확인 할 수 있다. 

**메세지 소스에서 찾기**
message.properties 
```properties
error.bad=잘못된 요청 오류 입니다. 메시지 사용 
```

```java
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException {
}
```

**ResponseStatusException**<br>
@ResponseStatus 는 개발자가 직접 변경할 수 있는 예외에만 적용할 수 있다.<br>
스프링에서 기본으로 제공해주는 예외 같은 경우에는 사용할 수 없다<br>
그럴 때 ResponseStatusException 예외를 사용하면 된다. 

```java
@GetMapping("/api/response-status-ex2")
public String responseStatusEx2() {
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
}
```


