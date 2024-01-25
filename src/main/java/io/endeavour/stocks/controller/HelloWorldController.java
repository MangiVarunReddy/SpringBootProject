package io.endeavour.stocks.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
public class HelloWorldController {

    @GetMapping(value = "/helloworld")
    public String firstMethod(){
        return "Hello Varun";
    }
    @GetMapping(value = "hello/world")
    public String callingHelloWorld(){
        return "Hello World Again";
    }
    @GetMapping(value = "concatString/{inputString}")
    public String concatString(@PathVariable(value ="inputString" ) String sampleString){ //Sending String Value in path parameter
        return sampleString+sampleString;
    }
    @GetMapping(value = "concatStringDate/{inputString}/{inputDate}")
    public String concatStringDate(@PathVariable(value ="inputString" ) String sampleString,
                                  @PathVariable(value = "inputDate") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                  LocalDate sampleDate){
        return sampleString+" "+sampleDate.minusMonths(5);
    }
    @GetMapping(value = "concatStringDate/queryParam/{inputString}")
    public String concatStringDateQP(@PathVariable(value = "inputString") String sampleString,
                                     @RequestParam(value = "inputDate") @DateTimeFormat(pattern = "yyyy-MM-dd")
                                     LocalDate sampleDate){
        return sampleDate+" "+sampleDate.plusMonths(6);
    }

    @PostMapping(value = "sortTickers/{inputString}")
    public List<String> sortTickerList(@PathVariable(value = "inputString") String sampleString,
                                       @RequestParam(value = "inputDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate someDate,
                                       @RequestBody List<String> tickerList){
        Collections.sort(tickerList);
        return tickerList;
    }

}
