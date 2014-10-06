package com.slocumbrau.filter;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class EventHandlerInjectionTest {
  private InjectionAttackWrapper wrapper;
  private HttpServletRequest request;
  Map<String, String[]> innocuousMap = new HashMap<String, String[]>();
  Map<String, String[]> injectionMap = new HashMap<String, String[]>();

  @Before
  public void setUp() throws Exception {
    request = Mockito.mock(HttpServletRequest.class);
    wrapper = new InjectionAttackWrapper(request);
    innocuousMap.put("param1", new String[] { "%20Christina Zhong" });
    innocuousMap.put("param2", new String[] { "%20ona sunny day" });
    innocuousMap.put("param3", new String[] { "%20on a rainy day" });
    innocuousMap.put("param4", new String[] { "Then ona winter day" });

    String injectedCodeWithHexspace = "%20onMouseOver=alert('today');";
    String injectedCodeWithHexquote = "%22%20string";
    String injectedCodeWithSpace = " onMouseOver=alert('today');";
    String injectedCodeWithQuote = "' string";
    String injectedCodeKeyboardWithSpace = " onkeydown=alert('today');";
    String injectedCodeWithHexspaceAndSpace = "%22+onmouseover=alert(document.cookie)+%22";
    injectionMap.put("param1", new String[] { injectedCodeWithHexspace });
    injectionMap.put("param2", new String[] { injectedCodeWithHexquote });
    injectionMap.put("param3", new String[] { injectedCodeWithSpace });
    injectionMap.put("param4", new String[] { injectedCodeWithQuote });
    injectionMap.put("param5", new String[] { injectedCodeKeyboardWithSpace });
    injectionMap.put("param6", new String[] { injectedCodeWithHexspaceAndSpace });
}

  @Test
  public void shouldNotFilterMessageWithAnOnInAName() {
    Mockito.when(request.getParameterMap()).thenReturn(innocuousMap);

    wrapper = new InjectionAttackWrapper(request);
    Map<String,String[]> output = wrapper.getParameterMap();
    assertEquals("%20Christina Zhong", output.get("param1")[0]);

  }

  @Test
  public void shouldNotFilterMessageWithStartingWithOn() {
    Mockito.when(request.getParameterMap()).thenReturn(innocuousMap);

    wrapper = new InjectionAttackWrapper(request);
    Map<String,String[]> output = wrapper.getParameterMap();
    assertEquals("%20ona sunny day", output.get("param2")[0]);

  }

  @Test
  public void shouldNotFilterMessageWithAnOnInItLaterInTheString() {
    Mockito.when(request.getParameterMap()).thenReturn(innocuousMap);

    wrapper = new InjectionAttackWrapper(request);
    Map<String,String[]> output = wrapper.getParameterMap();
    assertEquals("Then ona winter day", output.get("param4")[0]);

  }

  @Test
  public void shouldNotFilterMessageStartingWithOn() {
    Mockito.when(request.getParameterMap()).thenReturn(innocuousMap);

    wrapper = new InjectionAttackWrapper(request);
    Map<String,String[]> output = wrapper.getParameterMap();
    assertEquals("%20on a rainy day", output.get("param3")[0]);

  }

  @Test
  public void shouldFilterInjectedCodeWithHexspace() {
    Mockito.when(request.getParameterMap()).thenReturn(injectionMap);
    wrapper = new InjectionAttackWrapper(request);
    Map<String,String[]> output = wrapper.getParameterMap();

    assertEquals("", ((String[]) output.get("param1"))[0]);
  }

  @Test
  public void shouldFilterInjectedCodeWithHexquote() {
    Mockito.when(request.getParameterMap()).thenReturn(injectionMap);
    wrapper = new InjectionAttackWrapper(request);
    Map<String,String[]> output = wrapper.getParameterMap();

    assertEquals("", ((String[]) output.get("param2"))[0]);
  }

  @Test
  public void shouldFilterInjectedCodeWithSpace() {
    Mockito.when(request.getParameterMap()).thenReturn(injectionMap);
    wrapper = new InjectionAttackWrapper(request);
    Map<String,String[]> output = wrapper.getParameterMap();

    assertEquals("", ((String[]) output.get("param3"))[0]);
  }

  @Test
  public void shouldNotFilterQuoteSpace() {
    Mockito.when(request.getParameterMap()).thenReturn(injectionMap);
    wrapper = new InjectionAttackWrapper(request);
    Map<String,String[]> output = wrapper.getParameterMap();

    assertEquals("' string", ((String[]) output.get("param4"))[0]);
  }

  @Test
  public void shouldNotFilterInjectedCodeKeyboardWithSpace() {
    Mockito.when(request.getParameterMap()).thenReturn(injectionMap);
    wrapper = new InjectionAttackWrapper(request);
    Map<String,String[]> output = wrapper.getParameterMap();

    assertEquals("", ((String[]) output.get("param5"))[0]);
  }

  @Test
  public void shouldFilterInjectedCodeHexQuoteWithNormalSpace() {
    Mockito.when(request.getParameterMap()).thenReturn(injectionMap);
    wrapper = new InjectionAttackWrapper(request);
    Map<String,String[]> output = wrapper.getParameterMap();

    assertEquals("", ((String[]) output.get("param6"))[0]);
  }

}