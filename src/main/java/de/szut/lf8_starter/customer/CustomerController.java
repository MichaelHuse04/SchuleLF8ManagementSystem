package de.szut.lf8_starter.customer;


import de.szut.lf8_starter.exceptionHandling.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/customer")
@Tag(name = "Customer Management", description = "Endpoints for managing customers")
public class CustomerController {
    private final CustomerService service;
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public List<CustomerEntity> findAllCustomers() {
        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public CustomerEntity findCustomerById(@PathVariable Long id) {
        CustomerEntity customer = this.service.findById(id);
        if (customer == null) {
            throw new ResourceNotFoundException("Customer with id " + id + " not found");
        }
        return customer;
    }

    @PostMapping
    public CustomerEntity createCustomer(@RequestBody CustomerEntity customer) {
        return this.service.save(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        if (this.service.findById(id) == null) {
            throw new ResourceNotFoundException("Customer with id " + id + " not found");
        }

        this.service.deleteById(id);
    }

    @PutMapping("/{id}")
    public CustomerEntity updateCustomer(@PathVariable Long id, @RequestBody CustomerEntity customer) {
        CustomerEntity newCustomer = this.service.update(id, customer);
        if (newCustomer == null) {
            throw new ResourceNotFoundException("Customer with id " + id + " not found");
        }
        return newCustomer;
    }
}
