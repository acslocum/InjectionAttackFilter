package com.slocumbrau.filter;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class ConfigurationParametersTest {
  private InjectionAttackWrapper wrapper;
  private HttpServletRequest request;
  String SQL_INJECTION = "'select * from users;--";
  String XSS_INJECTION = " onMouseOver=alert('today');";

  @Before
  public void setUp() throws Exception {
    request = Mockito.mock(HttpServletRequest.class);
  }

  @Test
  public void shouldNotFilterSqlIfSqlParamIsFalse() {
    wrapper = new InjectionAttackWrapper(request, true, false);
    assertEquals(SQL_INJECTION, wrapper.filterParamString(SQL_INJECTION));
  }

  @Test
  public void shouldFilterXSSIfSqlParamIsFalse() {
    wrapper = new InjectionAttackWrapper(request, true, false);
    assertEquals("", wrapper.filterParamString(XSS_INJECTION));
  }

  @Test
  public void shouldNotFilterEventHandlersIfXSSParamIsFalse() {
    wrapper = new InjectionAttackWrapper(request, false, true);
    assertEquals(XSS_INJECTION, wrapper.filterParamString(XSS_INJECTION));
  }

  @Test
  public void shouldFilterSqlIfXSSParamIsFalse() {
    wrapper = new InjectionAttackWrapper(request, false, true);
    assertEquals("", wrapper.filterParamString(SQL_INJECTION));
  }

}