package managesmartphone.diepxdemo.service;

import managesmartphone.diepxdemo.entity.Customer;
import managesmartphone.diepxdemo.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        return customerRepository.save(customer);
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
