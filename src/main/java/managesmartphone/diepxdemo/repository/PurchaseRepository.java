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

    @Query("SELECT DISTINCT p FROM Purchase p LEFT JOIN FETCH p.items WHERE p.customer.id = :customerId ORDER BY p.date DESC")
    List<Purchase> findByCustomerIdOrderByDateDesc(@Param("customerId") Long customerId);

    @Query("SELECT DISTINCT p FROM Purchase p LEFT JOIN FETCH p.items WHERE DATE(p.date) = DATE(:date) ORDER BY p.date DESC")
    List<Purchase> findByDate(@Param("date") LocalDateTime date);

    @Query("SELECT DISTINCT p FROM Purchase p LEFT JOIN FETCH p.items WHERE EXTRACT(MONTH FROM p.date) = :month AND EXTRACT(YEAR FROM p.date) = :year ORDER BY p.date DESC")
    List<Purchase> findByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT DISTINCT p FROM Purchase p LEFT JOIN FETCH p.items WHERE EXTRACT(YEAR FROM p.date) = :year ORDER BY p.date DESC")
    List<Purchase> findByYear(@Param("year") int year);

    @Query("SELECT SUM(pi.unitPrice * pi.qty) FROM Purchase p JOIN p.items pi WHERE DATE(p.date) = DATE(:date)")
    BigDecimal getTotalAmountByDate(@Param("date") LocalDateTime date);

    @Query("SELECT SUM(pi.unitPrice * pi.qty) FROM Purchase p JOIN p.items pi WHERE EXTRACT(MONTH FROM p.date) = :month AND EXTRACT(YEAR FROM p.date) = :year")
    BigDecimal getTotalAmountByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT SUM(pi.unitPrice * pi.qty) FROM Purchase p JOIN p.items pi WHERE EXTRACT(YEAR FROM p.date) = :year")
    BigDecimal getTotalAmountByYear(@Param("year") int year);

    @Query("SELECT SUM(pi.unitPrice * pi.qty) FROM Purchase p JOIN p.items pi")
    BigDecimal getTotalAmount();

    @Query("SELECT DISTINCT p FROM Purchase p LEFT JOIN FETCH p.items WHERE p.id = :id")
    Purchase findByIdWithItems(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Purchase p LEFT JOIN FETCH p.items ORDER BY p.date DESC")
    List<Purchase> findAllOrderByDateDesc();
}
