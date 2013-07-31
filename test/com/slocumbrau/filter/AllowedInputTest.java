package com.slocumbrau.filter;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AllowedInputTest {
  private final String URL_INPUT6 = "http%3A%2F%2Fimg.slocumbrau.com%2Fis%2Fimage%2Fhere%2Fabcd12345600";
  private final String ADDRESS = "123 o'malley st. apt #4, kona, hi 12345";
  private final String ADDRESS_WITH_SPACE = "123 o' malley st. apt #4, kona, hi 12345";
  private final String DATE_INPUT = "August 30, 2005";
  private final String OTHER_CHARS_INPUT = ".' ,-";

  private InjectionAttackWrapper wrapper;
  private HttpServletRequest request;

  @Before
  public void setUp() throws Exception {
    request = Mockito.mock(HttpServletRequest.class);
    wrapper = new InjectionAttackWrapper(request);
  }

  @Test
  public void shouldAllowValidDate() {
    assertEquals(DATE_INPUT, wrapper.filterParamString(DATE_INPUT));
  }

  @Test
  public void shouldAllowSomePunctuation() {
    assertEquals(OTHER_CHARS_INPUT, wrapper.filterParamString(OTHER_CHARS_INPUT));
  }

  @Test
  public void shouldAllowQuotedAddress() {
    assertEquals(ADDRESS, wrapper.filterParamString(ADDRESS));
  }

  @Test
  public void shouldAllowQuotedAddressWithSpace() {
    assertEquals(ADDRESS_WITH_SPACE, wrapper.filterParamString(ADDRESS_WITH_SPACE));
  }

  @Test
  public void shouldAllowEmailAddress() {
    assertEquals("john@gmail.com", wrapper.filterParamString("john@gmail.com"));
  }

  @Test
  public void shouldNotFilterUnderscore() {
    assertEquals("DONT_KNOW", wrapper.filterParamString("DONT_KNOW"));
  }

  @Test
  public void shouldNotFilterUrlsPassedAsParameters() {
    assertEquals(URL_INPUT6, wrapper.filterParamString(URL_INPUT6));
  }

  @Test
  public void shouldNotFilterColon() {
    assertEquals("a:b", wrapper.filterParamString("a:b"));
  }

  @Test
  public void shouldAllowApostropheAndHyphenNames() {
    assertEquals("mr. john o'malley-sanchez", wrapper.filterParamString("mr. john o'malley-sanchez"));
  }

  @Test
  public void shouldAllowQuestionIdForAnswers() {
    assertEquals("questionId=12345", wrapper.filterParamString("questionId=12345"));
  }
  
  @Test
  public void shouldAllowMultipleReturnsInInput() {
    assertEquals("asdf\r\n\r\nasdf", wrapper.filterParamString("asdf\r\n\r\nasdf"));
  }

}