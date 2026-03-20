package com.ecommerce.demo.service;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.demo.model.Brand;
import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.dto.ProductDTO;
import com.ecommerce.demo.model.dto.ProductFilterDTO;
import com.ecommerce.demo.repository.ProductRepository;
import com.ecommerce.demo.utils.specifications.ProductSpecification;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;

    public List<Product> listarProductos() throws Exception {
        return productRepository.findAll();
    }

    public Product obtenerProductoId(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new Exception("Producto no encontrado"));
    }

    public Page<Product> filtrarProductos(ProductFilterDTO filter, Integer page, Integer size) throws Exception {
        
        Pageable pageable = PageRequest.of(page, size);
        
        Specification<Product> spec = Stream.of(
                ProductSpecification.byName(filter.name()),
                ProductSpecification.bySku(filter.sku()),
                ProductSpecification.byCategoryName(filter.categoryName()),
                ProductSpecification.bybrandName(filter.brandName())
        ).filter(Objects::nonNull)
        .reduce(Specification::and)
        .orElse(null);

        return productRepository.findAll(spec, pageable);
    }
    
    public Map<String, Object> alertarStockBajo() throws Exception {
        Map<String, Object> resp = new HashMap<>();
        try {

            List<Product> products = productRepository.productosStockBajo();
            resp.put("severity", "warn");
            resp.put("msg", "Estos productos estan con stock bajo");
            resp.put("productos", products);

        } catch (Exception e) {
            resp.put("severity", "error");
            resp.put("msg", e.getMessage());
        }
        return resp;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> crearProducto(ProductDTO body) throws Exception {
        Map<String, Object> resp = new HashMap<>();
        try {

            Product product = new Product();
            product.setName(body.name());
            product.setSku(body.sku());
            product.setStock(0L);
            product.setMinStock(body.minStock());
            product.setCostPrice(new BigDecimal(body.costPrice()));
            product.setSalePrice(new BigDecimal(body.salePrice()));
            product.setDescription(body.description());

            Category category = categoryService.obtenerCategoria(body.categoria());
            product.setCategoryId(category);

            Brand brand = brandService.obtenerMarcaNombre(body.brand());
            product.setBrandId(brand);

            productRepository.save(product);

            resp.put("severity", "success");
            resp.put("msg", "Producto creado con exito");

        } catch (Exception e) {
            resp.put("severity", "error");
            resp.put("msg", e.getMessage());
        }
        return resp;
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> editarProducto(Long id, ProductDTO body) throws Exception {

        Map<String, Object> resp = new HashMap<>();
        try {

            Product productoExist = obtenerProductoId(id);
            productoExist.setName(body.name());
            productoExist.setSku(body.sku());
            productoExist.setStock(0L);
            productoExist.setMinStock(body.minStock());
            productoExist.setCostPrice(new BigDecimal(body.costPrice()));
            productoExist.setSalePrice(new BigDecimal(body.salePrice()));
            productoExist.setDescription(body.description());

            Category category = categoryService.obtenerCategoria(body.categoria());
            productoExist.setCategoryId(category);

            Brand brand = brandService.obtenerMarcaNombre(body.brand());
            productoExist.setBrandId(brand);

            productRepository.save(productoExist);

            resp.put("severity", "success");
            resp.put("msg", "Producto editado con exito");

        } catch (Exception e) {
            resp.put("severity", "error");
            resp.put("msg", e.getMessage());
        }
        return resp;
    }

    public Map<String, Object> eliminarProducto(Long id) throws Exception {

        Map<String, Object> resp = new HashMap<>();
        try {

            productRepository.delete(obtenerProductoId(id));

            resp.put("severity", "success");
            resp.put("msg", "Producto eliminado con exito");
        } catch (Exception e) {
            resp.put("severity", "error");
            resp.put("msg", e.getMessage());
        }
        return resp;
    }

}
