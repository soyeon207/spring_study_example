# 08 @ControllerAdvice

@ExceptionHandler 를 사용해서 예외를 깔끔하게 처리할 수 있지만, 하나의 컨트롤러에 종속되어 있다.<br>
이럴 때 `@ControllerAdvice` 또는 `@RestControllerAdvice` 를 사용하면 분리할 수 있다. 

### RestControllerAdvice

```java
@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {
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
}
```
@RestControllerAdvice 를 사용하여 코드 분리 

### ControllerAdvice
- `@ControllerAdvice` 는 대상으로 지정한 여러 컨트롤러에 `@ExceptionHandler`, `@InitBuilder` 기능을 부여해주는 역할을 한다 
- `@ControllerAdvice` 에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다 (글로벌 적용)
- `@RestControllerAdvice` 는 `@ControllerAdvice` 와 기능이 같고, @ResponseBody 만 추가되어 있다 

### 대상 컨트롤러 지정하기 
```java
// RestController 만 적용
@ControllerAdvice(annotations = RestController.class)

// org.example.controllers 패키지 하위 적용 
@ControllerAdvice("org.example.controllers")

// 부모 클래스 나 컨트롤러 지정 
@ControllerAdvice(assignableTypes = {ControllerInterface.class, AbstractController.class})
```




