package io.endeavour.stocks.controller;

import io.endeavour.stocks.entity.crud.Person;
import io.endeavour.stocks.service.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@RestController()
@RequestMapping(value = "/crud")
public class CrudController {

    private final static Logger LOGGER= LoggerFactory.getLogger(CrudService.class);
    @Autowired
    CrudService crudService;

    @GetMapping(value = "/getAllPersons")
    public List<Person> getAllPersons(){
        LOGGER.debug("In getAllPersons() method of class {} ",getClass());
        return  crudService.getAllPersons();
    }

    @GetMapping(value = "/getPerson")
    public ResponseEntity<Person> getPerson(@RequestParam(value = "personID") Optional<Integer> personIDOptional){
        if (personIDOptional.isPresent()){
            return ResponseEntity.of(crudService.getPerson(personIDOptional.get()));
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Person ID not sent");
        }

    }

}
