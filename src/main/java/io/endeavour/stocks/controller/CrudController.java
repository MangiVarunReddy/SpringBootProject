package io.endeavour.stocks.controller;

import io.endeavour.stocks.StockException;
import io.endeavour.stocks.entity.crud.Address;
import io.endeavour.stocks.entity.crud.Person;
import io.endeavour.stocks.service.CrudService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
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

    @PostMapping(value = "/insertPerson")
    public Person insertPerson(@RequestBody Optional<Person> personOptional){
        if (personOptional.isPresent()){
            return  crudService.insertPerson(personOptional.get());
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Person object sent for insertion is null");
        }
    }

    @PutMapping(value = "/updatePerson")
    public Person updatePerson(@RequestBody Optional<Person> personOptional,
                               @RequestParam(name = "personID") Integer personID){
        if (personOptional.isPresent()){
            if (personOptional.get().getPersonID()==personID){
                return crudService.updatePerson(personOptional.get(),personID);
            }else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"PersonID sent in the URL mismatch with personID in the object");

            }
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Person object sent is null");
        }
    }

    @DeleteMapping(value = "/deletePerson/{personID}")
    public void deletePerson(@PathVariable(value = "personID") Integer personID){
        crudService.deletePerson(personID);
    }

    @GetMapping(value = "/getAlladdresses")
    public List<Address> getAlladdresses(){
      return   crudService.getAllAddresses();
    }

    @ExceptionHandler({StockException.class})
    public ResponseEntity generateExceptionResponse(Exception e){
        return ResponseEntity.notFound().header("ErrorMessage",e.getMessage()).build();
    }

}
