package com.edcs.tds.storm.topology;

import com.edcs.tds.storm.topology.calc.CalcTopology;

public class CalcTopologyTest {

	public static void main(String[] args) throws Exception {
		CalcTopology topology = new CalcTopology();
		topology.runForLocal(args);

	}

}
