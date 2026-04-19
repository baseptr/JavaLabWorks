package controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Beverage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.BeverageSelector;
import service.CoffeeService;

import java.util.List;

@Controller
public class BeverageController {

    //private final CoffeeService cs;
    private final BeverageSelector bs;
    private final ObjectMapper om = new ObjectMapper();

    public BeverageController(BeverageSelector service) {
        this.bs = service;
    }

    @GetMapping("/beverages")
    @ResponseBody
    public List<Beverage> getBeverages() {
        return bs.selectBeverage();
    }

    @GetMapping("/beveragesst")
    @ResponseBody
    public String getBeveragesAsString() throws JsonProcessingException {
        return om.writeValueAsString(bs.selectBeverage());
    }
}
