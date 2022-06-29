package example.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        // 예외가 넘어오면 정상적인 ModelAndView 로 반환
        try {
            if (ex instanceof IllegalArgumentException) {
                log.info("IllegalArgumentException resolver to 400");
                // IllegalAccessException error 를 400 에러로 리턴
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                // 빈값으로 넘기면 WAS 까지 정상적인 흐름으로 리턴
                return new ModelAndView();
            }
        } catch (IOException e) {
            log.error("resolver ex", e);
        }

        // null 로 리턴하면 예외가 그냥 날라감.
        return null;
    }

}
