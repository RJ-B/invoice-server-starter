package cz.itnetwork.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor sloužící k měření doby zpracování HTTP požadavku.
 *
 * Slouží k logování odezvy backendu na jednotlivé API volání.
 */
@Component
public class RequestTimingInterceptor implements HandlerInterceptor {

    private static final Logger log =
            LoggerFactory.getLogger(RequestTimingInterceptor.class);

    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        // Uložení času zahájení zpracování požadavku
        request.setAttribute(START_TIME, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {

        Long startTime = (Long) request.getAttribute(START_TIME);
        if (startTime == null) {
            return;
        }

        long duration = System.currentTimeMillis() - startTime;

        log.info(
                "Request [{} {}] completed in {} ms (status {})",
                request.getMethod(),
                request.getRequestURI(),
                duration,
                response.getStatus()
        );
    }
}
