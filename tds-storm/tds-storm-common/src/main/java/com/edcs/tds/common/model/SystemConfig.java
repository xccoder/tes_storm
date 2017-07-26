package com.edcs.tds.common.model;

public class SystemConfig {
	
	public static final String SUBCHANNEL_NAME = "pvSubChannelData";//子通道的名称前缀
	/*工步名称开始*/
	public static final String HOLD= "搁置";
	public static final String CONSTANT_CURRENT_DISCHARGE = "恒流放电";
	public static final String CONSTENT_CURRENT_CHARGE = "恒流充电";
	public static final String CONSTANT_VOLTAGE_CHARGE = "恒压充电";
	public static final String CONSTANT_CURRENT_VOLTAGE_CHARGE = "恒流恒压充电";
	public static final String CONSTANT_POWER_CHARGE = "恒功率充电";
	public static final String CONSTANT_POWER_DISCHARGE = "恒功率放电";
	public static final String CONSTANT_RESISTANCE_DISCHARGE = "恒阻放电";
	public static final String SIMULATION_WORKSTEP_CURRENT = "模拟工步（电流模式）";
	public static final String SIMULATION_WORKSTEP_POWER = "模拟工步（功率模式）";
	/*工步名称结束*/
	public static final String SITE = "2001";
	/*邮件通知配置-开始*/
	public static final String MY_EMAIL_ACCOUNT = "TDS-Admin@catlbattery.com";
    public static final String MY_EMAIL_PASSWORD = "Aa123456";
    public static final String MY_EMAIL_SMTPHOST = "Mail.catlbattery.com";
    public static final boolean IS_BOOEAN_SSL = false;
    /*邮件通知配置-结束*/
	public static final String URL = "http://172.26.66.35:50000/tes-backing/api/v1/integration/storm/md_process_info";
	public static final String SEQUENCEID = "sequenceId_";//维护sequenceId redis中对应的key
	public static final String BUSINESSCYCLE = "businessCycle_";//维护 业务循环号 redis 中对应的key
	public static final String STEPLOGICNUMBER = "stepLogicNumber_";//维护工步的逻辑序号 redis 中对应的key
	public static final String RESULTDATA = "resultData_";//维护测试结果数据，用于查询上一条数据使用，会定期清理   redis 中对应的key
//	public static final String 
//	public static final String 
//	public static final String 
//	public static final String 
//	public static final String 
//	public static final String 
//	public static final String 
//	public static final String 
//	public static final String 
//	public static final String 
//	
//	public static final String 
//	public static final String 
//	public static final String 
//	public static final String 
//	public static final String 
//	public static final String 
//	public static final String 
	

}
