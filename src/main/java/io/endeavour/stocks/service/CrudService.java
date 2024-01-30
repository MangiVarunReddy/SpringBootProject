package io.endeavour.stocks.service;

import io.endeavour.stocks.StockException;
import io.endeavour.stocks.entity.crud.Address;
import io.endeavour.stocks.entity.crud.Person;
import io.endeavour.stocks.repository.crud.AddressRepository;
import io.endeavour.stocks.repository.crud.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CrudService {

    private final static Logger LOGGER= LoggerFactory.getLogger(CrudService.class);
    @Autowired
    PersonRepository personRepository;

    @Autowired
    AddressRepository addressRepository;

    public List<Person> getAllPersons(){
        LOGGER.debug("In the getAllPersons() method of the {} class", getClass());
        return personRepository.findAll();
    }

    public Optional<Person> getPerson(Integer personID) {
        return personRepository.findById(personID);
    }

    public Person insertPerson(Person person){
        Optional<List<Address>> addressList = Optional.ofNullable(person.getAddressList());
        addressList.ifPresent(addresses -> {
            addresses.forEach(address -> {
                address.setPerson(person);
            });
        });
        return  personRepository.save(person);
    }

    /**
     * Update
     * @param person
     * @param personID
     * @return
     */
    public Person updatePerson( Person person, Integer personID){
        if (personRepository.existsById(personID)){
            return insertPerson(person);
        }else {
            throw new StockException("Given PersonID to be updated, doesnot exist in the database");
        }
    }

    /**
     * Delete
     */
    public void deletePerson(Integer personID){
        if (personRepository.existsById(personID)){
            personRepository.deleteById(personID);
        }else {
            throw new StockException("Sent personID doesnot exist in the database");
        }
    }

public List<Address> getAllAddresses(){
      return   addressRepository.findAll();
}

}
