package com.ecommerce.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.demo.model.dto.PurchaseOrderDTO;
import com.ecommerce.demo.model.dto.PurchaseOrderFilterDTO;
import com.ecommerce.demo.service.PurchaseOrderService;

@RestController
@CrossOrigin(origins = { "*" })
@RequestMapping("/api/purchase-orders")
public class PurchaseOrderController {

    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @GetMapping("/listar")
    public ResponseEntity<?> listarOrdenes() throws Exception {
        return ResponseEntity.ok(purchaseOrderService.listarOrdenes());
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<?> obtenerOrdenId(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(purchaseOrderService.obtenerOrdenId(id));
    }

    @GetMapping("/filtrar")
    public ResponseEntity<?> filtrarOrdenes(
            @RequestParam(required = false) String orderNumber,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) throws Exception {
        
        com.ecommerce.demo.model.enums.OrderStatus statusEnum = null;
        if (status != null && !status.isBlank()) {
            statusEnum = com.ecommerce.demo.model.enums.OrderStatus.valueOf(status);
        }
        
        java.time.LocalDate dateFromParsed = dateFrom != null ? java.time.LocalDate.parse(dateFrom) : null;
        java.time.LocalDate dateToParsed = dateTo != null ? java.time.LocalDate.parse(dateTo) : null;
        
        PurchaseOrderFilterDTO filter = new PurchaseOrderFilterDTO(
            orderNumber, supplierId, statusEnum, dateFromParsed, dateToParsed, page, size
        );
        
        return ResponseEntity.ok(purchaseOrderService.filtrarOrdenes(filter, page, size));
    }

    @PostMapping("/guardar")
    public ResponseEntity<?> guardarOrden(@RequestBody PurchaseOrderDTO dto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseOrderService.crearOrden(dto));
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarOrden(@PathVariable Long id, @RequestBody PurchaseOrderDTO dto) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseOrderService.editarOrden(id, dto));
    }

    @PostMapping("/recibir/{id}")
    public ResponseEntity<?> recibirOrden(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(purchaseOrderService.recibirOrden(id));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> eliminarOrden(@PathVariable Long id) throws Exception {
        return ResponseEntity.ok(purchaseOrderService.eliminarOrden(id));
    }
}
