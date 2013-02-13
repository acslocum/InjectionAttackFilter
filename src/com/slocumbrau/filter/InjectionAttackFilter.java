package com.slocumbrau.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class InjectionAttackFilter implements Filter {
  public static final String FILTER_XSS_PARAM_NAME = "filter_xss";
  public static final String FILTER_SQL_INJECTION_PARAM_NAME = "filter_sql_injection";
  boolean filterXSS = true;
  boolean filterSQL = true;
  
  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    InjectionAttackWrapper wrapper = new InjectionAttackWrapper((HttpServletRequest) servletRequest, filterXSS, filterSQL);
    filterChain.doFilter(wrapper, servletResponse);
  }

  @Override
  public void init(FilterConfig config) throws ServletException {
    String filterXSSParam = config.getInitParameter(FILTER_XSS_PARAM_NAME);
    String filterSQLParam = config.getInitParameter(FILTER_SQL_INJECTION_PARAM_NAME);
    filterXSS = new Boolean(filterXSSParam);
    filterSQL = new Boolean(filterSQLParam);
  }

}
