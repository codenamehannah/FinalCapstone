package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.data.ProductDao;
import org.yearup.models.Category;
import org.yearup.models.Product;

import java.util.List;

// add the annotations to make this a REST controller
// add the annotation to make this controller the endpoint for the following url
    // http://localhost:8080/categories
// add annotation to allow cross site origin requests
// The @CrossOrigin annotation enables Cross-Origin Resource Sharing (CORS)
// Allowing this controller to respond to requests from different origins or domains

@RestController
@CrossOrigin
public class CategoriesController {
    private final CategoryDao categoryDao;
    private ProductDao productDao;

    @Autowired //automatically injects instances of CategoryDao and ProductDao
    public CategoriesController(CategoryDao categoryDao, ProductDao productDao) {
        this.categoryDao = categoryDao;
        this.productDao = productDao;
    }

    @GetMapping(path = "/categories", method = RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public List<Category> getAll() {
        try {
            return categoryDao.getAllCategories();
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The code is taking a coffee break. Please caffeinate and try again! ")
        }
    }

    // add the appropriate annotation for a get action
@GetMapping(path = "/categories/{id}", method = RequestMethod.GET)
@PreAuthorize("permitAll()")
    public Category getById(@PathVariable int id) {
        // get the category by id
    try {
        var category = categoryDao.getById(id);
        if (category == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        return category;
    } catch (Exception ex) {

        throw ex;

        }
    }

    // the url to return all products in category 1 would look like this
    // https://localhost:8080/categories/1/products
    @GetMapping(path = "/categories{id}/products", method = RequestMethod.GET)
    @PreAuthorize("permitAll()")
    public List<Product> getProductsById(@PathVariable int categoryId) {
        try {
            return productDao.listByCategoryId(categoryId);

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The code is taking a coffee break. Please caffeinate and try again!")
        }

        // add annotation to call this method for a POST action
        // add annotation to ensure that only an ADMIN can call this function
        @GetMapping(path = "/categories", method = RequestMethod.POST)
        @ResponseStatus(value = HttpStatus.CREATED)
        @PreAuthorize("hasRole('ROLE_ADMIN')") // Annotation to ensure only an ADMIN can call this function
        //users with admin access are allowed to execute this.

        public Category addCategory (@RequestBody Category category){
            try {
                return categoryDao.create(category);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The code is taking a coffee break. Please caffeinate and try again! ");
            }
        }

        // add annotation to call this method for a PUT (update) action - the url path must include the categoryId
        // add annotation to ensure that only an ADMIN can call this function
        @GetMapping(path = "/categories/{id}", method = RequestMethod.PUT)
        @PreAuthorize("hasRole('ROLE_ADMIN')") //reserved for admins to call
        public void updateCategory ( @PathVariable int id, @RequestBody Category category){
            try {
                categoryDao.update(id, category);
            } catch (Exception ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The code is taking a coffee break. Please caffeinate and try again!");
            }

        }


        // add annotation to call this method for a DELETE action - the url path must include the categoryId
        // add annotation to ensure that only an ADMIN can call this function
        @GetMapping(path = "/categories/{id}", method = RequestMethod.DELETE)
        @ResponseStatus(value = HttpStatus.NO_CONTENT)
        @PreAuthorize("hasRole('ROLE_ADMIN')") //reserved for admins to call
        public void deleteCategory ( @PathVariable int id){
            try {
                Category category = categoryDao.getById(id);
                if (category == null)
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                categoryDao.delete(id);

            } catch (Exception ex) {


                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "The code is taking a coffee break. Please caffeinate and try again! ");
            }
        }

    }