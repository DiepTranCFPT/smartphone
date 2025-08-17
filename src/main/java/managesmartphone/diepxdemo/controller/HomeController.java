package managesmartphone.diepxdemo.controller;

import managesmartphone.diepxdemo.entity.Customer;
import managesmartphone.diepxdemo.entity.Purchase;
import managesmartphone.diepxdemo.service.CustomerService;
import managesmartphone.diepxdemo.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("customer", new Customer());
        return "index";
    }

    @PostMapping("/search-customer")
    public String searchCustomer(@RequestParam String phone, Model model, RedirectAttributes redirectAttributes) {
        if (phone == null || phone.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập số điện thoại");
            return "redirect:/";
        }

        Optional<Customer> customerOpt = customerService.findByPhone(phone.trim());
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            List<Purchase> recentPurchases = purchaseService.findByCustomerId(customer.getId());

            model.addAttribute("customer", customer);
            model.addAttribute("recentPurchases", recentPurchases);
            model.addAttribute("showCustomerInfo", true);
            return "index";
        } else {
            Customer newCustomer = new Customer();
            newCustomer.setPhone(phone); // Điền sẵn số điện thoại vào form
            model.addAttribute("customer", newCustomer);
            model.addAttribute("phone", phone);
            model.addAttribute("showCreateForm", true);
            return "index";
        }
    }

    @PostMapping("/create-customer")
    public String createCustomer(@ModelAttribute Customer customer, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Kiểm tra xem số điện thoại đã tồn tại chưa
            Optional<Customer> existingCustomer = customerService.findByPhone(customer.getPhone().trim());
            if (existingCustomer.isPresent()) {
                model.addAttribute("error", "Số điện thoại đã tồn tại. Vui lòng sử dụng số điện thoại khác.");
                model.addAttribute("customer", customer);
                model.addAttribute("phone", customer.getPhone());
                model.addAttribute("showCreateForm", true);
                return "index";
            }

            Customer savedCustomer = customerService.save(customer);
            // Chuyển hướng trực tiếp đến tạo đơn hàng cho khách hàng vừa tạo
            return "redirect:/purchases/new/" + savedCustomer.getId();
        } catch (Exception e) {
            // Xử lý các lỗi khác
            String errorMessage = "Có lỗi xảy ra khi tạo khách hàng";
            if (e.getMessage().contains("duplicate") || e.getMessage().contains("unique")) {
                errorMessage = "Số điện thoại đã tồn tại. Vui lòng sử dụng số điện thoại khác.";
            }

            model.addAttribute("error", errorMessage);
            model.addAttribute("customer", customer);
            model.addAttribute("phone", customer.getPhone());
            model.addAttribute("showCreateForm", true);
            return "index";
        }
    }

    @GetMapping("/edit-customer/{id}")
    public String editCustomer(@PathVariable Long id, Model model) {
        Optional<Customer> customerOpt = customerService.findById(id);
        if (customerOpt.isPresent()) {
            model.addAttribute("customer", customerOpt.get());
            model.addAttribute("isEdit", true);
            return "customer-form";
        }
        return "redirect:/";
    }

    @PostMapping("/update-customer")
    public String updateCustomer(@ModelAttribute Customer customer, RedirectAttributes redirectAttributes) {
        try {
            customerService.save(customer);
            redirectAttributes.addFlashAttribute("success", "Thông tin khách hàng đã được cập nhật!");
            return "redirect:/?customerId=" + customer.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/customer-details/{id}")
    public String customerDetails(@PathVariable Long id, Model model) {
        Optional<Customer> customerOpt = customerService.findById(id);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            List<Purchase> purchases = purchaseService.findByCustomerId(id);

            model.addAttribute("customer", customer);
            model.addAttribute("recentPurchases", purchases);
            model.addAttribute("showCustomerInfo", true);
            return "index";
        }
        return "redirect:/";
    }

    @GetMapping("/api/customers/search")
    @ResponseBody
    public List<Customer> searchCustomers(@RequestParam String phone) {
        if (phone == null || phone.trim().length() < 3) {
            return new ArrayList<>();
        }

        // Tìm kiếm khách hàng có số điện thoại chứa chuỗi tìm kiếm
        return customerService.findByPhoneContaining(phone.trim());
    }
}
