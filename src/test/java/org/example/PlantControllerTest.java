package org.example;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.example.controllers.ApiExceptionHandler;
import org.example.controllers.PlantController;
import org.example.dto.GrowthState;
import org.example.dto.PlantState;
import org.example.dto.SensorData;
import org.example.entites.Plant;
import org.example.entites.Species;
import org.example.services.PlantServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = PlantController.class)
@Import(ApiExceptionHandler.class)
class PlantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PlantServices plantServices;

    @Test
    void createPlant_returnsSavedPlant() throws Exception {
        Species species = new Species("species-1", "Tulip");
        Plant toCreate = new Plant(null, "My Plant", species, 0.3, LocalDate.now());
        Plant saved = new Plant("plant-1", toCreate.getName(), species, toCreate.getHeight(), toCreate.getPlantedDate());

        when(plantServices.createPlant(any(Plant.class))).thenReturn(saved);

        mockMvc.perform(
                        post("/plants")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(toCreate))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is("plant-1")))
                .andExpect(jsonPath("$.name", is("My Plant")))
                .andExpect(jsonPath("$.species.id", is("species-1")));
    }

    @Test
    void createPlant_withInvalidPayload_returnsBadRequest() throws Exception {
        Species species = new Species("species-1", "Tulip");
        Plant invalid = new Plant(null, "", species, -1, LocalDate.now().plusDays(1));

        mockMvc.perform(
                        post("/plants")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalid))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Données invalides")))
                .andExpect(jsonPath("$.errors.name", notNullValue()))
                .andExpect(jsonPath("$.errors.height", notNullValue()))
                .andExpect(jsonPath("$.errors.plantedDate", notNullValue()));
    }

    @Test
    void getState_returnsPlantState() throws Exception {
        SensorData sensors = new SensorData(55, 22, 300);
        GrowthState growth = new GrowthState(0.35, 0.02);
        PlantState state = new PlantState(sensors, growth);

        when(plantServices.getState(eq("plant-1"))).thenReturn(state);

        mockMvc.perform(get("/plants/plant-1/state"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sensors.humidity", is(55.0)))
                .andExpect(jsonPath("$.growth.newHeight", is(0.35)));
    }
}
