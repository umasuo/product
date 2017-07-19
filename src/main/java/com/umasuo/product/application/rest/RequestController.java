package com.umasuo.product.application.rest;

import com.umasuo.product.application.service.RequestApplication;
import com.umasuo.product.infrastructure.Router;
import com.umasuo.product.infrastructure.enums.RequestStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Davis on 17/7/19.
 */
@RestController
public class RequestController {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(RequestController.class);

  @Autowired
  private transient RequestApplication requestApplication;

  @PutMapping(Router.PRODUCT_REQUEST_WITH_ID)
  public void updateStatus(@PathVariable("id") String id, @RequestBody RequestStatus status) {
    LOG.info("Enter. id: {}, status: {}.", id, status);

    requestApplication.replyRequest(id, status);

    LOG.debug("Exit.");
  }
}
