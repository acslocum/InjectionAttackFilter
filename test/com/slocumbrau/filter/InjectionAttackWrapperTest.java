package com.slocumbrau.filter;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

public class InjectionAttackWrapperTest {
  private final String DATE_INPUT = "August 30, 2005";
  private final String OTHER_CHARS_INPUT = ".' ,-";
  private final String STRING_WITH_RESTRICTED_CHARS = "<script>foo</script>";
  private final String RESTRICTED_2 = "%3Cscript%3Efoo%3C/script%3E";
  private final String RESTRICTED_LIST = "=%<>|$;%\"\n\r\\hi";
  private final String FILTERED_STRING = "scriptfoo/script";
  private InjectionAttackWrapper wrapper;
  private HttpServletRequest request;

  @Before
  public void setUp() throws Exception {
    request = Mockito.mock(HttpServletRequest.class);
    wrapper = new InjectionAttackWrapper(request);
  }

  @Test
  public void shouldStopScriptlets() {
    assertEquals(FILTERED_STRING, wrapper.filterParamString((STRING_WITH_RESTRICTED_CHARS)));
  }

  @Test
  public void shouldStopEscapedScriptlets() {
    assertEquals(FILTERED_STRING, wrapper.filterParamString((RESTRICTED_2)));
  }

  @Test
  public void shouldFilterLinkInjection() {
    assertEquals("%22%27A%20HREF%22%2FWF_XSRF.html%22Injected%20Link%2FA",
        wrapper.filterParamString("%22%27%3E%3CA%20HREF%3D%22%2FWF_XSRF.html%22%3EInjected%20Link%3C%2FA%3E"));
  }

  @Test
  public void shouldFilterEscapedEquals() {
    assertEquals("hi", wrapper.filterParamString("%3Dhi"));
  }

  @Test
  public void shouldFilterCSSInjection() {
    assertEquals("something", wrapper.filterParamString("something%22%20style%3D%22background:url(javascript:alert(234))%22%20OA%3D%22"));
  }

  @Test
  @Ignore
  public void shouldRemoveLotsOfBadCharacters() {
    assertEquals("hi", wrapper.filterParamString(RESTRICTED_LIST));
  }

  @Test
  public void shouldFiltersRestrictedCharsAndSkipsRegularCharsInArray() {
    String[] array = new String[] { STRING_WITH_RESTRICTED_CHARS, DATE_INPUT, OTHER_CHARS_INPUT };

    assertEquals(FILTERED_STRING, wrapper.filterStringArray(array)[0]);
    assertEquals(DATE_INPUT, wrapper.filterStringArray(array)[1]);
    assertEquals(OTHER_CHARS_INPUT, wrapper.filterStringArray(array)[2]);
  }

  @Test
  public void shouldFilterParameterMap() {
    Map<String, String[]> map = new HashMap<String, String[]>();
    final String param1 = "param1";
    map.put(param1, new String[] { STRING_WITH_RESTRICTED_CHARS });
    final String param2 = "param2";
    map.put(param2, new String[] { DATE_INPUT });

    Mockito.when(request.getParameterMap()).thenReturn(map);

    wrapper = new InjectionAttackWrapper(request);
    Map output = wrapper.getParameterMap();

    assertEquals(FILTERED_STRING, ((String[]) output.get(param1))[0]);
    assertEquals(DATE_INPUT, ((String[]) output.get(param2))[0]);
  }

  @Test
  public void shouldFilterEntireQueryString() {
    String xssString = "orderNumber=&phoneNumber=>'><script>alert('boooo')</script>";
    Mockito.when(request.getQueryString()).thenReturn(xssString);
    wrapper = new InjectionAttackWrapper(request);
    assertEquals("orderNumber=&phoneNumber='scriptalert('boooo')/script", wrapper.getQueryString());
  }

}
