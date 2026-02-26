package com.ecommerce.demo.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.demo.model.Category;
import com.ecommerce.demo.model.Product;
import com.ecommerce.demo.model.dto.ProductDTO;
import com.ecommerce.demo.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;

    public List<Product> listarProductos() throws Exception {
        return productRepository.findAll();
    }

    public Product obtenerProductoId(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new Exception("Producto no encontrado"));
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
            product.setStock(body.stock());
            product.setMinStock(5L);
            product.setCostPrice(new BigDecimal(body.costPrice()));
            product.setSalePrice(new BigDecimal(body.salePrice()));
            product.setDescription(body.description());

            Category category = categoryService.obtenerCategoria(body.categoria());
            product.setCategoryId(category);

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
            productoExist.setStock(body.stock());
            productoExist.setMinStock(5L);
            productoExist.setCostPrice(new BigDecimal(body.costPrice()));
            productoExist.setSalePrice(new BigDecimal(body.salePrice()));
            productoExist.setDescription(body.description());

            Category category = categoryService.obtenerCategoria(body.categoria());
            productoExist.setCategoryId(category);

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
