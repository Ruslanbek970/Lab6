package com.example.manager.controller;

import com.example.manager.model.ApplicationRequest;
import com.example.manager.repository.CourseRepository;
import com.example.manager.repository.OperatorRepository;
import com.example.manager.service.ApplicationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class ApplicationRequestController {

    private final ApplicationRequestService service;
    private final CourseRepository courseRepo;
    private final OperatorRepository operatorRepo;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("requests", service.getAll());
        model.addAttribute("newCount", service.getPending().size());
        model.addAttribute("processedCount", service.getProcessed().size());
        return "index";
    }

    @GetMapping("/requests/new")
    public String createForm(Model model) {
        model.addAttribute("applicationRequest", new ApplicationRequest());
        model.addAttribute("courses", courseRepo.findAll());
        return "add_request";
    }

    @PostMapping("/requests")
    public String create(@ModelAttribute ApplicationRequest request,
                         @RequestParam("courseId") Long courseId) {
        courseRepo.findById(courseId).ifPresent(request::setCourse);
        request.setHandled(false);
        service.save(request);
        return "redirect:/";
    }

    @GetMapping("/requests/{id}")
    public String details(@PathVariable Long id, Model model) {
        service.getById(id).ifPresent(req -> model.addAttribute("req", req));
        model.addAttribute("operators", operatorRepo.findAll());
        return "details";
    }

    @GetMapping("/requests/{id}/process")
    public String processForm(@PathVariable Long id, Model model) {
        var maybe = service.getById(id);
        if (maybe.isEmpty()) {
            return "redirect:/";
        }
        var req = maybe.get();
        if (req.isHandled()) {
            return "redirect:/requests/" + id;
        }
        model.addAttribute("req", req);
        model.addAttribute("operators", operatorRepo.findAll());
        return "process_operators";
    }

    @PostMapping("/requests/{id}/process")
    public String processAssign(@PathVariable Long id,
                                @RequestParam(name = "operatorIds", required = false) List<Long> operatorIds) {
        if (operatorIds == null) operatorIds = List.of();
        service.assignOperators(id, operatorIds);
        return "redirect:/requests/" + id;
    }

    @PostMapping("/requests/{id}/operators/{operatorId}/remove")
    public String removeOperator(@PathVariable Long id, @PathVariable Long operatorId) {
        service.removeOperator(id, operatorId);
        return "redirect:/requests/" + id;
    }

    @PostMapping("/requests/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/";
    }



    @GetMapping("/requests/pending")
    public String pending(Model model) {
        model.addAttribute("requests", service.getPending());
        return "index";
    }

    @GetMapping("/requests/processed")
    public String processed(Model model) {
        model.addAttribute("requests", service.getProcessed());
        return "index";
    }
}
