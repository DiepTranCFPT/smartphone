package managesmartphone.diepxdemo.controller;

import managesmartphone.diepxdemo.entity.Customer;
import managesmartphone.diepxdemo.entity.Purchase;
import managesmartphone.diepxdemo.entity.PurchaseItem;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public String purchases(
            @RequestParam(required = false) String filterType,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) Integer year,
            Model model) {

        List<Purchase> purchases;
        BigDecimal totalAmount;

        if (filterType != null) {
            switch (filterType) {
                case "date":
                    if (date != null && !date.isEmpty()) {
                        LocalDateTime filterDate = LocalDateTime.parse(date + "T00:00:00");
                        purchases = purchaseService.findByDate(filterDate);
                        totalAmount = purchaseService.getTotalAmountByDate(filterDate);
                        model.addAttribute("selectedDate", date);
                    } else {
                        purchases = purchaseService.findAllOrderByDateDesc();
                        totalAmount = purchaseService.getTotalAmount();
                    }
                    break;
                case "month":
                    if (month != null && !month.isEmpty()) {
                        String[] parts = month.split("-");
                        int yearValue = Integer.parseInt(parts[0]);
                        int monthValue = Integer.parseInt(parts[1]);
                        purchases = purchaseService.findByMonth(monthValue, yearValue);
                        totalAmount = purchaseService.getTotalAmountByMonth(monthValue, yearValue);
                        model.addAttribute("selectedMonth", month);
                    } else {
                        purchases = purchaseService.findAllOrderByDateDesc();
                        totalAmount = purchaseService.getTotalAmount();
                    }
                    break;
                case "year":
                    if (year != null) {
                        purchases = purchaseService.findByYear(year);
                        totalAmount = purchaseService.getTotalAmountByYear(year);
                        model.addAttribute("selectedYear", year);
                    } else {
                        purchases = purchaseService.findAllOrderByDateDesc();
                        totalAmount = purchaseService.getTotalAmount();
                    }
                    break;
                default:
                    purchases = purchaseService.findAllOrderByDateDesc();
                    totalAmount = purchaseService.getTotalAmount();
                    break;
            }
        } else {
            purchases = purchaseService.findAllOrderByDateDesc();
            totalAmount = purchaseService.getTotalAmount();
            filterType = "all";
        }

        model.addAttribute("purchases", purchases);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("filterType", filterType);

        return "purchases";
    }

    // API endpoint để tạo đơn hàng mới - hiển thị form tìm kiếm khách hàng
    @GetMapping("/create-new")
    public String createNewPurchase(Model model) {
        Purchase purchase = new Purchase();
        purchase.setItems(new ArrayList<>());
        purchase.getItems().add(new PurchaseItem()); // Add initial empty item

        model.addAttribute("purchase", purchase);
        model.addAttribute("customer", new Customer());
        model.addAttribute("showSearchForm", true);
        return "purchase-create";
    }

    // API endpoint để tìm kiếm khách hàng cho đơn hàng mới
    @PostMapping("/search-customer-for-purchase")
    public String searchCustomerForPurchase(@RequestParam String phone, Model model, RedirectAttributes redirectAttributes) {
        if (phone == null || phone.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng nhập số điện thoại");
            return "redirect:/purchases/create-new";
        }

        Optional<Customer> customerOpt = customerService.findByPhone(phone.trim());
        if (customerOpt.isPresent()) {
            // Khách hàng đã tồn tại - chuyển đến tạo đơn hàng
            Customer customer = customerOpt.get();
            return "redirect:/purchases/new/" + customer.getId();
        } else {
            // Khách hàng chưa tồn tại - hiển thị form tạo khách hàng
            Customer newCustomer = new Customer();
            newCustomer.setPhone(phone);
            model.addAttribute("customer", newCustomer);
            model.addAttribute("phone", phone);
            model.addAttribute("showCreateForm", true);
            return "purchase-create";
        }
    }

    // API endpoint để tạo khách hàng mới từ trang tạo đơn hàng
    @PostMapping("/create-customer-for-purchase")
    public String createCustomerForPurchase(@ModelAttribute Customer customer, Model model) {
        try {
            // Kiểm tra số điện thoại đã tồn tại chưa
            Optional<Customer> existingCustomer = customerService.findByPhone(customer.getPhone().trim());
            if (existingCustomer.isPresent()) {
                model.addAttribute("error", "Số điện thoại đã tồn tại. Vui lòng sử dụng số điện thoại khác.");
                model.addAttribute("customer", customer);
                model.addAttribute("phone", customer.getPhone());
                model.addAttribute("showCreateForm", true);
                return "purchase-create";
            }

            Customer savedCustomer = customerService.save(customer);
            // Chuyển hướng đến tạo đơn hàng cho khách hàng vừa tạo
            return "redirect:/purchases/new/" + savedCustomer.getId();
        } catch (Exception e) {
            String errorMessage = "Có lỗi xảy ra khi tạo khách hàng";
            if (e.getMessage().contains("duplicate") || e.getMessage().contains("unique")) {
                errorMessage = "Số điện thoại đã tồn tại. Vui lòng sử dụng số điện thoại khác.";
            }

            model.addAttribute("error", errorMessage);
            model.addAttribute("customer", customer);
            model.addAttribute("phone", customer.getPhone());
            model.addAttribute("showCreateForm", true);
            return "purchase-create";
        }
    }

    @GetMapping("/new/{customerId}")
    public String createPurchaseForCustomer(@PathVariable Long customerId, Model model) {
        Optional<Customer> customerOpt = customerService.findById(customerId);
        if (!customerOpt.isPresent()) {
            return "redirect:/purchases/create-new";
        }

        Purchase purchase = new Purchase();
        purchase.setCustomer(customerOpt.get());
        purchase.setItems(new ArrayList<>());
        purchase.getItems().add(new PurchaseItem()); // Add initial empty item
        purchase.setDate(LocalDateTime.now().plusHours(6));

        model.addAttribute("purchase", purchase);
        model.addAttribute("customer", customerOpt.get());
        return "purchase-form";
    }

    @GetMapping("/edit/{id}")
    public String editPurchase(@PathVariable Long id, Model model) {
        Optional<Purchase> purchaseOpt = purchaseService.findById(id);
        if (purchaseOpt.isPresent()) {
            Purchase purchase = purchaseOpt.get();
            // ensure at least one item exists for the form
            if (purchase.getItems() == null || purchase.getItems().isEmpty()) {
                purchase.addItem(new PurchaseItem());
            }
            model.addAttribute("purchase", purchase);
            model.addAttribute("customer", purchase.getCustomer());
            model.addAttribute("isEdit", true);
            return "purchase-form";
        }
        return "redirect:/purchases";
    }

    @PostMapping("/save")
    public String savePurchase(@ModelAttribute Purchase purchase, RedirectAttributes redirectAttributes) {
        try {
            if (purchase.getId() == null) {
                purchase.setDate(LocalDateTime.now().plusHours(6));
            }

            // Remove empty items and set back-reference
            if (purchase.getItems() != null) {
                Iterator<PurchaseItem> it = purchase.getItems().iterator();
                while (it.hasNext()) {
                    PurchaseItem item = it.next();
                    boolean emptyName = (item.getItemName() == null || item.getItemName().trim().isEmpty());
                    boolean invalidQty = (item.getQty() == null || item.getQty() <= 0);
                    boolean missingPrice = (item.getUnitPrice() == null);
                    if (emptyName || invalidQty || missingPrice) {
                        it.remove();
                    } else {
                        item.setPurchase(purchase);
                    }
                }
            }

            purchaseService.save(purchase);
            redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được lưu thành công!");
            return "redirect:/?customerId=" + purchase.getCustomer().getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi lưu đơn hàng: " + e.getMessage());
            return "redirect:/purchases";
        }
    }

    @PostMapping("/delete/{id}")
    public String deletePurchase(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Purchase> purchaseOpt = purchaseService.findById(id);
            if (purchaseOpt.isPresent()) {
                Long customerId = purchaseOpt.get().getCustomer().getId();
                purchaseService.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Đơn hàng đã được xóa thành công!");
                return "redirect:/?customerId=" + customerId;
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi xóa đơn hàng: " + e.getMessage());
        }
        return "redirect:/purchases";
    }

    @GetMapping("/print/{id}")
    public String printPurchase(@PathVariable Long id, Model model) {
        Optional<Purchase> purchaseOpt = purchaseService.findById(id);
        if (purchaseOpt.isPresent()) {
            Purchase purchase = purchaseOpt.get();
            model.addAttribute("purchase", purchase);
            model.addAttribute("customer", purchase.getCustomer());
            model.addAttribute("currentDate", LocalDateTime.now().plusHours(6));
            return "invoice";
        }
        return "redirect:/purchases";
    }

    @PostMapping("/print-multiple")
    public String printMultiplePurchases(@RequestParam("purchaseIds") List<Long> purchaseIds, Model model) {
        List<Purchase> purchases = new ArrayList<>();
        for (Long id : purchaseIds) {
            Optional<Purchase> purchaseOpt = purchaseService.findById(id);
            if (purchaseOpt.isPresent()) {
                purchases.add(purchaseOpt.get());
            }
        }

        if (!purchases.isEmpty()) {
            model.addAttribute("purchases", purchases);
            model.addAttribute("currentDate", LocalDateTime.now().plusHours(6));
            model.addAttribute("isMultiple", true);
            return "invoice";
        }
        return "redirect:/purchases";
    }

    // API endpoint để lấy danh sách tất cả khách hàng
    @GetMapping("/api/customers/all")
    @ResponseBody
    public List<Customer> getAllCustomers() {
        return customerService.findAll();
    }

    // API endpoint để tìm kiếm khách hàng theo số điện thoại (cho suggestions)
    @GetMapping("/api/customers/search")
    @ResponseBody
    public List<Customer> searchCustomers(@RequestParam String phone) {
        if (phone == null || phone.trim().length() < 3) {
            return new ArrayList<>();
        }
        return customerService.findByPhoneContaining(phone.trim());
    }
}
