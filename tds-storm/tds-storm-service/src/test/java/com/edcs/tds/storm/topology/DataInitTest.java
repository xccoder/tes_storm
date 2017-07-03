package com.edcs.tds.storm.topology;

import com.edcs.tds.storm.model.TestingMessage;
import com.edcs.tds.storm.service.DataInit;

public class DataInitTest {
	
	public static void main(String[] args) {
		String str = "{"
			  +"\"remark\": \"T3-20170320-1317-24 6454_DCR_25\","
			  +"\"sfc\": \"6454_DCR_25\","
			  +"\"resourceId\": \"FXXX0001\","
			  +"\"channelId\": 1,"
			  +"\"sequenceId\": 1,"
			  +"\"cycle\": 2,"
			  +"\"stepId\": 2,"
			  +"\"stepName\": \"huacheng\","
			  +"\"testTimeDuration\": 300,"
			  +"\"timestamp\": \"2017-03-21 18:07:42\","
			  +"\"svIcRange\": 5.0,"
			  +"\"svIvRange\": 5.0,"
			  +"\"pvVoltage\": 2.0,"
			  +"\"pvCurrent\": 4.0,"
			  +"\"pvIr\": 5.0,"
			  +"\"pvTemperature\": 17.0,"
			  +"\"pvChargeCapacity\": 5.0,"
			  +"\"pvDischargeCapacity\": 5.0,"
			  +"\"pvChargeEnergy\": 5.0,"
			  +"\"pvDischargeEnergy\": 5.0,"
			  +"\"pvDataFlag\": 1,"
			  +"\"pvWorkType\": 2,"
			  +"\"pvSubChannelData1\": {"
			   +"\"sequenceId\": 4,"
			    +"\"cycle\": 1,"
			    +"\"stepId\": 1,"
			    +"\"testTimeDuration\": 1,"
			    +"\"voltage\": 1.0,"
			    +"\"current\": 1.0,"
			    +"\"ir\": 16.0,"
			    +"\"temperature\": 17.0,"
			    +"\"chargeCapacity\": 18.0,"
			    +"\"dischargeCapacity\": 19.0,"
			    +"\"chargeEnergy\": 20.0,"
			    +"\"dischargeEnergy\": 21.0,"
			    +"\"timestamp\": \"2017-03-21 18:07:43\","
			    +"\"dataFlag\": 101,"
			    +"\"workType\": 1"
			  +"},"
			  +"\"pvSubChannelData2\": {"
			   +"\"sequenceId\": 4,"
			   +"\"cycle\": 2,"
			    +"\"stepId\": 1,"
			    +"\"testTimeDuration\": 1,"
			    +"\"voltage\": 1.0,"
			    +"\"current\": 1.0,"
			    +"\"ir\": 16.0,"
			    +"\"temperature\": 17.0,"
			    +"\"chargeCapacity\": 18.0,"
			    +"\"dischargeCapacity\": 19.0,"
			    +"\"chargeEnergy\": 20.0,"
			    +"\"dischargeEnergy\": 21.0,"
			    +"\"timestamp\": \"2017-03-21 18:07:43\","
			    +"\"dataFlag\": 101,"
			    +"\"workType\": 1"
			  +"}}";
		
//		TestingMessage testingMessage = DataInit.initRequestMessage(str);
		System.out.println("111111111");
		}
	
	
	
}
