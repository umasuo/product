package com.umasuo.product.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.umasuo.product.infrastructure.enums.Category;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.Transient;

/**
 * Created by Davis on 17/7/12.
 */
@Data
public class ProductDataView implements Serializable{

  private static final long serialVersionUID = 1291157843289547677L;

  /**
   * auto generated uuid.
   */
  private String id;

  /**
   * version used for update date check.
   */
  private Integer version;

  /**
   * data id defined by the developer.
   */
  private String dataId;

  /**
   * the data structure.
   */
  @JsonIgnore
  private String schema;

  @Transient
  private JsonNode dataSchema;

  /**
   * name of this definition.
   */
  private String name;

  /**
   * describe the usage of this definition.
   */
  private String description;

  /**
   * The Openable.
   * True means other developers can find this data, false means not.
   */
  private Boolean openable;

  private Category category;

  public static List<ProductDataView> build(List<ProductDataView> objects) {
    List<ProductDataView> result = Lists.newArrayList();

    for (Object view : objects) {
      LinkedHashMap map = (LinkedHashMap) view;

      result.add(build(map));
    }

    return result;
  }

  private static ProductDataView build(LinkedHashMap map) {
    ProductDataView result = new ProductDataView();

    result.setDataId(map.get("dataId").toString());
    result.setName(map.get("name").toString());
    result.setId(map.get("id").toString());
    result.setCategory(Category.valueOf(map.get("category").toString()));
    result.setSchema(map.get("schema").toString());
    result.setDataSchema(null);
    result.setDescription(map.get("description").toString());
    result.setVersion(Integer.valueOf(map.get("version").toString()));

    return result;
  }
}