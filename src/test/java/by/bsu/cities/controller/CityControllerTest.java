package by.bsu.cities.controller;

import by.bsu.cities.ServerApplication;
import by.bsu.cities.config.SecurityConfig;
import by.bsu.cities.dto.CityDto;
import by.bsu.cities.dto.CityEditDto;
import by.bsu.cities.service.CityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CityController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = {SecurityConfig.class, ServerApplication.class})
public class CityControllerTest {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CityService cityService;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testGetCities() throws Exception {
        Page<CityDto> cities = new PageImpl<>( Arrays.asList(
                new CityDto(1, "New York", "image.png"),
                new CityDto(2, "Los Angeles", "image.png")
        ));

        Pageable pageable = PageRequest.of(0, 10);
        given(cityService.find(pageable, "york")).willReturn(cities);


        mockMvc.perform(get("/api/v1/cities")
                        .param("page", "0")
                        .param("size", "10")
                        .param("name", "york")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("New York")))
                .andExpect(jsonPath("$.content[0].photoUrl", is("image.png")))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].title", is("Los Angeles")))
                .andExpect(jsonPath("$.content[1].photoUrl", is("image.png")));
    }

    @Test
    public void testGetCity() throws Exception {
        CityDto city = new CityDto(1, "New York", "image.png");
        given(cityService.findById(anyLong())).willReturn(city);

        ResultActions response = mockMvc.perform(get("/api/v1/cities/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(city)));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("New York")))
                .andExpect((jsonPath("$.photoUrl", is("image.png"))));
    }

    @Test
    @WithMockUser(roles = "ALLOW_EDIT")
    public void testPatchCity() throws Exception {
        CityEditDto editDto = new CityEditDto("New York", "image.png");
        CityDto patchedCity = new CityDto(1, "New Stolin", "image2.png");
        given(cityService.patchCity(anyLong(), any(CityEditDto.class))).willReturn(patchedCity);

        mockMvc.perform(patch("/api/v1/cities/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(editDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("New Stolin")))
                .andExpect(jsonPath("$.photoUrl", is("image2.png")));
    }


    @Test()
    public void testPatchCityUnauthorized() throws Exception {
        CityEditDto editDto = new CityEditDto();
        editDto.setTitle("New title");
        editDto.setPhotoUrl("https://example.com/image.png");

        mockMvc.perform(patch("/cities/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editDto)))
                .andExpect(unauthenticated());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
