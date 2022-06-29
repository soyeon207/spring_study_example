# 07 @ExceptionHandler

### HTML 화면 오류 vs API 오류 
웹 브라우저에 HTML 화면 오류가 발생한 경우 👉 BasicErrorController 를 사용하는게 편하다.<br>
이때는 단순히 4xx, 5xx 오류 화면을 보여주기만 하면 되기 때문에 

하지만,, API 오류는 시스템마다 응답 및 스펙이 모두 다르고, 단순히 오류 화면을 보여주는게 아니라 다른 예외 응답 및 데이터를 출력해야 할 수 도 있기 때문에 더욱 세밀한 제어가 필요하다. 

### API 예외처리의 어려운 점 
1. HandlerExceptionResolver 는 ModelAndView 를 반환해야 한다. 
2. API 응답을 위해서 HttpServletResponse 에 직접 응답 데이터를 넣어줘야 한다 
3. 특정 컨트롤러에서만 발생하는 예외를 별도로 처리하기 어렵다. 

### @ExceptionHandler
그렇기 때문에 스프링은 API 예외 처리 문제를 해결하기 위해 `@ExceptionHandler` 라는 어노테이션을 통해 매우 편리한 예외 처리 기능을 제공한다.<br> 
이게 바로 **ExceptionHandlerExceptionResolver**<br>
기본으로 제공하고, ExceptionResolver 중에서 우선순위도 가장 높다 

```java
@ResponseStatus(HttpStatus.BAD_REQUEST)
@ExceptionHandler(IllegalArgumentException.class)
public ErrorResult illegalExHandler(IllegalArgumentException e) {
    // ApiExceptionV2Controller 에서 발생한 오류만 처리해줌.

    // Controller > DispatcherServlet > ExceptionResolver
    // 정상적으로 리턴되기 때문에 status = 400 으로 설정 (ResponseStatus)
    log.error("[exceptionHandler] ex", e);
    return new ErrorResult("BAD", e.getMessage());
}

@ExceptionHandler
public ResponseEntity<ErrorResult> userExHandler(UserException e) {
    log.error("[exceptionHandler] ex", e);
    ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
    return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
}

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
@ExceptionHandler
public ErrorResult exHandler(Exception e) {
    log.error("[exceptionHandler] ex",e);
    return new ErrorResult("EX", "내부 오류");
}
```

`@ExceptionHandler` 어노테이션을 선언하고, 해당 컨트롤러에서 처리하고 싶은 예외를 지정해주면 사용할 수 있다.<br>
해당 컨트롤러에서 예외가 발생하면 메소드가 호출된다. 

**① 우선순위**<br>
항상 자세한 것이 우선권을 가진다.<br>
EX) RuntimeException 과 IndexOutOfBoundsException 가 있을 때 IndexOutOfBoundsException 가 RuntimeException 보다 우선권을 가짐. 

**② 다양한 예외**<br>
한번에 여러 예외를 처리할 수 있다. 
```java
@ExceptionHandler(AException.class, BException.class)
public String ex(Exception e) {
        ...    
}
```

**③ 예외 생략**<br>
@ExceptionHandler 의 예외를 생략할 수 있다 
```java
@ExceptionHandler
public ResponseEntity<ErrorResult> userExHandler(UserException e) {}
```

**④ 파라미터와 응답**<br>
정말 다양한 파라미터와 응답을 지정할 수 있다.<br>
https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-exceptionhandler-args