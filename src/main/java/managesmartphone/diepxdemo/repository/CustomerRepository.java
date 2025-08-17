package managesmartphone.diepxdemo.repository;

import managesmartphone.diepxdemo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByPhone(String phone);
    
    List<Customer> findByPhoneContaining(String phone);

    @Query("SELECT c FROM Customer c WHERE DATE(c.createdDate) = DATE(:date)")
    List<Customer> findByCreatedDate(@Param("date") LocalDateTime date);
    
    @Query("SELECT c FROM Customer c WHERE MONTH(c.createdDate) = :month AND YEAR(c.createdDate) = :year")
    List<Customer> findByCreatedMonth(@Param("month") int month, @Param("year") int year);
    
    @Query("SELECT c FROM Customer c WHERE YEAR(c.createdDate) = :year")
    List<Customer> findByCreatedYear(@Param("year") int year);
    
    @Query("SELECT COUNT(c) FROM Customer c WHERE DATE(c.createdDate) = DATE(:date)")
    long countByCreatedDate(@Param("date") LocalDateTime date);

    @Query("SELECT COUNT(c) FROM Customer c WHERE MONTH(c.createdDate) = :month AND YEAR(c.createdDate) = :year")
    long countByCreatedMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(c) FROM Customer c WHERE YEAR(c.createdDate) = :year")
    long countByCreatedYear(@Param("year") int year);
}
