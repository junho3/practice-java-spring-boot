package com.example.demo.web.v1.stock;

import com.example.demo.core.stock.param.DecreaseStockParam;
import com.example.demo.core.stock.service.DecreaseStockService;
import com.example.demo.web.v1.stock.request.DecreaseStockRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("DecreaseStockController")
@WebMvcTest(controllers = DecreaseStockController.class)
class DecreaseStockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DecreaseStockService decreaseStockService;

    @Test
    @DisplayName("재고 차감 API 호출 시 정상적으로 처리된다")
    void decreaseStock() throws Exception {
        // given
        DecreaseStockRequest.Stock stock1 = new DecreaseStockRequest.Stock("P001", 5);
        DecreaseStockRequest.Stock stock2 = new DecreaseStockRequest.Stock("P002", 10);
        DecreaseStockRequest request = new DecreaseStockRequest(Set.of(stock1, stock2));

        // when & then
        mockMvc.perform(post("/v1/stocks/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(decreaseStockService).decrease(any(DecreaseStockParam.class));
    }

    @Test
    @DisplayName("재고 차감 요청 시 상품 코드가 비어있으면 400 에러를 반환한다")
    void decreaseStock_withEmptyProductCode_returnsBadRequest() throws Exception {
        // given
        DecreaseStockRequest.Stock invalidStock = new DecreaseStockRequest.Stock("", 5);
        DecreaseStockRequest request = new DecreaseStockRequest(Set.of(invalidStock));

        // when & then
        mockMvc.perform(post("/v1/stocks/decrease")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"));
    }

    @Test
    @DisplayName("재고 차감 요청 시 수량이 0 이하이면 400 에러를 반환한다")
    void decreaseStock_withInvalidQuantity_returnsBadRequest() throws Exception {
        // given
        DecreaseStockRequest.Stock invalidStock = new DecreaseStockRequest.Stock("P001", 0);
        DecreaseStockRequest request = new DecreaseStockRequest(Set.of(invalidStock));

        // when & then
        mockMvc.perform(post("/v1/stocks/decrease")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"));
    }

    @Test
    @DisplayName("재고 차감 요청 시 상품 목록이 비어있으면 400 에러를 반환한다")
    void decreaseStock_withEmptyStocks_returnsBadRequest() throws Exception {
        // given
        DecreaseStockRequest request = new DecreaseStockRequest(Set.of());

        // when & then
        mockMvc.perform(post("/v1/stocks/decrease")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.exception").value("MethodArgumentNotValidException"));
    }
}
