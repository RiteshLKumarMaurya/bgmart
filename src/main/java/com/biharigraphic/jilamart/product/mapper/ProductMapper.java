package com.biharigraphic.jilamart.product.mapper;

import com.biharigraphic.jilamart.product.entity.Product;
import com.biharigraphic.jilamart.product.model.req.CreateProductRequest;
import com.biharigraphic.jilamart.product.model.reqs.ProductResponse;
import com.biharigraphic.jilamart.productcategory.entity.ProductCategory;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(CreateProductRequest req, ProductCategory category) {

        return Product.builder()
                .name(req.getName())
                .shortInfo(req.getShortInfo())
                .longInfo(req.getLongInfo())
                .stock(req.getStock())
                .weight(req.getWeight())
                .price(req.getPrice())
                .discountPrice(req.getDiscountPrice())
                .sku(req.getSku())
                .brand(req.getBrand())
                .fragile(req.getFragile())
                .productCategory(category)
                .thumbnail(req.getThumbnail())
                .imgUrls(req.getImgUrls())
                .build();
    }

    public ProductResponse toResponse(Product product) {

        ProductResponse res = new ProductResponse();

        res.setId(product.getId());
        res.setName(product.getName());
        res.setShortInfo(product.getShortInfo());
        res.setPrice(product.getPrice());
        res.setDiscountPrice(product.getDiscountPrice());
        res.setBrand(product.getBrand());
        res.setRating(product.getRating());
        res.setThumbnail(product.getThumbnail());
        res.setImages(product.getImgUrls());
        res.setFeatured(product.isFeatured());

        return res;
    }
}