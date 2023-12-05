package com.hladchenko.bring;

import com.hladchenko.bring.context.BringContext;
import com.hladchenko.bring.demo.TestController;

public class Main {

  public static void main(String[] args) {
    BringContext bringContext = BringContext.scan(Main.class);
    TestController testController = bringContext.getBean(TestController.class);
    testController.sayFromService();
  }
}