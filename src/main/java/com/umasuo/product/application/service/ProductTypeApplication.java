package com.umasuo.product.application.service;

import com.umasuo.exception.NotExistException;
import com.umasuo.product.application.dto.CommonDataView;
import com.umasuo.product.application.dto.ProductTypeDraft;
import com.umasuo.product.application.dto.ProductTypeView;
import com.umasuo.product.application.dto.mapper.ProductTypeMapper;
import com.umasuo.product.domain.model.ProductType;
import com.umasuo.product.domain.service.ProductTypeService;
import com.umasuo.product.infrastructure.update.UpdateAction;
import com.umasuo.product.infrastructure.update.UpdaterService;
import com.umasuo.product.infrastructure.validator.VersionValidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ProductType application.
 */
@Service
public class ProductTypeApplication {

  /**
   * Logger.
   */
  private static final Logger LOG = LoggerFactory.getLogger(ProductTypeApplication.class);

  /**
   * ProductType service.
   */
  @Autowired
  private transient ProductTypeService productTypeService;

  /**
   * RestClient.
   * 用于访问data-definition，获取产品类别对应的数据定义。
   */
  @Autowired
  private transient RestClient restClient;

  /**
   * CacheApplication.
   */
  @Autowired
  private transient CacheApplication cacheApplication;

  /**
   * UpdaterService.
   */
  @Autowired
  private transient UpdaterService updaterService;

  /**
   * Create product type view.
   *
   * @param productTypeDraft the product type draft
   * @return the product type view
   */
  public ProductTypeView create(ProductTypeDraft productTypeDraft) {
    LOG.debug("Enter. productTypeDraft: {}.", productTypeDraft);

    ProductType productType = ProductTypeMapper.toModel(productTypeDraft);

    ProductType newProductType = productTypeService.save(productType);

    ProductTypeView result = ProductTypeMapper.toView(newProductType);

    cacheApplication.deleteProductTypes();

    LOG.debug("Exit. new productType id: {}.", result.getId());
    return result;
  }

  /**
   * Delete ProductType by it's id.
   *
   * @param id the id
   * @param version the version
   */
  public void delete(String id, Integer version) {
    LOG.debug("Enter. product type id: {}, version: {}.", id, version);

    ProductType productType = productTypeService.getById(id);

    VersionValidator.checkVersion(version, productType.getVersion());

    productTypeService.delete(id);

    restClient.deleteProductTypeData(id);

    cacheApplication.deleteProductTypes();

    LOG.debug("Exit.");
  }

  /**
   * Update product type view.
   *
   * @param id the id
   * @param version the version
   * @param actions the actions
   * @return the product type view
   */
  public ProductTypeView update(String id, Integer version, List<UpdateAction> actions) {
    LOG.debug("Enter: id: {}, version: {}, actions: {}.", id, version, actions);

    ProductType valueInDb = productTypeService.getById(id);

    VersionValidator.checkVersion(version, valueInDb.getVersion());

    actions.stream().forEach(action -> updaterService.handle(valueInDb, action));

    ProductType product = productTypeService.save(valueInDb);

    cacheApplication.deleteProductTypes();

    Map<String, List<CommonDataView>> dataDefinitionViews =
        restClient.getProductTypeData();

    ProductTypeView updatedProduct = ProductTypeMapper.toView(product, dataDefinitionViews);

    LOG.trace("updated productType: {}", updatedProduct);
    LOG.debug("Exit.");

    return updatedProduct;
  }

  /**
   * 查询所有的产品类型。
   *
   * @return list build ProductType
   */
  public List<ProductTypeView> getAll() {
    LOG.debug("Enter.");

    // get from redis
    List<ProductTypeView> cacheProductTypes = cacheApplication.getAllProductType();

    if (cacheProductTypes.isEmpty()) {
      LOG.debug("Cache fail. Get from database.");
      List<ProductType> productTypes = productTypeService.getAll();

      // 调用data-definition的api获取对应id的CommonDataView
      Map<String, List<CommonDataView>> dataDefinitionViews =
          restClient.getProductTypeData();

      cacheProductTypes = ProductTypeMapper.toView(productTypes, dataDefinitionViews);

      cacheApplication.cacheProductType(cacheProductTypes);

    }

    LOG.debug("Exit. productType size: {}.", cacheProductTypes.size());
    return cacheProductTypes;
  }

  /**
   * Get ProductType by it's id.
   *
   * @param id the id
   * @return the product type view
   */
  public ProductTypeView get(String id) {
    LOG.debug("Enter. id: {}.", id);
    List<ProductTypeView> productTypeViews = getAll();

    ProductTypeView result =
        productTypeViews.stream().filter(productTypeView -> id.equals(productTypeView.getId()))
            .findAny().orElse(null);

    if (result == null) {
      LOG.debug("Can not find productType: {}.", id);
      throw new NotExistException("ProductType not exist");
    }

    LOG.debug("Exit. productType: {}.", result);
    return result;
  }
}
