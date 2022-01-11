package hellojpa.jpa.controller;

import hellojpa.jpa.domain.item.Book;
import hellojpa.jpa.domain.item.Item;
import hellojpa.jpa.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ItemController {


    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {


        ItemForm itemForm = new ItemForm();
        model.addAttribute("form", itemForm);

        return "items/createItemForm";

    }

    @PostMapping("/items/new")
    public String create(@ModelAttribute(name = "form")
                               ItemForm form) {


        Book book = new Book();


        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);

        return "redirect:/items";

    }


    @GetMapping("/items")
    public String itemList(Model model) {

        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);

        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String editForm(@PathVariable(name = "itemId") Long itemId,
                           Model model) {

        log.info("EDIT FORM CONTROLLER START");

        Book item = (Book) itemService.findOne(itemId);

        ItemForm itemForm = new ItemForm();
        itemForm.setId(item.getId());
        itemForm.setAuthor(item.getAuthor());
        itemForm.setIsbn(item.getIsbn());
        itemForm.setPrice(item.getPrice());
        itemForm.setName(item.getName());
        itemForm.setStockQuantity(item.getStockQuantity());

        model.addAttribute("form", itemForm);

        return "items/updateItemForm";
    }

    @PostMapping("/items/{itemId}/edit")
    public String editForm(@ModelAttribute(name = "form") ItemForm
            form) {


        Long itemId = form.getId();
        String name = form.getName();
        int price = form.getPrice();
        int stockQuantity = form.getStockQuantity();

        itemService.updateItem(itemId, name, price, stockQuantity);


        return "redirect:/items";
    }







}
