package org.vas.exceptions.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.testng.annotations.Test;
import org.vas.commons.bean.MsgBean;
import org.vas.test.AbstractVasRestTest;
import org.vas.test.rest.Response;

public class TestExceptions extends AbstractVasRestTest {
	
	@Test
	public void itShouldRespondWith501() {
		Response<MsgBean> response = vasRest
			.get("/rest/test/exceptions/nop", MsgBean.class);

		assertThat(response.status).as("The status code should be equals to 501").isEqualTo(501);
	}
}