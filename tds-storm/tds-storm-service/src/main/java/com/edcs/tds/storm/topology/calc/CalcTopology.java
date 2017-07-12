package com.edcs.tds.storm.topology.calc;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import com.edcs.tds.storm.topology.BaseTopology;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.topology.TopologyBuilder;


public class CalcTopology extends BaseTopology {

    private int spoutNum = 1;
    private int calcNum = 1;
    private String topicName = "tds-data-topic";

    @Override
    public String getTopologyName() {
        return "tds-calc";
    }

    @Override
    public String getConfigName() {
        return "tds-calc-topology.xml";
    }

    @Override
    public int getWorkerNumber() {
        return 1;
    }

    @Override
    public void addOption(Options options) {
        options.addOption("spout", true, "spoutParallelism");
        options.addOption("calc", true, "calcParallelism");
    }

    @Override
    public void setupOptionValue(CommandLine cmd) {
        spoutNum = Integer.parseInt(cmd.getOptionValue("spout", "1").trim());
        calcNum = Integer.parseInt(cmd.getOptionValue("calc", "1").trim());
    }

    @Override
    public void createTopology(TopologyBuilder builder) {
        KafkaSpout kafkaSpout = new KafkaSpout(getSpoutConfig(topicName));
        CalcBolt bolt = new CalcBolt();

        builder.setSpout("kafkaSpout", kafkaSpout, spoutNum);
        builder.setBolt("handleBolt", bolt, calcNum).shuffleGrouping("kafkaSpout");
    }

    public static void main(String[] args) throws Exception {
        CalcTopology topology = new CalcTopology();
		topology.run(args);
//        topology.createTopology(new TopologyBuilder());
    }

}
