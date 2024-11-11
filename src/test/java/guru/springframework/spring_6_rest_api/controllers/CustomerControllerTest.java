package guru.springframework.spring_6_rest_api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring_6_rest_api.model.Customer;
import guru.springframework.spring_6_rest_api.services.CustomerService;
import guru.springframework.spring_6_rest_api.services.CustomerServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
// These are all static imports
// ... doesn't usually display in your intelisense list so must be added manually
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.UUID;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Test
    void testDeleteCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().get(0);

        mockMvc.perform(delete("/api/v1/customer/" + customer.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Use 'ArgumentCaptor' to get an accurate assertion on the identifier
        // to check whether the id property is being parsed properly
        // A very handy way of asserting that values are being sent through parts of your code properly
        verify(customerService).deleteById(uuidArgumentCaptor.capture());
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().get(0);

        mockMvc.perform(put("/api/v1/customer/" + customer.getId())
            .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
            .andExpect(status().isNoContent());

        // Verify that "updateCustomerById" has been called
        verify(customerService).updateCustomerById(uuidArgumentCaptor.capture(), any(Customer.class));
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testCreateCustomer() throws Exception {
        Customer customer = customerServiceImpl.listCustomers().get(0);
        
        customer.setId(null);
        customer.setVersion(null);
        
        given(customerService.saveNewCustomer(any(Customer.class)))
            .willReturn(customerServiceImpl.listCustomers().get(1));

        // Emulating what's happening in the database so returning the "correct" customer
        // isn't important. Currently testing the basic response properties.
        mockMvc.perform(post("/api/v1/customer")
            .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
    }

    @Test
    void testListCustomers() throws Exception {
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get("/api/v1/customer").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getBeerById() throws Exception {
        Customer testCustomer = customerServiceImpl.listCustomers().get(0);

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(testCustomer);

        mockMvc.perform(get("/api/v1/customer/" + testCustomer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
            .andExpect(jsonPath("$.name", is(testCustomer.getName().toString())));

    }
}
