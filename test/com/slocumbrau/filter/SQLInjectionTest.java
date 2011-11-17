package com.slocumbrau.filter;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

public class SQLInjectionTest {
  private final String SQL_INJECTION_ENCODED = "%27%20%7C%7C%20%27foo";
  private final String SQL_INJECTION_UNENCODED = "' || 'foo";

  private InjectionAttackFilter wrapper;
  private HttpServletRequest request;

  @Before
  public void setUp() throws Exception {
    request = Mockito.mock(HttpServletRequest.class);
    wrapper = new InjectionAttackFilter(request);
  }

  @Test
  public void shouldCatchSqlStrings() {
    assertEquals("", wrapper.filterParamString("'select * from users;--"));
  }

  @Test
  public void shouldFilterEncodedPipes() {
    assertEquals("%27%20%20%27foo", wrapper.filterParamString(SQL_INJECTION_ENCODED));
  }

  @Test
  public void shouldFilterUnencodedPipes() {
    assertEquals("'  'foo", wrapper.filterParamString(SQL_INJECTION_UNENCODED));
  }

}