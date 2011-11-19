package com.slocumbrau.filter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import javax.servlet.FilterConfig;

import org.junit.Before;
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
  public void falseShouldParseFalse() {
	assertFalse( new Boolean("false") );
  }

  @Test
  public void trueShouldParseTrue() {
	assertTrue( new Boolean("true") );
  }

}