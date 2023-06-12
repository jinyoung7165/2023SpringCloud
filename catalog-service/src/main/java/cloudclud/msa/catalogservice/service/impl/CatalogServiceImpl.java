package cloudclud.msa.catalogservice.service.impl;

import cloudclud.msa.catalogservice.jpa.CatalogEntity;
import cloudclud.msa.catalogservice.jpa.CatalogRepository;
import cloudclud.msa.catalogservice.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    private final CatalogRepository catalogRepository;

    @Override
    public Iterable<CatalogEntity> getAllCatalogs() {
        return catalogRepository.findAll();
    }
}
