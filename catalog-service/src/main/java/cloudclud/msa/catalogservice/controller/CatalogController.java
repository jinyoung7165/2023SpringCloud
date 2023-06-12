package cloudclud.msa.catalogservice.controller;

import cloudclud.msa.catalogservice.jpa.CatalogEntity;
import cloudclud.msa.catalogservice.service.CatalogService;
import cloudclud.msa.catalogservice.utils.CustomObjectMapper;
import cloudclud.msa.catalogservice.utils.response.ApiResponse;
import cloudclud.msa.catalogservice.vo.ResponseCatalog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog-service")
public class CatalogController {
    private final CatalogService catalogService;

    @GetMapping(value = "/health-check")
    public String status(HttpServletRequest request) {
       return String.format("It's working in User Service, on PORT %s",
                request.getServerPort()); // env-> local.server.port로 가져올 수도 있음
    }


    @GetMapping(value = "/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getUsers() {
        Iterable<CatalogEntity> catalogList = catalogService.getAllCatalogs();
        List<ResponseCatalog> result = new ArrayList<>();
        catalogList.forEach(v -> {
            result.add(CustomObjectMapper.to(v, ResponseCatalog.class));
        });
        return ApiResponse.success(result);
    }
}
