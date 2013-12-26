package com.marlboro.core.protocol.handler;

import com.marlboro.core.model.dto.TestData;
import com.marlboro.core.model.manager.TestDataManager;
import com.marlboro.core.protocol.request.RequestTEST0002;
import com.marlboro.core.protocol.response.ResponseTEST0002;
import com.marlboro.exception.MarlboroException;
import com.marlboro.exception.NoDataException;

public class HandlerTEST0002 extends Handler<RequestTEST0002, ResponseTEST0002>{
	
	@Override
	public void handle() throws MarlboroException {

			TestDataManager dataManager = new TestDataManager();
			TestData testData = dataManager.getTestData(getRequest().getMarlboroId(), getRequest().getMarlboroNumber());
			if (testData == null)
				throw new NoDataException("testData");

			dataManager.updateTestData(getRequest().getMarlboroId(), getRequest().getMarlboroNumber(), getRequest().getNewPassWord());
			getResponse().setNewPassWord(getRequest().getNewPassWord());
	}
}
