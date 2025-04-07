package com.sahajanand.java_21_gradle_k_sb_jar_mysql.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class SystemOutSystemErrDisableAll {

  private final Utils utils;

  public SystemOutSystemErrDisableAll(Utils utils) {
    this.utils = utils;
  }

  @PostConstruct
  public void init() {
    if (Utils.isDev()) {
      Utils.disableAllSystemOut(false);
    }
    else if (Utils.isProd()) {
      Utils.disableAllSystemOut(true);
    }
  }
}
