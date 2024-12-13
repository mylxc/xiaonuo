package com.jackli.common.consts;


/**
 * @description: 业务常量
 * @author: luoxingcheng
 * @created: 2023/10/10 10:43
 */
public class BusinessConstant {

    /******************设备对应的设备类型id*********************/
    // 加工设备对应的设备类型值
    public final static Integer MCCHINING_CENTER_MACHINING_CENTER_CODE = 1;

    // 设备是装卸站的类型
    public final static Integer UNLOADING_STATION_EQPT_TYPE_CODE = 2;

    // 机械手(搬运机构)对应类型code值
    public final static Integer POSITION_MANIPULATOR_CODE = 3;

    // 检测设备对应设备类型的code
    public final static Integer INSPECTION_EQUIPMENT_CODE = 4;

    // 清洗设备 对应设备类型code
    public final static Integer ClEANING_EQUIPMENT_CODE = 5;

    // 打标设备对应设备类型code
    public final static Integer MARKING_EQUIPMENT_CODE = 6;

    /**********************************************************/

    /*******************刀具预警值设置****************************/

    public final static Float TOOL_ALARM_NUM = 10.0F;

    // 设备类型是机械手类型的子类型是仓储机械手值
    public final static Integer MANIPULATOR_TYPE_SUB_TYPE_ID = 1;

    // 中央刀库机械手
    public final static Integer CENTRAL_TOOL_TYPE_SUB_TYPE_ID = 2;

    //天轨机械手
    public final static Integer CEILING_RAIL_TYPE_SUB_TYPE_ID = 3;

    /*********************************************************/

    /**********************设备识别码***************************/

    // 料仓识别码
    public final static String SILO_IDENTIFICATION_CODE = "L001-LC-01";


    /********************托盘类型*******************************/
    /**
     * 550托盘类型对应id值
     */
    public final static Long PALLET_TYPE_550_ID = 2L;

    // 450托盘类型对应id值
    public final static Long PALLET_TYPE_450_ID = 1L;

    /**********************设备运行状态值*************************/

    // 机床异常状态
    private final static String EQPT_STATE_EXCEPTION = "9";


    /**********************************************************/

    // 储位类型： 料仓储位对应的code码
    public final static Integer POSITION_SILO_CODE = 1;

    // 设备类型是单工作台类型
    public final static String WORK_TABLE_CODE_WT001 = "WT001";

    // 设备类型是双工作台类型
    public final static String WORK_TABLE_CODE_WT002 = "WT002";

    // 设备类型是双工作台类型(机床缓存台类型储位)
    public final static int CACHE_POSITION_TYPE = 4;

    // 设备类型是双工作台类型时， 储位类型需要有一个是 机床工作台（机床内加工工作台） ，现对应的数据是5
    public final static int WHPOSITION_TYPE = 5;

    //机床工作台
    public final static Integer PROCESSING_TABLE_CODE = 5;

    // 储位初始化后的初始状态（默认是0）
    public final static Integer WHPOSITION_StATE_INITIAlIZATION = 0;

    // 表示异常状态
    public final static Integer EXCEPTION_STATE = 9;

    //  机床加工状态 key 对应的未加工状态 0
    public final static String CNC_WORK_STATE_NOT_WORK = "0";

    // 机床加工状态 key 对应的加工中状态 0
    public final static String CNC_WORK_STATE_WORKING = "1";

    //  机床加工状态 key 对应的加工结束状态
    public final static String CNC_WORK_STATE_WORK_FINISH = "2";

    // 机床运行状态 key ,对应未运行状态
    public final static String CNC_RUN_STATE_NOT_RUN = "0";

    // 机床运行状态 key ,对应运行中状态
    public final static String CNC_RUN_STATE_RUNING = "1";

    // 设备在线/离线设置标记字段
    public final static String MACHINE_CENTER_ON_LINE = "MACHINE_CENTER_ON_LINE";

    // 1表示已经存在
    public final static String IFEXIST = "1";

    // 上料工步id
    public final static Long LOAD_WORK_STEP_ID = 11L;

    // 下料工步id
    public final static Long UN_LOAD_WORK_STEP_ID = 12L;

    /**
     *  入库工步id
     */
    public final static Long PUT_IN_STORAGE_WORK_STEP_ID = 13L;

    /**
     *  加工工步功能码对应id
     */
    public final static Long WORK_WORK_STEP_FUN_ID = 1L;

    /**
     *  重新装夹工步功能码对应id
     */
    public final static Long REINSTALL_CLAMP_WORK_STEP_FUN_ID = 2L;

    /**
     *  上料工步功能码对应id
     */
    public final static Long LOAD_WORK_STEP_FUN_ID = 4L;

    /**
     *  下料工步功能码对应id
     */
    public final static Long UN_LOAD_WORK_STEP_FUN_ID = 5L;

    /**
     *  入库工步功能码对应id
     */
    public final static Long PUT_IN_STORAGE_WORK_STEP_FUN_ID = 3L;


    /*****************现场料架行列设置以及排列方式*************************/
    /**
     * 数据字典配置 行数据字段名
     */
    public final static String ROW_NUMBER = "rowNumber";

    /**
     * 数据字典配置 列数据字段名
     */
    public final static String COLUMN_NUMBER = "columnNumber";

    /**
     * 数据字典配置 ,排列方向
     */
    public final static String SOLIT_DIRECTION_FLAG = "solitdirectionflag";

    // 楚天现场料架 行数设置
    public final static Integer ROW_NUMBER_VALUE = 3;

    // 楚天现场料架 行数设置
    public final static Integer COLUMN_NUMBER_VALUE = 17;

    // 楚天现场料架 排列方式值
    public final static String SOLIT_DIRECTION_FLAG_VALUE = "3";

    /****************************************************************/


    /**
     * 楚天： 程序同步--主程序对应自定义类型code
     */
    public final static String PROGRAM_TYPE_CODE = "1000";

    /**
     *  程序版本
     */
    public final static String PROGRAM_VERSION = "V1.0";

    /**
     *  一分钟60秒
     */
    public final static Integer SIXTY_SECOND = 60;

    /**
     *  刀具类型（同步刀具对应类型id）
     */
    public final static Long CT_TOOLS_TYPE_ID = 1L;

    /**
     *  刀具大小类型，默认正常类型的对应code
     */
    public final static String TOOL_SBSKIND_CODE = "1001";

    /**
     *  默认加工工序code
     */
    public final static String DEFAULT_PROCESS_CODE = "1001";

    /**
     *  默认加工工序名称
     */
    public final static String DEFAULT_PROCESS_NAME = "OP10";

    /**
     *  创建同步工步code前缀
     */
    public final static String WORK_STEP_CODE_PREFIX = "SY_00";

    /**
     * 默认产线 id = 1
     */
    public static final Long DEFAULT_LOCATION_ID = 1739129319740252162L;

    /**
     *  工步code自增号对应的key
     */
    public static final String WORK_STEP_CODE_NUMBER_KEY = "WORK_STEP_CODE_NUMBER_INCREMENT";

    /**
     *  上下料 节拍默认2分钟
     */
    public static final Integer LOAD_UNLOAD_DEFAULT_BEAT_TIME = 2;

    /**
     *  文件路径分割符号
     */
    public static final String FILE_SEPARATOR = "/";

    /**
     *  物料流水号对应key
     */
    public static final String SERIAL_MATERIAL_NUM = "SERIAL_MATERIAL_NUM";

    /**
     *  物料流水号位数
     */
    public static final int SERIAL_MATERIAL_NUM_DIGIT = 5;

    /**
     *  物料类型的父类型
     */
    public static final Long PARENT_MATERIAL_TYPE_ID = 1L;

    /**
     *  ftp 前置默认路径
     */
    public static final String STORE_PATH_PREFIX = "D:/defaultUploadFolder/defaultBucketName";

    /**
     *  自动线code
     */
    public static final String AUTO_PROD_LINE_CODE = "L001";

    /**
     * 刀具寿命预警阈值数， 单位分
     */
    public static final Integer WARN_TOOL_SECOND_NUM = 30 ;

    /**
     *  远程调用超时时间设置(单位: 毫秒)
     */
    public static final Integer HTTP_CONNECT_TIME_OUT = 5000;

    /**
     * http数据读取超时设置(单位: 毫秒)
     */
    public static final Integer  HTTP_READ_TIME_OUT = 10000;


    /*******************机床加工状态设置********************/
    // 机床加工状态 key
    public final static String CNC_WORK_STATE = "CNC_WORK_STATE";


    // 机床运行状态 key
    public final static String CNC_RUN_STATE = "CNC_RUN_STATE";


}
