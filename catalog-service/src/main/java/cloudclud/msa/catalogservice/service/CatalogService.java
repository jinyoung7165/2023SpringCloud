package cloudclud.msa.catalogservice.service;

import cloudclud.msa.catalogservice.jpa.CatalogEntity;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}
