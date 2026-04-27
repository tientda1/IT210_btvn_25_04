package org.example.btnl_2504.controller;

import jakarta.validation.Valid;
import org.example.btnl_2504.model.entity.Todo;
import org.example.btnl_2504.repository.TodoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class TodoController {
    @Autowired
    private TodoRepository todoRepository;

    private static final String OWNER_NAME = "ownerName";

    private boolean isOwnerMissing(HttpSession session) {
        Object owner = session.getAttribute(OWNER_NAME);
        return owner == null || owner.toString().trim().isEmpty();
    }

    @GetMapping("/")
    public String redirectRoot(HttpSession session) {
        return isOwnerMissing(session) ? "redirect:/welcome" : "redirect:/todos";
    }

    @GetMapping("/welcome")
    public String showWelcome(HttpSession session, Model model) {
        Object owner = session.getAttribute(OWNER_NAME);
        model.addAttribute(OWNER_NAME, owner == null ? "" : owner.toString());
        return "welcome";
    }

    @PostMapping("/welcome")
    public String saveOwner(@RequestParam("ownerName") String ownerName,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        String trimmedOwnerName = ownerName == null ? "" : ownerName.trim();
        if (trimmedOwnerName.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ten chu so huu khong duoc de trong");
            return "redirect:/welcome";
        }

        session.setAttribute(OWNER_NAME, trimmedOwnerName);
        return "redirect:/todos";
    }

    @GetMapping({"/todos", "/list"})
    public String listTodo(Model model, HttpSession session) {
        if (isOwnerMissing(session)) {
            return "redirect:/welcome";
        }

        model.addAttribute(OWNER_NAME, session.getAttribute(OWNER_NAME));
        model.addAttribute("todos", todoRepository.findAll());
        return "list";
    }

    @GetMapping({"/todos/form", "/form"})
    public String showForm(Model model, HttpSession session) {
        if (isOwnerMissing(session)) {
            return "redirect:/welcome";
        }

        model.addAttribute("todo", new Todo());
        return "form";
    }

    @GetMapping({"/todos/form/{id}", "/form/{id}"})
    public String showEditForm(@PathVariable Long id,
                               Model model,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        if (isOwnerMissing(session)) {
            return "redirect:/welcome";
        }

        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Khong tim thay cong viec voi ID = " + id);
            return "redirect:/todos";
        }
        model.addAttribute("todo", todoOptional.get());
        return "form";
    }

    @PostMapping({"/todos/form", "/form"})
    public String addTodo(@Valid @ModelAttribute("todo") Todo todo,
                          BindingResult bindingResult,
                          HttpSession session) {
        if (isOwnerMissing(session)) {
            return "redirect:/welcome";
        }

        if (bindingResult.hasErrors()) {
            return "form";
        }
        todoRepository.save(todo);
        return "redirect:/todos";
    }

    @PostMapping({"/todos/delete/{id}", "/delete/{id}"})
    public String deleteTodo(@PathVariable Long id,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        if (isOwnerMissing(session)) {
            return "redirect:/welcome";
        }

        if (!todoRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Khong tim thay cong viec voi ID = " + id);
            return "redirect:/todos";
        }
        todoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Da xoa cong viec ID = " + id + " thanh cong");
        return "redirect:/todos";
    }
}
