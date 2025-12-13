package cz.itnetwork.configuration;

import cz.itnetwork.logging.RequestTimingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Webová konfigurace aplikace.
 *
 * Registruje HTTP interceptory.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestTimingInterceptor timingInterceptor;

    public WebConfig(RequestTimingInterceptor timingInterceptor) {
        this.timingInterceptor = timingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timingInterceptor)
                .addPathPatterns("/api/**");
    }
}
