package cn.com.onlinetool.jt809.util;

import cn.com.onlinetool.jt809.bean.BasePacket;
import cn.com.onlinetool.jt809.bean.Message;
import cn.com.onlinetool.jt809.bean.MessageHead;
import cn.com.onlinetool.jt809.constants.JT809Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author choice
 * @description: 公共包工具
 * @date 2018-12-27 17:48
 *
 */
public class PacketUtil {
    private static AtomicInteger serialNumber = new AtomicInteger(1);
    public static short getSerialNumber(){
        return (short)serialNumber.getAndIncrement();
    }



    /**
     * 获取校验码
     * @param packet
     * @return
     */
    public static byte getValidCode(byte[] packet){
        //从命令单元开始到校验码前一个byte
        byte temp = packet[2];
        for (int i = 3; i < packet.length - 1; i++){
            temp ^= packet[i];
        }
        return temp;
    }





    public static String formatDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String time = dateFormat.format(date);
        return time;
    }

    public static byte[] getCurrentTimeBytes(){
        String timeStr = formatDate(new Date());
        return ByteArrayUtil.hexStr2Bytes(timeStr.substring(2));
    }


    /**
     * 校验数据体
     * @return
     */
    public static boolean checkPacket(BasePacket basePacket){
        boolean flag = false;
        return true;
    }

    /**
     * 解析数据头
     * @param realBytes
     * @return
     */
    public static MessageHead parseMsgHead(byte[] realBytes) {

        MessageHead messageHead = new MessageHead();
        messageHead.setMsgLength(ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(realBytes,0,4)));
        messageHead.setMsgSn(ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(realBytes,4,4)));
        messageHead.setMsgId(ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(realBytes,8,2)));
        messageHead.setMsgGnssCenterId(ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(realBytes,10,4)));
        messageHead.setVersionFlag(ByteArrayUtil.subBytes(realBytes,14,3));
        messageHead.setEncrypyFlag(ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(realBytes,17,1)));
        messageHead.setEncrypyKey(ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(realBytes,18,4)));

        return messageHead;
    }

    /**
     * 校验消息头标识
     * @param realBytes
     * @return
     */
    public static boolean checkHeadFlag(byte[] realBytes){
        return JT809Constants.MSG_HEAD == realBytes[0];
    }

    /**
     * 获取数据包完整长度
     * @return
     */
    public static int getPacketLen(byte[] realBytes){
        //完整数据长度 == （数据长度 = 头标识 + 数据头 + 数据体 + 尾标识）+ （CRC = 2）
        return ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(realBytes,1,4)) + 2;
    }

    /**
     * 获取数据体长度
     * @param realBytes
     * @return
     */
    public static int getMsgBodyLen(byte[] realBytes){
        //完整数据长度 == （数据长度 = 头标识 + 数据头 + 数据体 + 尾标识）- 头标识 - 数据头 - CRC - 尾标识
        return ByteArrayUtil.bytes2int(ByteArrayUtil.subBytes(realBytes,1,4)) - 1 - 22 - 2 - 1;
    }

    /**
     * 解析经纬度
     * @param data
     * @return
     */
    public static double parseLonOrLat(byte[] data){
        double r =  ByteArrayUtil.bytes2int(data) / Math.pow(10,6);
        return Double.parseDouble(String.format("%.6f",r));
    }


}
