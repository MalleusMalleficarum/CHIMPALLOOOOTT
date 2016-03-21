package edu.kit.ipd.creativecrowd.readablemodel;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.kit.ipd.chimpalot.jsonclasses.ConfigModelJson;

public final class ConfigModelMock {
	
	private ConfigModelMock() {
	}
	
	/**
	 * Creates a valid config from validconfig.json
	 * @return
	 * @throws Exception
	 */
	public static ConfigModelJson validConfig() throws Exception {
		ObjectMapper jacksonObjectMapper = new ObjectMapper();
		return jacksonObjectMapper.readValue(ConfigModelMock.class.getResourceAsStream("/validconfig.json"),
				ConfigModelJson.class);	

	}
}
