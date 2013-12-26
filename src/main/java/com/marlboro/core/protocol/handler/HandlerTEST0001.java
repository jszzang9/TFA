package com.marlboro.core.protocol.handler;

import com.marlboro.core.model.dto.TestData;
import com.marlboro.core.model.manager.TestDataManager;
import com.marlboro.core.protocol.request.RequestTEST0001;
import com.marlboro.core.protocol.response.ResponseTEST0001;
import com.marlboro.exception.MarlboroException;

public class HandlerTEST0001 extends Handler<RequestTEST0001, ResponseTEST0001>{

	@Override
	public void handle() throws MarlboroException {
			TestDataManager dataManager = new TestDataManager();

			TestData testData = new TestData(getRequest().getMarlboroId(), getRequest().getMarlboroPw(), getRequest().getMarlboroNumber());
			dataManager.putTestData(testData);
	}
}
