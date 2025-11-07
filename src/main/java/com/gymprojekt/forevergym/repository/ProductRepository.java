package com.gymprojekt.forevergym.repository;

import com.gymprojekt.forevergym.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    List<Product> findByPrice(Integer price);

    List<Product> findAllByOrderByPriceDesc();

    List<Product> findAllByOrderByPriceAsc();

    List<Product> findByDescriptionContaining(String description);
}
