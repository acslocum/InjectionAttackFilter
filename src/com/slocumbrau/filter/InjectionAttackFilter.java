package com.slocumbrau.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InjectionAttackFilter implements Filter {
  private static final String X_FRAME_VALUE = "SAMEORIGIN";
private static final String X_FRAME_HEADER = "X-FRAME-OPTIONS";
public static final String FILTER_XSS_PARAM_NAME = "filter_xss";
  public static final String FILTER_SQL_INJECTION_PARAM_NAME = "filter_sql_injection";
  public static final String CLICK_JACKING_HEADER = "click_jacking_header";
  boolean filterXSS = true;
  boolean filterSQL = true;
  boolean clickJacking = true;
  
  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    InjectionAttackWrapper wrapper = new InjectionAttackWrapper((HttpServletRequest) servletRequest, filterXSS, filterSQL);
    filterClickJack(servletResponse);
    filterChain.doFilter(wrapper, servletResponse);
  }

  private void filterClickJack(ServletResponse servletResponse) {
    if(clickJacking) {
        if(servletResponse instanceof HttpServletResponse) {
        HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;
            if(!httpServletResponse.containsHeader(X_FRAME_HEADER)) {
                httpServletResponse.addHeader(X_FRAME_HEADER, X_FRAME_VALUE);
            }
        }
    }
  }

@Override
  public void init(FilterConfig config) throws ServletException {
    String filterXSSParam = config.getInitParameter(FILTER_XSS_PARAM_NAME);
    String filterSQLParam = config.getInitParameter(FILTER_SQL_INJECTION_PARAM_NAME);
    String clickJackingParam = config.getInitParameter(CLICK_JACKING_HEADER);
    filterXSS = new Boolean(filterXSSParam);
    filterSQL = new Boolean(filterSQLParam);
    clickJacking = new Boolean(clickJackingParam);
  }

}
