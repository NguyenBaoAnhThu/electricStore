package org.example.electricstore.controller.admin;


import org.example.electricstore.DTO.product.ProductChoiceRequestDTO;
import org.example.electricstore.DTO.transaction.TransactionsDetailDTO;
import org.example.electricstore.model.InventoryTransaction;
//import org.example.electricstore.service.impl.InventoryTransactionService;
import org.example.electricstore.service.impl.InventoryTransactionService;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Admin/transactions")
public class InventoryTransactionController {

   private final InventoryTransactionService inventoryTransactionService;
   public InventoryTransactionController(InventoryTransactionService inventoryTransactionService) {
       this.inventoryTransactionService = inventoryTransactionService;
   }

//    @PostMapping("/submit-import")
//    public ModelAndView submitImport(@ModelAttribute ProductChoiceRequestDTO productChoiceRequestDTO,
//                                     @RequestParam Map<String, String> params,
//                                     RedirectAttributes redirectAttributes) {
//
//        System.out.println("productChoiceRequestDTO : " +  productChoiceRequestDTO);
//        System.out.println("params : " + params);
//        this.inventoryTransactionService.importToWarehouse(productChoiceRequestDTO);
//        redirectAttributes.addFlashAttribute("successfulNotification" , "Nhập kho thành công !") ;
//        return new ModelAndView("redirect:/Admin/ware-houses");
//    }
//    @PostMapping("/submit-export")
//    public ModelAndView submitExport(@ModelAttribute ProductChoiceRequestDTO productChoiceRequestDTO ,
//                                     RedirectAttributes redirectAttributes) {
//        this.inventoryTransactionService.exportFromWarehouse(productChoiceRequestDTO);
//        redirectAttributes.addFlashAttribute("successfulNotification" , "Xuất kho thành công !") ;
//        return new ModelAndView("redirect:/Admin/ware-houses");
//    }
//
//    @GetMapping("/history")
//    public ModelAndView showTransactions (@RequestParam(name = "searchField", required = false, defaultValue = "productName") String field,
//                                          @RequestParam(name = "searchInput", required = false, defaultValue = "") String keyword,
//                                          @RequestParam(name = "transactionType", required = false) String transactionType,
//                                          @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate fromDate,
//                                          @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate toDate,
//                                          @RequestParam(defaultValue = "1") int page,
//                                          @RequestParam(defaultValue = "10") int size) {
//
//        ModelAndView modelAndView = new ModelAndView("admin/transactions/transactions-table");
//        String filterKeyWord = keyword.trim();
//        Page<InventoryTransaction> transactions = this.inventoryTransactionService.searchTransactions(field, filterKeyWord, page, size, fromDate,
//                toDate, transactionType);
//        modelAndView.addObject("transactions", transactions);
//        modelAndView.addObject("page", page);
//        modelAndView.addObject("size", size);
//        modelAndView.addObject("transactionType", transactionType);
//        modelAndView.addObject("fromDate", fromDate);
//        modelAndView.addObject("toDate", toDate);
//        modelAndView.addObject("field", field);
//        modelAndView.addObject("filterKeyWord", filterKeyWord);
//
//        return modelAndView;
//    }
//    @GetMapping("/details/{id}")
//    @ResponseBody
//    public List<TransactionsDetailDTO> showTransactionDetail(@PathVariable("id") Integer id) {
//        return this.inventoryTransactionService.getTransactionsDetail(id) ;
//    }

}
