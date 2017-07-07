package com.umasuo.device.definition.application.service.update;

import com.umasuo.device.definition.application.dto.action.SetStatus;
import com.umasuo.device.definition.domain.model.Device;
import com.umasuo.device.definition.infrastructure.update.UpdateAction;
import com.umasuo.device.definition.infrastructure.update.UpdateActionUtils;
import com.umasuo.model.Updater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Davis on 17/7/4.
 */
@Service(UpdateActionUtils.SET_STATUS)
public class SetStatusService implements Updater<Device, UpdateAction> {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(SetStatusService.class);

  @Override
  public void handle(Device device, UpdateAction updateAction) {
    LOG.debug("Enter. device: {}, updateAction: {}.", device, updateAction);

    SetStatus action = (SetStatus) updateAction;
    device.setStatus(action.getStatus());

    LOG.debug("Exit.");
  }
}