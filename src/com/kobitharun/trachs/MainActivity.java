package com.kobitharun.trachs;

import java.nio.ByteBuffer;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothClass.Device;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import com.kobitharun.trachs.MainHelper;
import com.kobitharun.trachs.R.integer;

public class MainActivity extends BlunoLibrary {
	
	//Declare Buttons
	private Button btn_tempUp;
	private Button btn_tempDown;
	private Button btn_btConnect;
	private Button btn_modeAuto;
	private Button btn_modeHot;
	private Button btn_modeCold;
	private Button btn_modeOff;
	private Button btn_setSetpoint;
	private RadioButton radio_autoButton;
	private RadioButton radio_hotButton;
	private RadioButton radio_coldButton;
	private RadioButton radio_offButton;
	
	private TextView lbl_dynamic_currentTemp;
	private TextView lbl_dynamic_setTemp;
	private TextView lbl_dynamic_seatID;
	private TextView lbl_dynamic_seatStatus;
	
	public static String deviceName="";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		onCreateProcess();
		
		serialBegin(115200);  //set the Uart Baudrate on BLE chip to 115200
		btn_setSetpoint = (Button) findViewById(R.id.btn_setSetpoint);
		btn_tempUp = (Button) findViewById(R.id.btn_tempUp);
		btn_tempDown = (Button) findViewById(R.id.btn_tempDown);
		btn_btConnect = (Button) findViewById(R.id.btn_disconnectSeat);
		btn_modeAuto = (Button) findViewById(R.id.btn_modeAuto);
		btn_modeCold = (Button) findViewById(R.id.btn_modeCold);
		btn_modeHot = (Button) findViewById(R.id.btn_modeHot);
		btn_modeOff = (Button) findViewById(R.id.btn_modeOff);
		radio_autoButton = (RadioButton) findViewById(R.id.btn_radio_auto);
		radio_hotButton = (RadioButton) findViewById(R.id.btn_radio_hot);
		radio_coldButton = (RadioButton) findViewById(R.id.btn_radio_cold);
		radio_offButton = (RadioButton) findViewById(R.id.btn_radio_off);
		
		lbl_dynamic_currentTemp = (TextView) findViewById(R.id.dynamic_disp_currentTemp);
		lbl_dynamic_setTemp = (TextView) findViewById(R.id.dyn_disp_setTemp);
		lbl_dynamic_seatStatus = (TextView) findViewById(R.id.dynamc_lbl_seatStatus);
		
		/**************** Button Listeners *****************/
btn_setSetpoint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				setTargetTemp(Integer.parseInt(lbl_dynamic_setTemp.getText().toString()));	
			
			}
		});

btn_tempUp.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		changeSetTemp("UP");
		}
});

btn_tempDown.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		changeSetTemp("DOWN");
		}
});

btn_btConnect.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
	}
});


btn_modeAuto.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		setSystemMode("AUTO");
	
	}
});
		
		
btn_modeCold.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				setSystemMode("COLD");
				}
		});
		
btn_modeHot.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		setSystemMode("HOT");
		}
});

btn_modeOff.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		setSystemMode("OFF");
		}
});

		
	}

	protected void onResume(){
		super.onResume();
		System.out.println("BlUNOActivity onResume");
		onResumeProcess();														//onResume Process by BlunoLibrary
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResultProcess(requestCode, resultCode, data);					//onActivityResult Process by BlunoLibrary
		super.onActivityResult(requestCode, resultCode, data);
	}
	
    @Override
    protected void onPause() {
        super.onPause();
        onPauseProcess();														//onPause Process by BlunoLibrary
    }
	
	protected void onStop() {
		super.onStop();
		onStopProcess();														//onStop Process by BlunoLibrary
	}
    
	@Override
    protected void onDestroy() {
        super.onDestroy();	
        onDestroyProcess();														//onDestroy Process by BlunoLibrary
    }

	@Override
	public void onConectionStateChange(connectionStateEnum theConnectionState) {//Once connection state changes, this function will be called
		switch (theConnectionState) {											//Four connection state
		case isConnected:
			
			lbl_dynamic_seatStatus.setText(deviceName+ " connected");
			btn_btConnect.setText("Disconnect");
			break;
		case isConnecting:
			lbl_dynamic_seatStatus.setText("Connecting");
			btn_btConnect.setText("Connecting");
			break;
		case isToScan:
			lbl_dynamic_seatStatus.setText("Disconnected");
			btn_btConnect.setText("Connect");
			break;
		case isScanning:
			lbl_dynamic_seatStatus.setText("Scanning");
			break;
		case isDisconnecting:
			lbl_dynamic_seatStatus.setText("isDisconnecting");
			btn_btConnect.setText("Connecting");
			break;
		default:
			break;
		}
	}

	@Override
	public void onSerialReceived(String theString) {							//Once connection data received, this function will be called
		// TODO Auto-generated method stub
		//serialReceivedText.append(theString);							//append the text into the EditText
		//The Serial data from the BLUNO may be sub-packaged, so using a buffer to hold the String is a good choice.
		
		processAndroidData(theString);

		//************** INSERT JSON CODE ******************//
		
	}

	public void processAndroidData(String dataString)
	{
		
		
		if(dataString != null){
			try {
			String commandString = dataString.substring(dataString.indexOf("<") + 1);
			
			commandString = commandString.substring(0, commandString.indexOf(">"));
			
			
				if(commandString.contains("update"))
				{
					String valueString = dataString.substring(dataString.indexOf(">") + 1);
					valueString = valueString.substring(0, valueString.indexOf(";"));

					String[] updateValuesStrings = valueString.split(",");
					float update_currentTemp = Float.valueOf(updateValuesStrings[0])/10;
				
					
					lbl_dynamic_currentTemp.setText(String.valueOf(update_currentTemp));
					
					
					int update_targetTemp = Integer.parseInt(updateValuesStrings[1]);
					
					lbl_dynamic_setTemp.setText(String.valueOf(update_targetTemp));
					
					//********* Update Mode Button ***********//
					String mode = "OFF";
					int modeValue = Integer.parseInt(updateValuesStrings[2]);
					
					
					if(modeValue == 1)
					{
						mode = "AUTO";
					}
					else if (modeValue == 2)
					{
						mode = "OFF";
					}
					else if (modeValue == 3)
					{
						mode = "HOT";
					}
					else if (modeValue == 4)
					{
						mode = "COLD";
					}
					setModeRadio(mode);
				}
				else if (commandString.contains("ack"))
				{
					//write code for serial communication acknowledgement
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		
		
		
	}
	
	public void changeSetTemp(String direction)
	{
		int temp = Integer.parseInt(lbl_dynamic_setTemp.getText().toString());
		
		if (direction == "UP")
		{
			
			if(temp < 40)
			{
				temp++;
			}

		}
		else if (direction == "DOWN")
		{
			if(temp > 15)
			{
				temp--;
			}
			
		}
		
		lbl_dynamic_setTemp.setText(String.valueOf(temp));
	}
	
	
	public void setTargetTemp(int temp)
	{
		String commandString = "target_temp";
		int[] tempData = {temp};

		sendToController(commandString, tempData);
	}
	
	public void getSystemUpdate()
	{
		String commandString = "get_update";
		int[] tempData = {0};

		sendToController(commandString, tempData);
	}
	
	public void sendToController(String command, int[] data)
	{
		String sendString = "<"+command+">";

		for (int i = 0; i < data.length; i++) {
			sendString = sendString+ data[i] +",";
		}
		sendString = sendString.substring(0, (sendString.length()-1)) + ";";
		serialSend(sendString);
	}
	
	
	public void setSystemMode(String function)
	{
		
		String commandString = "target_mode";

		int[] modeData = {0};
		
		if(function == "AUTO")
		{
			modeData[0] = 1;
		}
		else if (function == "OFF")
		{
			modeData[0]=2;
		}
		else if (function == "HOT")
		{
			modeData[0]=3;
		}
		else if (function == "COLD")
		{
			modeData[0]=4;
		}
		setModeRadio(function);
		sendToController(commandString, modeData);
	}
	
	
	public void setModeRadio(String mode)
	{
		if(mode == "AUTO")
		{
			radio_autoButton.setChecked(true);
		}
		else if (mode == "OFF")
		{
			radio_offButton.setChecked(true);
		}
		else if (mode == "HOT")
		{
			radio_hotButton.setChecked(true);
		}
		else if (mode == "COLD")
		{
			radio_coldButton.setChecked(true);
		}	
	}
	
}