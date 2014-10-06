package com.slocumbrau.filter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.mockito.Mockito;

public class InjectionAttackFilterTest {
  @Test
  public void shouldSetFilterXSSToTrueWhenParamIsSetTrue() throws Exception {
	FilterConfig filterConfig = Mockito.mock(FilterConfig.class);
	Mockito.when(filterConfig.getInitParameter(InjectionAttackFilter.FILTER_XSS_PARAM_NAME)).thenReturn("true");
	InjectionAttackFilter filter = new InjectionAttackFilter();
	filter.init(filterConfig);
	assertTrue(filter.filterXSS);
  }

  @Test
  public void shouldSetFilterXSSToFalseWhenParamIsSetFalse() throws Exception {
	FilterConfig filterConfig = Mockito.mock(FilterConfig.class);
	Mockito.when(filterConfig.getInitParameter(InjectionAttackFilter.FILTER_XSS_PARAM_NAME)).thenReturn("false");
	InjectionAttackFilter filter = new InjectionAttackFilter();
	filter.init(filterConfig);
	assertFalse(filter.filterXSS);
  }

  @Test
  public void shouldSetFilterSQLToTrueWhenParamIsSetTrue() throws Exception {
	FilterConfig filterConfig = Mockito.mock(FilterConfig.class);
	Mockito.when(filterConfig.getInitParameter(InjectionAttackFilter.FILTER_SQL_INJECTION_PARAM_NAME)).thenReturn("true");
	InjectionAttackFilter filter = new InjectionAttackFilter();
	filter.init(filterConfig);
	assertTrue(filter.filterSQL);
  }

  @Test
  public void shouldSetFilterSQLToFalseWhenParamIsSetFalse() throws Exception {
	FilterConfig filterConfig = Mockito.mock(FilterConfig.class);
	Mockito.when(filterConfig.getInitParameter(InjectionAttackFilter.FILTER_SQL_INJECTION_PARAM_NAME)).thenReturn("false");
	InjectionAttackFilter filter = new InjectionAttackFilter();
	filter.init(filterConfig);
	assertFalse(filter.filterSQL);
  }
  
  @Test
  public void shouldAddXFrameOptionsHeaderWhenNotPresent() throws Exception {
      FilterConfig filterConfig = Mockito.mock(FilterConfig.class);
      Mockito.when(filterConfig.getInitParameter(InjectionAttackFilter.CLICK_JACKING_HEADER)).thenReturn("true");
      InjectionAttackFilter filter = new InjectionAttackFilter();
      filter.init(filterConfig);
      HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
      Mockito.when(response.containsHeader("X-FRAME-OPTIONS")).thenReturn(false);
      filter.doFilter(Mockito.mock(HttpServletRequest.class), response, Mockito.mock(FilterChain.class));
      Mockito.verify(response).addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
  }

  @Test
  public void shouldNotAddXFrameOptionsHeaderWhenAlreadyPresent() throws Exception {
      FilterConfig filterConfig = Mockito.mock(FilterConfig.class);
      Mockito.when(filterConfig.getInitParameter(InjectionAttackFilter.CLICK_JACKING_HEADER)).thenReturn("true");
      InjectionAttackFilter filter = new InjectionAttackFilter();
      filter.init(filterConfig);
      HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
      Mockito.when(response.containsHeader("X-FRAME-OPTIONS")).thenReturn(true);
      filter.doFilter(Mockito.mock(HttpServletRequest.class), response, Mockito.mock(FilterChain.class));
      Mockito.verify(response, Mockito.never()).addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
  }

  @Test
  public void shouldNotAddXFrameOptionsHeaderWhenDisabled() throws Exception {
      FilterConfig filterConfig = Mockito.mock(FilterConfig.class);
      Mockito.when(filterConfig.getInitParameter(InjectionAttackFilter.CLICK_JACKING_HEADER)).thenReturn("false");
      InjectionAttackFilter filter = new InjectionAttackFilter();
      filter.init(filterConfig);
      HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
      Mockito.when(response.containsHeader("X-FRAME-OPTIONS")).thenReturn(false);
      filter.doFilter(Mockito.mock(HttpServletRequest.class), response, Mockito.mock(FilterChain.class));
      Mockito.verify(response, Mockito.never()).addHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
  }

  @Test
  public void falseShouldParseFalse() {
	assertFalse( new Boolean("false") );
  }

  @Test
  public void trueShouldParseTrue() {
	assertTrue( new Boolean("true") );
  }

}