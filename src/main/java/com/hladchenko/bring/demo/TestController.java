package com.hladchenko.bring.demo;

@BringComponent
public class TestController {

  TestService testService;

  public TestController(TestService testService) {
    this.testService = testService;
  }

  public void sayFromService() {
    System.out.println(testService.getValue());
  }
}
