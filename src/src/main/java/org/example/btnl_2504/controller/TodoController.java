package org.example.btnl_2504.controller;

import jakarta.validation.Valid;
import org.example.btnl_2504.model.entity.Todo;
import org.example.btnl_2504.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class TodoController {
    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/")
    public String redirectToList() {
        return "redirect:/list";
    }

    @GetMapping("/list")
    public String listTodo(Model model) {
        model.addAttribute("todos", todoRepository.findAll());
        return "list";
    }

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("todo", new Todo());
        return "form";
    }

    @GetMapping("/form/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Todo> todoOptional = todoRepository.findById(id);
        if (todoOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Khong tim thay cong viec voi ID = " + id);
            return "redirect:/list";
        }
        model.addAttribute("todo", todoOptional.get());
        return "form";
    }

    @PostMapping("/form")
    public String addTodo(@Valid @ModelAttribute("todo") Todo todo,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "form";
        }
        todoRepository.save(todo);
        return "redirect:/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (!todoRepository.existsById(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Khong tim thay cong viec voi ID = " + id);
            return "redirect:/list";
        }
        todoRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Da xoa cong viec ID = " + id + " thanh cong");
        return "redirect:/list";
    }
}
