package guru.springframework.spring_6_rest_api.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import guru.springframework.spring_6_rest_api.model.CustomerDTO;
import guru.springframework.spring_6_rest_api.services.CustomerService;
import guru.springframework.spring_6_rest_api.services.CustomerServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
// These are all static imports
// ... doesn't usually display in your intelisense list so must be added manually
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

    @Test
    void getCustomerByIdNotFound() throws Exception {
        given(customerService.getCustomerById(any(UUID.class)))
            .willReturn(Optional.empty());
        
        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    void testPatchCustomer() throws JsonProcessingException, Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);

        // Provide the data to be patched (it is serialized below)
        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("name", "Atta Boy");

        given(customerService.patchById(any(), any())).willReturn(Optional.of(customer));

        mockMvc.perform(patch(CustomerController.CUSTOMER_PATH_ID, customer.getId())
            .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customerMap)))
            .andExpect(status().isNoContent());

        verify(customerService).patchById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        // Use ArgumentCaptor to assert that the correct field was patched.
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customer.getId());
        assertThat(customerArgumentCaptor.getValue().getName()).isEqualTo(customerMap.get("name"));
    }

    @Test
    void testDeleteCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);

        given(customerService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Use 'ArgumentCaptor' to get an accurate assertion on the identifier
        // to check whether the id property is being parsed properly
        // A very handy way of asserting that values are being sent through parts of your code properly
        verify(customerService).deleteById(uuidArgumentCaptor.capture());
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customer.getId());
    }

    @Test
    void testUpdateCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);

        given(customerService.updateCustomerById(any(), any())).willReturn(Optional.of(customer));

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
            .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
            .andExpect(status().isNoContent());

        // Verify that "updateCustomerById" has been called
        verify(customerService).updateCustomerById(uuidArgumentCaptor.capture(), any(CustomerDTO.class));
        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(customer.getId());
    }

    @Test
    void testCreateCustomer() throws Exception {
        CustomerDTO customer = customerServiceImpl.listCustomers().get(0);
        
        customer.setId(null);
        customer.setVersion(null);
        
        given(customerService.saveNewCustomer(any(CustomerDTO.class)))
            .willReturn(customerServiceImpl.listCustomers().get(1));

        // Emulating what's happening in the database so returning the "correct" customer
        // isn't important. Currently testing the basic response properties.
        mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
            .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
    }

    @Test
    void testListCustomers() throws Exception {
        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getBeerById() throws Exception {
        CustomerDTO testCustomer = customerServiceImpl.listCustomers().get(0);

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH_ID, testCustomer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
            .andExpect(jsonPath("$.name", is(testCustomer.getName().toString())));

    }
}
