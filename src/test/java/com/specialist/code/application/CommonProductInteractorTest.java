package com.specialist.code.application;

import com.specialist.code.application.boundaries.output.ICommonProductRegisterGateway;
import com.specialist.code.application.interactors.CommonProductInteractor;
import com.specialist.code.application.model.request.CommonProductRequestModel;
import com.specialist.code.application.model.response.CommonProductResponseModel;
import com.specialist.code.application.presenter.ICommonProductPresenter;
import com.specialist.code.domain.CommonProduct;
import com.specialist.code.domain.factories.ICommonProductFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class CommonProductInteractorTest {

    @Mock
    ICommonProductFactory mockedFactory;

    @Mock
    ICommonProductPresenter mockedPresenter;

    @Mock
    ICommonProductRegisterGateway mockedGateway;

    @BeforeEach
    void Setup(){
    }

    @Test
    void givenValidCommonProductProperties_whenCreate_thenSaveItAndPrepareSuccessView(){
        // ARRANGE
        CommonProductRequestModel requestModel = new CommonProductRequestModel("TestId", "ValidTestName", "Test description", 52.2);

        long timestmap = 1668617824L;
        CommonProduct product = new CommonProduct("001", "ValidName", "Some Description", 25.25d, timestmap);
        Mockito.when(mockedGateway.existsById(requestModel.getId())).thenReturn(false);
        Mockito.when(mockedFactory.create(requestModel.getId(), requestModel.getName(), requestModel.getDescription(), requestModel.getPrice())).thenReturn(product);
        CommonProductResponseModel responseModel = Mockito.verify(mockedPresenter.prepareSuccessView(new CommonProductResponseModel(product.getId(), product.getName(), product.getDescription(), product.getPrice(), String.valueOf(timestmap))), Mockito.times(1));
        CommonProductInteractor interactor = new CommonProductInteractor(mockedPresenter, mockedFactory, mockedGateway);

        // ACT
        interactor.create(requestModel);

        // ASSERT
        Assertions.assertEquals("2022-11-16", responseModel.getCreatedAt());

        Mockito.verify(mockedGateway, Mockito.times(1)).save(product);
        Mockito.verify(mockedGateway, Mockito.times(1)).existsById(requestModel.getId());
    }

    @Test
    void givenExistingProduct_whenCreate_thenPrepareFailView_withAlreadyContainsMessage(){
        // ARRANGE
        CommonProductRequestModel requestModel = new CommonProductRequestModel("TestId", "123", "Test description", 52.2);

        long timestmap = 1668617824L;


        CommonProduct product = new CommonProduct("001", "123", "Some Description", 25.25d, timestmap);
        Mockito.when(mockedGateway.existsById(requestModel.getId())).thenReturn(true);
        Mockito.when(mockedFactory.create(requestModel.getId(), requestModel.getName(), requestModel.getDescription(), requestModel.getPrice())).thenReturn(product);

        String expectedMessage = "Name 123 is not valid";

        // ACT
        Exception failViewException = Assertions.assertThrows(Exception.class, () -> {
            mockedPresenter.prepareFailView(expectedMessage);
        });

        // ASSERT
        Assertions.assertTrue(failViewException.getMessage().contains(expectedMessage));
        Mockito.verify(mockedGateway, Mockito.times(1)).existsById(requestModel.getId());
    }


    @Test
    void given123Name_whenCreate_thenPrepareFailView_withNameInvalidMessage(){

        // ARRANGE
        CommonProductRequestModel requestModel = new CommonProductRequestModel("TestId", "123", "Test description", 52.2);

        long timestmap = 1668617824L;

        CommonProduct product = new CommonProduct("001", "123", "Some Description", 25.25d, timestmap);

        Mockito.when(mockedGateway.existsById(requestModel.getId())).thenReturn(false);
        Mockito.when(mockedFactory.create(requestModel.getId(), requestModel.getName(), requestModel.getDescription(), requestModel.getPrice())).thenReturn(product);

        String expectedMessage = "Name 123 is not valid";

        // ACT
        Exception failViewException = Assertions.assertThrows(Exception.class, () -> {
           mockedPresenter.prepareFailView(expectedMessage);
        });

        // ASSERT
        Assertions.assertTrue(failViewException.getMessage().contains(expectedMessage));
        Mockito.verify(mockedGateway, Mockito.times(1)).existsById(requestModel.getId());
    }

}