package managesmartphone.diepxdemo.service;

import managesmartphone.diepxdemo.entity.Customer;
import managesmartphone.diepxdemo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Optional<Customer> findByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }

    public List<Customer> findByPhoneContaining(String phone) {
        return customerRepository.findByPhoneContaining(phone);
    }

    public Customer save(Customer customer) {
        if(customer.getId() == null){
            Customer newCustomer = Customer.builder()
                    .createdDate(LocalDateTime.now())
                    .name(customer.getName())
                    .phone(customer.getPhone())
                    .dob(customer.getDob())
                    .build();
            return customerRepository.save(newCustomer);
        }
        Customer customer1 = customerRepository.findById(customer.getId()).orElseThrow(()-> new RuntimeException("Customer not found"));
        if(!Objects.equals(customer1.getName(), customer.getName())){
            customer1.setName(customer.getName());
        }
        if (!Objects.equals(customer1.getDob(), customer.getDob())){
            customer1.setDob(customer.getDob());
        }
        customer1.setCreatedDate(LocalDateTime.now());
        if (!Objects.equals(customer1.getPhone(), customer.getPhone())){
            customer1.setPhone(customer.getPhone());
        }

        return customerRepository.save(customer1);

    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public List<Customer> findByCreatedDate(LocalDateTime date) {
        return customerRepository.findByCreatedDate(date);
    }

    public List<Customer> findByCreatedMonth(int month, int year) {
        return customerRepository.findByCreatedMonth(month, year);
    }

    public List<Customer> findByCreatedYear(int year) {
        return customerRepository.findByCreatedYear(year);
    }

    public long countAll() {
        return customerRepository.count();
    }

    public long countByCreatedDate(LocalDateTime date) {
        return customerRepository.countByCreatedDate(date);
    }

    public long countByCreatedMonth(int month, int year) {
        return customerRepository.countByCreatedMonth(month, year);
    }

    public long countByCreatedYear(int year) {
        return customerRepository.countByCreatedYear(year);
    }

    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }
}
