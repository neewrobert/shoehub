package com.neewrobert.shoehub

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import kotlin.random.Random

@SpringBootTest
@AutoConfigureMockMvc
class ShoeHubControllerIT(

    @Autowired
    val mockMvc: MockMvc
) {

    @Test
    fun givenAValidShoeId_shouldExpectSuccess() {
        //given
        val shoeId = Random.nextInt(0, 100)

        //when
        val resultActions =
            mockMvc.perform(MockMvcRequestBuilders.get("/shoe/$shoeId/buy"))

        //then - verify the output
        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun givenAInvalidShoeId_shouldExpectBadRequest() {
        //given
        val shoeId = -1

        //when
        val resultActions =
            mockMvc.perform(MockMvcRequestBuilders.get("/shoe/$shoeId/buy"))

        //then - verify the output
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun givenANotFoundShoeId_shoeExpectNotFound() {
        //given
        val shoeId = Random.nextInt(from = 100, until = 1000)

        //when
        val resultActions =
            mockMvc.perform(MockMvcRequestBuilders.get("/shoe/$shoeId/buy"))

        //then - verify the output
        resultActions.andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun givenPaymentMethodEqualCash_shouldExpectSuccess() {

        //given
        val paymentMethod = "Cash"

        //when
        val resultActions =
            mockMvc.perform(MockMvcRequestBuilders.post("/shoe/payment").queryParam("paymentMethod", paymentMethod))

        //then - verify the output
        resultActions.andExpect(MockMvcResultMatchers.status().isOk)

    }

    @Test
    fun givenPaymentMethodEqualCard_shouldExpectSuccess() {

        //given
        val paymentMethod = "Card"

        //when
        val resultActions =
            mockMvc.perform(MockMvcRequestBuilders.post("/shoe/payment").queryParam("paymentMethod", paymentMethod))

        //then - verify the output
        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun givenPaymentMethodInvalid_shouldExpectBadRequest() {

        //given
        val paymentMethod = "invalid"

        //when
        val resultActions =
            mockMvc.perform(MockMvcRequestBuilders.post("/shoe/payment").queryParam("paymentMethod", paymentMethod))

        //then - verify the output
        resultActions.andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

}