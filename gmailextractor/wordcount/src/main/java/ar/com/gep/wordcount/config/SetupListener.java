package ar.com.gep.wordcount.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class SetupListener extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
      return Guice.createInjector(new MainModule(), new TaskModule());
    }
    
}