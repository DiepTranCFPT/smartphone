package managesmartphone.diepxdemo.controller;

import managesmartphone.diepxdemo.entity.Customer;
import managesmartphone.diepxdemo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String customers(
            @RequestParam(required = false) String filterType,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) Integer year,
            Model model) {

        List<Customer> customers;
        long totalCustomers;

        if (filterType != null) {
            switch (filterType) {
                case "date":
                    if (date != null && !date.isEmpty()) {
                        LocalDateTime filterDate = LocalDateTime.parse(date + "T00:00:00");
                        customers = customerService.findByCreatedDate(filterDate);
                        totalCustomers = customerService.countByCreatedDate(filterDate);
                        model.addAttribute("selectedDate", date);
                    } else {
                        customers = customerService.findAll();
                        totalCustomers = customerService.countAll();
                    }
                    break;
                case "month":
                    if (month != null && !month.isEmpty()) {
                        try {
                            String[] parts = month.split("-");
                            int yearValue = Integer.parseInt(parts[0]);
                            int monthValue = Integer.parseInt(parts[1]);
                            customers = customerService.findByCreatedMonth(monthValue, yearValue);
                            totalCustomers = customerService.countByCreatedMonth(monthValue, yearValue);

                            model.addAttribute("selectedMonthStr", month);
                        } catch (Exception e) {
                            customers = customerService.findAll();
                            totalCustomers = customerService.countAll();
                        }
                    } else {
                        customers = customerService.findAll();
                        totalCustomers = customerService.countAll();
                    }
                    break;
                case "year":
                    if (year != null) {
                        customers = customerService.findByCreatedYear(year);
                        totalCustomers = customerService.countByCreatedYear(year);
                        model.addAttribute("selectedYear", year);
                    } else {
                        customers = customerService.findAll();
                        totalCustomers = customerService.countAll();
                    }
                    break;
                default:
                    customers = customerService.findAll();
                    totalCustomers = customerService.countAll();
                    break;
            }
        } else {
            customers = customerService.findAll();
            totalCustomers = customerService.countAll();
            filterType = "all";
        }

        model.addAttribute("customers", customers);
        model.addAttribute("totalCustomers", totalCustomers);
        model.addAttribute("filterType", filterType);

        return "customers";
    }

    // Hàm hiển thị form tạo khách hàng mới
    @GetMapping("/new")
    public String newCustomer(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("isEdit", false);
        return "customer-form";
    }

    // Hàm xử lý tạo khách hàng mới
    @PostMapping
    public String saveCustomer(@ModelAttribute Customer customer, Model model) {
        try {
            // Kiểm tra xem số điện thoại đã tồn tại chưa
            Optional<Customer> existingCustomer = customerService.findByPhone(customer.getPhone().trim());
            if (existingCustomer.isPresent()) {
                model.addAttribute("error", "Số điện thoại đã tồn tại. Vui lòng sử dụng số điện thoại khác.");
                model.addAttribute("customer", customer);
                model.addAttribute("isEdit", false);
                return "customer-form";
            }

            Customer savedCustomer = customerService.save(customer);
            // Chuyển hướng tự động đến tạo đơn hàng cho khách hàng vừa tạo
            return "redirect:/purchases/new/" + savedCustomer.getId();
        } catch (Exception e) {
            // Xử lý các lỗi khác
            String errorMessage = "Lỗi khi tạo khách hàng";
            if (e.getMessage().contains("duplicate") || e.getMessage().contains("unique")) {
                errorMessage = "Số điện thoại đã tồn tại. Vui lòng sử dụng số điện thoại khác.";
            } else {
                errorMessage = "Lỗi khi tạo khách hàng: " + e.getMessage();
            }

            model.addAttribute("error", errorMessage);
            model.addAttribute("customer", customer);
            model.addAttribute("isEdit", false);
            return "customer-form";
        }
    }

    // Hàm hiển thị form chỉnh sửa khách hàng
    @GetMapping("/edit/{id}")
    public String editCustomer(@PathVariable Long id, Model model) {
        Optional<Customer> customerOpt = customerService.findById(id);
        if (customerOpt.isPresent()) {
            model.addAttribute("customer", customerOpt.get());
            model.addAttribute("isEdit", true);
            return "customer-form";
        }
        return "redirect:/customers";
    }

    // Hàm xử lý cập nhật thông tin khách hàng
    @PostMapping("/update/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute Customer customer, Model model) {
        try {
            customer.setId(id);
            customerService.save(customer);
            return "redirect:/customers";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi khi cập nhật khách hàng: " + e.getMessage());
            model.addAttribute("customer", customer);
            model.addAttribute("isEdit", true);
            return "customer-form";
        }
    }
}
