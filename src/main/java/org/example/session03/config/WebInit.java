package org.example.session03.config;

import org.jspecify.annotations.NullMarked;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

@NullMarked
public class WebInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
