package org.example.electricstore.controller.admin;

import org.example.electricstore.DTO.product.ProductChoiceDTO;
import org.example.electricstore.DTO.supplier.SupplierDTO;
import org.example.electricstore.mapper.product.ProductMapper;
import org.example.electricstore.model.*;
import org.example.electricstore.repository.*;
import org.example.electricstore.service.impl.BrandService;
import org.example.electricstore.service.impl.SupplierService;
import org.example.electricstore.service.impl.WareHouseService;
import org.example.electricstore.service.interfaces.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/Admin/ware-houses")
public class WareHouseController {

    private final WareHouseService wareHouseService;
    private final IProductService productService;
    private final ProductMapper productMapper;
    private final BrandService brandService;
    private ISupplierRepository supplierRepository;
    private IProductRepository productRepository;
    private InvoiceItemRepository invoiceItemRepository;
    private InvoiceRepository invoiceRepository;
    private IWareHouseRepository wareHouseRepository;

    public WareHouseController(WareHouseService wareHouseService,
                               IProductService productService,
                               ProductMapper productMapper,
                               BrandService brandService,
                               ISupplierRepository supplierRepository,
                               IProductRepository productRepository,
                               InvoiceItemRepository invoiceItemRepository,
                               InvoiceRepository invoiceRepository,
                               IWareHouseRepository wareHouseRepository) {
        this.wareHouseService = wareHouseService;
        this.productService = productService;
        this.productMapper = productMapper;
        this.brandService = brandService;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.invoiceRepository = invoiceRepository;
        this.wareHouseRepository = wareHouseRepository;
    }





   @ModelAttribute("brands")
   public List<Brand> brands() {
        return this.brandService.getAllBrands();
   }

    @GetMapping
    public ModelAndView showWareHouse(
            @RequestParam(name = "importDate", required = false ) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate importDate,
            @RequestParam(name = "brand", required = false, defaultValue = "") String brand,
            @RequestParam(name = "statusStock", required = false, defaultValue = "0") Integer statusStock,
            @RequestParam(name = "productCode", required = false) String productCode,
            @RequestParam(name = "productName", required = false , defaultValue = "") String productName,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {
        String nameFilter = productName.trim();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<WareHouse> wareHousePage = wareHouseService.searchWareHouses(
                importDate, brand, statusStock, productCode, nameFilter, pageable
        );
        List<Supplier> suppliers = supplierRepository.findAll();

        ModelAndView modelAndView = new ModelAndView("admin/warehouse/warehouse-table");
        modelAndView.addObject("wareHousePage", wareHousePage);
        modelAndView.addObject("wareHouses", wareHousePage.getContent());
        modelAndView.addObject("importDate", importDate);
        modelAndView.addObject("brand", brand);
        modelAndView.addObject("statusStock", statusStock);
        modelAndView.addObject("productCode", productCode);
        modelAndView.addObject("productName", nameFilter);
        modelAndView.addObject("page", page);
        modelAndView.addObject("size", size);
        modelAndView.addObject("totalPages", wareHousePage.getTotalPages());
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("suppliers", suppliers);
        return modelAndView;
    }

    @GetMapping("/api/products-choice")
    @ResponseBody
    public List<ProductChoiceDTO> getProductChoices() {
        List<Product> products = this.productService.getAllProducts();
        return products.stream().map(this.productMapper::convertToProductChoiceDTO).toList();
    }

    @GetMapping("/api/products-choiceFromWareHouse")
    @ResponseBody
    public List<ProductChoiceDTO> getProductChoiceFromWareHouse() {
        List<WareHouse> wareHouseList = this.wareHouseService.getWareHouses();
        return wareHouseList.stream().map(this.productMapper::convertToProductChoiceDTOByWareHouse).toList();
    }

    @GetMapping("/import")
    public ModelAndView showImport(@RequestParam(value = "supplierId", required = false) Integer supplierId) {
        List<Supplier> suppliers = supplierRepository.findAll();

        List<Product> products;
        if (supplierId != null) {
            products = productRepository.getProductsBySupplierId(supplierId);
        } else {
            products = productRepository.findAll();
        }

        // Gán giá từ WareHouse (mới nhất hoặc logic phù hợp)
        for (Product product : products) {
            // Tìm danh sách kho theo productId
            List<WareHouse> relatedWarehouses = wareHouseRepository.findByProductIdOrderByImportDateDesc(product.getProductID());

            // Gán giá nếu có warehouse
            if (!relatedWarehouses.isEmpty()) {
                WareHouse latest = relatedWarehouses.get(0); // Lấy phiếu nhập gần nhất
                product.setPrice(latest.getPrice());          // Gán vào Product để hiển thị ở view
            }
        }
        ModelAndView modelAndView = new ModelAndView("admin/warehouse/import");
        modelAndView.addObject("suppliers", suppliers);
        modelAndView.addObject("products", products);
        modelAndView.addObject("selectedSupplier", supplierId);
        return modelAndView;
    }

    @GetMapping("/history_warehouse")
    public ModelAndView showHistoryWarehouse(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<InvoiceItem> invoicePage = invoiceItemRepository.findAllWithInvoice(pageable);

        ModelAndView modelAndView = new ModelAndView("admin/warehouse/history_warehouse");
        modelAndView.addObject("invoiceItems", invoicePage.getContent());
        modelAndView.addObject("currentPage", page); // truyền xuống view
        modelAndView.addObject("pageSize", size);    // truyền xuống v
        modelAndView.addObject("totalPages", invoicePage.getTotalPages());
        return modelAndView;
    }






}