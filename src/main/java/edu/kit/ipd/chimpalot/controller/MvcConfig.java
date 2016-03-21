package edu.kit.ipd.chimpalot.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Sets up all view controllers.
 * 
 * @author Thomas Friedel
 *
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {
	
	@Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        //TODO add requester and (maybe) assignment and preview
    }
}
