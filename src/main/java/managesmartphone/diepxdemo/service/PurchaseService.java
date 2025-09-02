package managesmartphone.diepxdemo.service;

import managesmartphone.diepxdemo.entity.Purchase;
import managesmartphone.diepxdemo.repository.PurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    public Purchase save(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public Optional<Purchase> findById(Long id) {
        return purchaseRepository.findById(id);
    }

    public List<Purchase> findAllOrderByDateDesc() {
        return purchaseRepository.findAllOrderByDateDesc();
    }

    public List<Purchase> findAll() {
        return purchaseRepository.findAll();
    }

    public void deleteById(Long id) {
        purchaseRepository.deleteById(id);
    }

    public List<Purchase> findByDate(LocalDateTime date) {
        return purchaseRepository.findByDate(date);
    }

    public List<Purchase> findByMonth(int month, int year) {
        return purchaseRepository.findByMonth(month, year);
    }

    public List<Purchase> findByYear(int year) {
        return purchaseRepository.findByYear(year);
    }

    public BigDecimal getTotalAmountByDate(LocalDateTime date) {
        return purchaseRepository.getTotalAmountByDate(date);
    }

    public BigDecimal getTotalAmountByMonth(int month, int year) {
        return purchaseRepository.getTotalAmountByMonth(month, year);
    }

    public BigDecimal getTotalAmountByYear(int year) {
        return purchaseRepository.getTotalAmountByYear(year);
    }

    public BigDecimal getTotalAmount() {
        return purchaseRepository.getTotalAmount();
    }

    public List<Purchase> findByCustomerId(Long customerId) {
        return purchaseRepository.findByCustomerIdOrderByDateDesc(customerId);
    }
}
