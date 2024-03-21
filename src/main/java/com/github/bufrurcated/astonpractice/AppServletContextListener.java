package com.github.bufrurcated.astonpractice;

import com.github.bufrurcated.astonpractice.api.DepartmentServlet;
import com.github.bufrurcated.astonpractice.api.EmplDepartServlet;
import com.github.bufrurcated.astonpractice.api.EmployeeServlet;
import com.github.bufrurcated.astonpractice.api.PhoneNumberServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServlet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@WebListener
public class AppServletContextListener implements ServletContextListener  {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("START APPLICATION!!!");
        var context = new ClassPathXmlApplicationContext("applicationContext.xml");
        var servletContext = sce.getServletContext();

        for (ServletInfo servletInfo : getServletInfos()) {
            servletRegistration(context, servletContext, servletInfo);
        }
    }

    private Set<ServletInfo> getServletInfos() {
        Set<ServletInfo> servletInfos = new HashSet<>();
        servletInfos.add(new ServletInfo(DepartmentServlet.class, "/api/v1/department/*"));
        servletInfos.add(new ServletInfo(EmplDepartServlet.class, "/api/v1/employee-department"));
        servletInfos.add(new ServletInfo(EmployeeServlet.class, "/api/v1/employees/*"));
        servletInfos.add(new ServletInfo(PhoneNumberServlet.class, "/api/v1/phone-numbers/*"));
        return servletInfos;
    }

    record ServletInfo(Class<? extends HttpServlet> servletClass, String... mapping){}

    private void servletRegistration(ClassPathXmlApplicationContext context, ServletContext servletContext, ServletInfo servletInfo) {
        var departmentServlet = context.getBean(servletInfo.servletClass());
        var name = toLowerCaseFirstLetter(departmentServlet.getClass().getSimpleName());
        var dynamic = servletContext.addServlet(name, departmentServlet);
        dynamic.addMapping(servletInfo.mapping());
    }

    private String toLowerCaseFirstLetter(String name) {
        char[] c = name.toCharArray();
        c[0] += 32;
        return new String(c);
    }
}