package com.umasuo.product.application.dto;

import com.umasuo.product.infrastructure.enums.NetType;
import lombok.Data;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * Created by umasuo on 17/6/1.
 */
@Data
public class ProductDraft {

  /**
   * name of the product.
   */
  @NotNull(message = "Name can not be null")
  private String name;

  /**
   * description of the product.
   */
  private String description;

  /**
   * product icon.
   */
  private String icon;

  /**
   * 产品类型的ID
   */
  @NotNull(message = "ProductType can not be null")
  private String productTypeId;

  /**
   * Product net type, identify by how the product connect to the internet.
   */
  @NotNull(message = "NetType can not be null")
  private NetType type;

  /**
   * Open status about this product.
   * True means this product can be find by other developers and false means not.
   */
  private Boolean openable;
}
