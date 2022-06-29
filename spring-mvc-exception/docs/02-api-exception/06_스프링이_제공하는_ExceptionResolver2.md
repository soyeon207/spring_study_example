# 06 스프링이 제공하는 ExceptionResolver2

DefaultHandlerExceptionResolver 에 대해서 알아보자. 

DefaultHandlerExceptionResolver 는 스프링 내부에서 발생하는 스프링 예외를 해결한다.<br>
EX) 파라미터 바인딩 시점에 타입이 맞지 않으면 내부적으로 발생하는 exception 인 TypeMismatchException 의 경우 그냥 두면 서블릿 컨테이너까지 오류가 올라가고, 결과적으로 500 오류가 발생한다.<br>
하지만 TypeMismatchException 은 보통 클라이언트가 요청을 잘못해서 발생하는 예외라서 400 오류로 리턴해줘야 하기 때문에 DefaultHandlerExceptionResolver 가 변경해준다.


### 코드 확인 
```java
@GetMapping("/api/default-handler-ex")
public String defaultException(@RequestParam Integer data) {
    return "ok";
}
```
/api/default-handler-ex 라는 API 가 있을 때 data 를 String 으로 넘겨주면 TypeMismatchException 가 발생 
```json
{
    "timestamp": "2022-06-29T16:09:00.802+00:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/api/default-handler-ex"
}
```


DefaultHandlerExceptionResolver 에 doResolveException 메소드에서
```java
if (ex instanceof TypeMismatchException) {
    return this.handleTypeMismatch((TypeMismatchException)ex, request, response, handler);
}

protected ModelAndView handleTypeMismatch(TypeMismatchException ex, HttpServletRequest request, HttpServletResponse response, @Nullable Object handler) throws IOException {
    response.sendError(400);
    return new ModelAndView();
}
```
response 를 400으로 변경해준다. 

하지만, DefaultHandlerExceptionResolver 와 ResponseStatusExceptionResolver 는 response 에 데이터를 직접 넣어줘야 하고, ModelAndView 를 반환해야 하기 때문에 매우 번거롭다. 스프링은 이 문제를 해결하기 위해 ⭐️ **ExceptionHandlerExceptionResolver** 를 제공한다. 