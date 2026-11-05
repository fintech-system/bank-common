package kz.fintech.bank.common.autoconfigure;

import kz.fintech.bank.common.error.ErrorResponseFactory;
import kz.fintech.bank.common.exception.GlobalExceptionHandler;
import kz.fintech.bank.common.exception.SecurityExceptionHandler;
import kz.fintech.bank.common.logging.RequestLoggingFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class BankCommonAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ErrorResponseFactory errorResponseFactory(MessageSource messageSource) {
        return new ErrorResponseFactory(messageSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler(ErrorResponseFactory factory, MessageSource messageSource) {
        return new GlobalExceptionHandler(factory, messageSource);
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestLoggingFilter requestLoggingFilter() {
        return new RequestLoggingFilter();
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(name = "org.springframework.security.access.AccessDeniedException")
    static class SecurityHandlerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public SecurityExceptionHandler securityExceptionHandler(ErrorResponseFactory factory) {
            return new SecurityExceptionHandler(factory);
        }
    }
}
