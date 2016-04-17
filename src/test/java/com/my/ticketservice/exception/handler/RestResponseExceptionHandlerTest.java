package com.my.ticketservice.exception.handler;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.my.ticketservice.Application;

@SpringApplicationConfiguration(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class RestResponseExceptionHandlerTest {
	
	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void before() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void handleExceptionRuntimeException() throws Exception {
		mockMvc.perform(get("/api/runtimeException"))
		.andExpect(status().isInternalServerError())
		.andExpect(content().string(RestResponseExceptionHandler.SERVER_ERROR));
	}
	
	@Test
	public void handleExceptionMediaAssetManagementException() throws Exception {
		mockMvc.perform(get("/api/ticketServiceException"))
		.andExpect(status().isBadRequest())
		.andExpect(content().string("new exception"));
	}
	
	

}
