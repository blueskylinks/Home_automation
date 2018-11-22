package com.blueskylinks.home_automation;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Airfreshner_activity extends AppCompatActivity {

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    BluetoothLeScanner scanner;
    private BluetoothDevice ble_device;
    ScanRecord scan_rec;
    byte sc1[];
    int connect=0;
    String message;
    private BluetoothGatt mGatt;
    Button b1;
    RadioButton R1,R2,R3,r1,r2,r3,r4;
    RadioGroup radioGroup,radioGroup1;

    public static String nrf_service = "00000023-0000-1000-8000-00805f9b34fb";
    public final static UUID NRF_UUID_SERVICE = UUID.fromString(nrf_service);

    public BluetoothGattCharacteristic characteristicTX;

    public static String nrf_tx = "0000b001-0000-1000-8000-00805f9b34fb";
    public final static UUID NRF_UUID_TX = UUID.fromString(nrf_tx);
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airfreshner_activity);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 200);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        radioGroup=findViewById(R.id.RGroup);
        radioGroup1=findViewById(R.id.RGroup1);
        b1=findViewById(R.id.b1);
        R1=findViewById(R.id.R1);
        R2=findViewById(R.id.R2);
        R3=findViewById(R.id.R3);
        r1=findViewById(R.id.r1);
        r2=findViewById(R.id.r2);
        r3=findViewById(R.id.r3);
        r4=findViewById(R.id.r4);
        R1.setTextColor(Color.WHITE);
        R2.setTextColor(Color.WHITE);
        R3.setTextColor(Color.WHITE);
        r1.setTextColor(Color.WHITE);
        r2.setTextColor(Color.WHITE);
        r3.setTextColor(Color.WHITE);
        r4.setTextColor(Color.WHITE);

        R1.setEnabled(false);
        R2.setEnabled(false);
        R3.setEnabled(false);
        r1.setEnabled(false);
        r2.setEnabled(false);
        r3.setEnabled(false);
        r4.setEnabled(false);

        initialize();
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();
        //  startscand();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean initialize() {
        // For API level 18 and above, get a reference to BluetoothAdapter through
        // BluetoothManager.
        Log.i("BleScanning:", "initilizing.......");
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.i("BleScanning:", "Unable to initialize BluetoothManager.");
                return false;
            }

        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.i("BleScanning:", "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            //Bluetooth is disabled
            mBluetoothAdapter.enable();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        startscand();
        return true;
    }

    //================  Start BLE Scanning  ===============
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startscand() {
        Log.i("BLE------", "Start Scanning");
        //final ParcelUuid UID_SERVICE =
        ParcelUuid.fromString("00000023-0000-1000-8000-00805f9b34fb");
        scanner = mBluetoothAdapter.getBluetoothLeScanner();
        ScanFilter beaconFilter = new ScanFilter.Builder() // this filter will be used to get only specific device based on service UUID
                //.setServiceUuid(UID_SERVICE)
                .build();
        ArrayList<ScanFilter> filters = new ArrayList<ScanFilter>();
        filters.add(beaconFilter);
        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();
        scanner.startScan(filters, settings, mcallback);
    }
    // This callback method will be automatically called every time the scanner get the device adv data
    public ScanCallback mcallback = new ScanCallback() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            int rssi;
            rssi = result.getRssi();
            scan_rec = result.getScanRecord();
            Log.i("Scan result", String.valueOf(rssi));
            Log.i("record", scan_rec.toString());
            Toast.makeText(Airfreshner_activity.this, String.valueOf(rssi), Toast.LENGTH_SHORT).show();
            ble_device = result.getDevice();
            sc1 = scan_rec.getManufacturerSpecificData(0);
            /*for (int i = 0; i < sc1.length; i++) {
                Log.i("Data-----:", String.valueOf(sc1[i]));
               // lr[i] = sc1[i];
            }*/
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void stopscand() {
        Log.i("BLE-----", "Stop Scanning");
        scanner.stopScan(mcallback);
    }
    //===========================================================================
    public BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + newState);

            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.i("gattCallback", "STATE_DISCONNECTED");
                    Intent intent = new Intent();
                    intent.setAction("CUSTOM_INTENT");
                    intent.putExtra("D1", "STATE_DISCONNECTED");

                    break;
                default:
                    Log.i("gattCallback", "STATE_OTHER");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            BluetoothGattService service1;
            Log.i("onServicesDiscovered", services.toString());
            service1=gatt.getService(NRF_UUID_SERVICE);
            characteristicTX = services.get(2).getCharacteristic(NRF_UUID_TX);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.i("onCharacteristicRead", characteristic.toString());
            final byte b1[]=characteristic.getValue();
            for (int i=0;i<b1.length;i++){
                Log.i(":",String.valueOf(b1[i]));
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status) {
            Log.i("onCharacteristicWrite??", characteristic.toString());
            final byte b1[]=characteristic.getValue();
            for (int i=0;i<b1.length;i++){
                Log.i(":",String.valueOf(b1[i]));
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void connect_device(View view) {

        String buttontext = (String) b1.getText();
        if (buttontext == "Disconnect") {
            disconnect();
            b1.setText("Connect");
            R1.setEnabled(false);
            R2.setEnabled(false);
            R3.setEnabled(false);
            r1.setEnabled(false);
            r2.setEnabled(false);
            r3.setEnabled(false);
            r4.setEnabled(false);
        } else {
            stopscand();
            if (ble_device != null){
                mGatt = ble_device.connectGatt(this, false, gattCallback);
                Log.i("BLE", "Device Connected...");
                connect=1;
                b1.setText("Disconnect");
                R1.setEnabled(true);
                R2.setEnabled(true);
                R3.setEnabled(true);
                r1.setEnabled(true);
                r2.setEnabled(true);
                r3.setEnabled(true);
                r4.setEnabled(true);
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void disconnect() {
        mGatt.disconnect();
        startscand();
    }

    public void  lb1(View view){
        r1.setHighlightColor(Color.RED);
        if(connect==1){
        message = "a";
        characteristicTX.setValue(message);
        mGatt.writeCharacteristic(characteristicTX);}
        else {
            Toast.makeText(this, "Please Connect with device", Toast.LENGTH_SHORT).show();
        }
    }

    public void  lb2(View view){
        r2.setHighlightColor(Color.RED);
        if(connect==1){
        message = "b";
        characteristicTX.setValue(message);
        mGatt.writeCharacteristic(characteristicTX);}
        else Toast.makeText(this, "Please Connect with device", Toast.LENGTH_SHORT).show();
    }

    public void  lb3(View view){

        r3.setHighlightColor(Color.RED);
        if(connect==1){
        message = "c";
        characteristicTX.setValue(message);
        mGatt.writeCharacteristic(characteristicTX);}
        else Toast.makeText(this, "Please Connect with device", Toast.LENGTH_SHORT).show();
    }

    public void  lb4(View view){
        r4.setHighlightColor(Color.RED);
        if(connect==1){
        message = "d";
        characteristicTX.setValue(message);
        mGatt.writeCharacteristic(characteristicTX);}
        else Toast.makeText(this, "Please Connect with device", Toast.LENGTH_SHORT).show();
    }

    public void  rb1(View view){
       R1.setHighlightColor(Color.RED);
        if(connect==1){
            message = "x";
            characteristicTX.setValue(message);
            mGatt.writeCharacteristic(characteristicTX);}
        else {

            Toast.makeText(this, "Please Connect with device", Toast.LENGTH_SHORT).show();}
    }

    public void  rb2(View view){
        R2.setHighlightColor(Color.RED);
        if(connect==1){

            message = "y";
            characteristicTX.setValue(message);
            mGatt.writeCharacteristic(characteristicTX);}
        else {

            Toast.makeText(this, "Please Connect with device", Toast.LENGTH_SHORT).show();}
    }

    public void  rb3(View view){
        R3.setHighlightColor(Color.RED);
        if(connect==1){

            message = "z";
            characteristicTX.setValue(message);
            mGatt.writeCharacteristic(characteristicTX);}
        else{

            Toast.makeText(this, "Please Connect with device", Toast.LENGTH_SHORT).show();}
    }

}

