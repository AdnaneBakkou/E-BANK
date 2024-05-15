package ma.bakkou.projerebank.web;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.bakkou.projerebank.dtos.CustomerDTO;
import ma.bakkou.projerebank.entities.Customer;
import ma.bakkou.projerebank.exceptions.CustomerNotFoundException;
import ma.bakkou.projerebank.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j

public class CustomerRestController {

    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountService.listCustomers();
    }

    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomr(@PathVariable(name ="id") Long customerId) throws CustomerNotFoundException {
return bankAccountService.getCustomer(customerId);

    }

    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
return  bankAccountService.saveCustmer(customerDTO);
    }

    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId  ,CustomerDTO customerDTO){

        customerDTO.setId(customerId);
        return         bankAccountService.updateCustmer(customerDTO);


    }


}