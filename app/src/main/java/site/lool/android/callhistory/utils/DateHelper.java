package site.lool.android.callhistory.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateHelper {
    public static Date StringToDate(String str_date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(str_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static String DateToString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return sdf.format(date);
    }

    //时间转换为 三部分
    public static Map<String,Integer> date_year_month_day(){
        Calendar calendar = Calendar.getInstance();
        Map<String,Integer> map = new HashMap<>();
        map.put("year",calendar.get(Calendar.YEAR));
        map.put("month",calendar.get(Calendar.MONTH));
        map.put("day",calendar.get(Calendar.DAY_OF_MONTH));
        return map;
    }

    //调整月份
    public static String month_nature_lengthOf2(int month){
        String month_str = month>=9?(month+1)+"":"0"+(month+1);
        return month_str;
    }
    //调整月份
    public static String day_lengthOf2(int day){
        String day_lengthOf2 = day>=9?day+"":"0"+day;
        return day_lengthOf2;
    }

    //组合时间成字符串紧凑型
    public static String date_show(int year, int month, int day){
        return "选择时间为："+year+"年"+month_nature_lengthOf2(month)+"月"+day_lengthOf2(day)+"日";
    }

    // 将时间组合为数据库查询 timeID 形式
    public static String timeID(int year, int month, int day){
        return year+month_nature_lengthOf2(month)+day_lengthOf2(day);
    }

}
