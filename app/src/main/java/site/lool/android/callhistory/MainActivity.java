package site.lool.android.callhistory;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import site.lool.android.callhistory.pojo.CallItem;
import site.lool.android.callhistory.utils.DateHelper;
import site.lool.android.callhistory.utils.PermissionHelper;

import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //region 成员变量
    CallItem callItem = new CallItem();
    private static final int PICK_CONTACT = 0;
    TextView text_name;
    TextView text_phone;
    DatePickerDialog datePicker;
    TimePickerDialog timePicker;
    TextView text_date;
    Button btn_date;
    Button btn_time;
    TextView text_time;
    EditText editText_lastTime;
    Button btn_write;
    Button btn_selectContact;
    RadioGroup radioGroup;
    //endregion
    //选择时间弹窗实现
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initMember();

        String[]mPermission = new String[]{Manifest.permission.READ_CALL_LOG,Manifest.permission.WRITE_CALL_LOG,Manifest.permission.READ_CONTACTS};
        if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, mPermission[1])
                        != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, mPermission[2])
                        != PackageManager.PERMISSION_GRANTED
                ){
            ActivityCompat.requestPermissions(this,
                    mPermission, 1);
        }


    }
    //region 初始化区
    private void initMember(){
        //选择日期弹窗实现
        btn_date = (Button)findViewById(R.id.btn_date);
        text_date = (TextView)findViewById(R.id.text_date);
        btn_time = (Button)findViewById(R.id.btn_time);
        text_time = (TextView)findViewById(R.id.text_time);
        editText_lastTime = (EditText)findViewById(R.id.editText_lastTime);
        btn_write = (Button)findViewById(R.id.btn_write);
        //联系人姓名，电话信息获取
        text_name = (TextView)findViewById(R.id.text_name);
        text_phone = (TextView)findViewById(R.id.text_phone);
        btn_selectContact = (Button)findViewById(R.id.btn_selectContact);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup_);
       initDatePicker();
       initTimePicker();
        initCheckRaio();
    }

    private void initDatePicker(){
        //初始化 DatePicker
        Map<String, Integer> date_year_month_day = DateHelper.date_year_month_day();
        int year= date_year_month_day.get("year");
        int month= date_year_month_day.get("month");
        int day = date_year_month_day.get("day");
        DatePickerDialog.OnDateSetListener DatePickerListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                //Toast.makeText(MainActivity.this,year+"年"+month+"月"+day+"日",Toast.LENGTH_LONG).show();
                //记录信息
                callItem.year = year;callItem.month = month+1; callItem.day = day;
                //显示选择后的日期
                text_date.setText(year+"年"+callItem.month+"月"+day+"日");
            }
        };

        datePicker = new DatePickerDialog(MainActivity.this, DatePickerListener, year, month, day);
    }
    private void initTimePicker(){
        TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener(){
            //当选择时间后
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                //Toast.makeText(MainActivity.this,hourOfDay+"时"+minute+"分",Toast.LENGTH_LONG).show();
                callItem.hour = hourOfDay;callItem.minute = minute;
                text_time.setText(hourOfDay+"时"+minute+"分");
            }
        };
        timePicker = new TimePickerDialog(MainActivity.this,timeListener,0,0,true);
    }
    //endregion

    //region 响应区
    private void initCheckRaio(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int Checkid) {
                RadioButton radio_check = findViewById(radioGroup.getCheckedRadioButtonId());

                if(radio_check.getText().equals("呼入")){
                    callItem.type = 1;
                }else{
                    callItem.type = 2;
                }
                //Toast.makeText(MainActivity.this,radio_check.getText(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void chooseContact(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, PICK_CONTACT);
    }
    public void showDatePicker(View view){
        datePicker.show();
    }
    public void showTimePicker(View view){
        timePicker.show();
    }
    public void writeCallLog(View view){
        //获取通话时长
        callItem.callLastTIme = Integer.parseInt(editText_lastTime.getText().toString());
        //转换时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy.MM.dd");
        Date date = new Date();
        try {
            date = df.parse(callItem.year+"."+(callItem.month)+"."+callItem.day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //年月日转换为长整形
        long time_long = date.getTime();
        //时分秒转换为长整形
        long hour_long = callItem.hour*60*60*1000;
        long minute_long = callItem.minute*60*1000;
        //获取通话类型

        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.CACHED_NAME, callItem.name);
        values.put(CallLog.Calls.NUMBER, callItem.phone);
        values.put(CallLog.Calls.DATE, time_long+ hour_long+minute_long);
        values.put(CallLog.Calls.DURATION, callItem.callLastTIme);//通话时长（响铃时长）以秒为单位 1分30秒则输入90
        values.put(CallLog.Calls.TYPE, callItem.type);//通话类型  1呼入 2呼出 3未接
        values.put(CallLog.Calls.NEW, 0);//isNew0已看1未看
        values.put(CallLog.Calls.PHONE_ACCOUNT_ID,1);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_CALL_LOG}, 1000);
        }
        getContentResolver().insert(CallLog.Calls.CONTENT_URI, values);
        Toast.makeText(MainActivity.this,"添加通讯记录成功",Toast.LENGTH_LONG).show();
        finish();
    }
    //endregion
    
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        String contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        String phoneNumber = null;
                        //cz
                        callItem.name = name;
                        if (hasPhone.equalsIgnoreCase("1")) {
                            hasPhone = "true";
                        }
                        else {
                            hasPhone = "false";
                        }
                        if (Boolean.parseBoolean(hasPhone)) {
                            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                                            + contactId,
                                    null,
                                    null);
                            while (phones.moveToNext()) {
                                phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                //cz
                                callItem.phone = phoneNumber.replaceAll("[ -]", "");
                            }
                            //cz
                            text_name.setText(callItem.name);
                            text_phone.setText(callItem.phone);
                            //Toast.makeText(MainActivity.this,callItem.name+":"+callItem.phone,Toast.LENGTH_LONG).show();
                            phones.close();

                        }


                    }
                }
                break;
        }
    }
}