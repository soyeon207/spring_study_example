# 04 HandlerExceptionResolver 활용

### ExceptionResolver 를 써야 하는 이유 
(기존) 예외가 발생하면 WAS 까지 예외가 던져지고, 오류 페이지를 찾아서 다시 /error 를 호출해야 한다 

(변경) ExceptionResolver 를 사용하면 WAS 까지 가지 않아도 해당 레벨에서 깔끔하게 해결할 수 있다 

### 예시 
```java
@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        try {
            if (ex instanceof UserException) {
                log.info("UserException resolver to 400");
                String acceptHeader = request.getHeader("accept");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

                if ("application/json".equals(acceptHeader)) {
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());
                    response.setContentType("application/json");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(objectMapper.writeValueAsString(errorResult));
                    return new ModelAndView();
                } else {
                    // TEXT/HTML
                    return new ModelAndView("error/500");
                }
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }
        return null;
    }
}
```

ExceptionHandler 를 사용하면 컨트롤러에서 예외가 발생해도 ExceptionResolver 에서 예외를 처리해버린다.<br>
위 예제에서 `new ModelAndView()` 를 리턴했는데, 저러면 서블릿은 정상 응답이라고 여긴다.<br>
따라서 예외가 발생해도 서블릿 컨테이너까지 예외가 전달되지 않고, 스프링 MVC 에서 예외 처리는 끝이 난다.<br> 

하지만, ExceptionHandler 를 직접 구현하는 건 너무 복잡하다<br> 
👉 스프링에서 제공하는 ExceptionHandler 를 사용하면 된다 