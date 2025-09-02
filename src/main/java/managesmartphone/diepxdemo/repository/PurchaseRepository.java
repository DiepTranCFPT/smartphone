package managesmartphone.diepxdemo.repository;

import managesmartphone.diepxdemo.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByCustomerIdOrderByDateDesc(Long customerId);

    @Query("SELECT p FROM Purchase p WHERE DATE(p.date) = DATE(:date) ORDER BY p.date DESC")
    List<Purchase> findByDate(@Param("date") LocalDateTime date);

    @Query("SELECT p FROM Purchase p WHERE MONTH(p.date) = :month AND YEAR(p.date) = :year ORDER BY p.date DESC")
    List<Purchase> findByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT p FROM Purchase p WHERE YEAR(p.date) = :year ORDER BY p.date DESC")
    List<Purchase> findByYear(@Param("year") int year);

    @Query("SELECT SUM(pi.unitPrice * pi.qty) FROM Purchase p JOIN p.items pi WHERE DATE(p.date) = DATE(:date)")
    BigDecimal getTotalAmountByDate(@Param("date") LocalDateTime date);

    @Query("SELECT SUM(pi.unitPrice * pi.qty) FROM Purchase p JOIN p.items pi WHERE MONTH(p.date) = :month AND YEAR(p.date) = :year")
    BigDecimal getTotalAmountByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(pi.unitPrice * pi.qty) FROM Purchase p JOIN p.items pi WHERE YEAR(p.date) = :year")
    BigDecimal getTotalAmountByYear(@Param("year") int year);

    @Query("SELECT SUM(pi.unitPrice * pi.qty) FROM Purchase p JOIN p.items pi")
    BigDecimal getTotalAmount();
}
