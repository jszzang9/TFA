package com.marlboro.core.protocol.handler;

import com.marlboro.core.model.dto.TestData;
import com.marlboro.core.model.manager.TestDataManager;
import com.marlboro.core.protocol.request.RequestTEST0003;
import com.marlboro.core.protocol.response.ResponseTEST0003;
import com.marlboro.exception.MarlboroException;
import com.marlboro.exception.NoDataException;

public class HandlerTEST0003 extends Handler<RequestTEST0003, ResponseTEST0003>{

	@Override
	public void handle() throws MarlboroException {
		TestDataManager dataManager = new TestDataManager();
		TestData testData = dataManager.getTestData(getRequest().getMarlboroId(), getRequest().getMarlboroNumber());
		if (testData == null)
			throw new NoDataException("Miss User");
		dataManager.deleteArticle(getRequest().getMarlboroId(), getRequest().getMarlboroNumber());
	}
}
