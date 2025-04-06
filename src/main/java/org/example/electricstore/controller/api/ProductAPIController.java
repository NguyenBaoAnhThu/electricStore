package org.example.electricstore.controller.api;

import org.example.electricstore.DTO.order.ProductChosen;
import org.example.electricstore.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductAPIController {

    @Autowired
    private ProductService productService;

//    @GetMapping("/select/{keyword}")
//    public List<ProductDTO> getProductsByKeyword(@PathVariable String keyword) {
//        return this.productService.getProductsDTOByKeyword(keyword);
//    }

    @GetMapping("/productChosen")
    public List<ProductChosen> getProductById(
            @RequestParam(value = "oldData", defaultValue = "") String listId,
            @RequestParam(value = "newData", defaultValue = "0") Integer id) {

        List<ProductChosen> products = new ArrayList<>();

        //oldData
        if(!listId.equals("empty")){
            List<Integer> productIds = new ArrayList<>();
            List<Integer> quantityList = new ArrayList<>();
            String[] listIdArr = listId.split("-");
            for (String idOld : listIdArr) {
                String[] idArr = idOld.split("\\.");
                Integer productId = Integer.parseInt(idArr[0]);
                Integer quantity = Integer.parseInt(idArr[1]);
                productIds.add(productId);
                quantityList.add(quantity);
            }

            for (int i = 0; i < productIds.size(); i++) {
                ProductChosen product = productService.getProductByIdUseInOrder(productIds.get(i));
                product.setQuantity(quantityList.get(i)>0?quantityList.get(i):1);
                products.add(product);
            }
            Boolean isExist = false;
            for(int j = 0; j< productIds.size(); j++){
                if(productIds.get(j) == id){
                    isExist = true;
                    break;
                }
            }
            if(!isExist&&id!=0){
                ProductChosen product = productService.getProductByIdUseInOrder(id);
                product.setQuantity(1);
                products.add(product);
            }
        }else {
            ProductChosen product = productService.getProductByIdUseInOrder(id);
            product.setQuantity(1);
            products.add(product);
        }

        return products;

    }

}

