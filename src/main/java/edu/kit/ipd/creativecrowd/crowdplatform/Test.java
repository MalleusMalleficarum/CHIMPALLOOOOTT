package edu.kit.ipd.creativecrowd.crowdplatform;

import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import edu.kit.ipd.chimpalot.util.GlobalApplicationConfig;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Test {
	static class Toll {
		private int info;
		protected Toll() {
			
		}
		public int getInfo() {
			return info;
		}

		public void setInfo(int info) {
			this.info = info;
		}
		
	}
	public Test() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) throws IllegalInputException, IOException {
		GlobalApplicationConfig.configure(true);
		PyBossaConnection p = new PyBossaConnection();
		//System.out.println(p.testUpdate("http://crowdcrafting.org/api/task/1232221?api_key=0ae3365a-220b-41ce-867c-a3f1cee5bf2e",
		//		"{\"n_answers\":20}"));
			p.extendAssignmentNumber(10, "1232221");	
		
	}
}
