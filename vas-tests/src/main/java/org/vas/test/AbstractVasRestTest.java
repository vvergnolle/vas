package org.vas.test;

import javax.inject.Inject;

import org.vas.test.rest.Rest;
import org.vas.test.rest.VasRest;

public class AbstractVasRestTest extends AbstractVasServerTest {
	
	@Inject
	protected VasRest vasRest;
	
	@Inject
	protected Rest rest;
}