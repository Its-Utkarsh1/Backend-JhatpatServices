package com.example.demo.Controller;

import com.example.demo.Dto.Response.ServiceResponse;
import com.example.demo.Model.Category;
import com.example.demo.Service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class UserServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> getServices(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search) {

        List<ServiceResponse> serv;

        if (search != null && !search.isBlank()) {
            serv = serviceService.search(search);
        }
        else if (category != null && !category.isBlank()) {
            serv = serviceService.getByCategory(
                    Category.valueOf(category.toUpperCase()));
        }
        else {
            serv = serviceService.getAllAvailable();
        }

        return ResponseEntity.ok(serv);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getService(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getById(id));
    }


}
