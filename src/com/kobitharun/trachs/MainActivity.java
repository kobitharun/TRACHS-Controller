package com.kobitharun.trachs;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.view.View.OnClickListener;
import com.kobitharun.trachs.MainHelper;

public class MainActivity extends BlunoLibrary {
	
	//Declare Buttons
	private Button btn_tempUp;
	private Button btn_tempDown;
	private Button btn_btConnect;
	private Button btn_btChange;
	private Button btn_modeAuto;
	private Button btn_modeHot;
	private Button btn_modeCold;
	private Button btn_modeOff;
	private Button btn_setSetpoint;
	
	
	private TextView lbl_dynamic_currentTemp;
	private TextView lbl_dynamic_setTemp;
	private TextView lbl_dynamic_seatID;
	private TextView lbl_dynamic_seatStatus;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		onCreateProcess();
		
		serialBegin(115200);  //set the Uart Baudrate on BLE chip to 115200
		btn_setSetpoint = (Button) findViewById(R.id.btn_setSetpoint);
		btn_tempUp = (Button) findViewById(R.id.btn_tempUP);
		btn_tempDown = (Button) findViewById(R.id.btn_tempDown);
		btn_btConnect = (Button) findViewById(R.id.btn_disconnectSeat);
		btn_btChange = (Button) findViewById(R.id.btn_changeSeat);
		btn_modeAuto = (Button) findViewById(R.id.btn_modeAuto);
		btn_modeCold = (Button) findViewById(R.id.btn_modeCold);
		btn_modeHot = (Button) findViewById(R.id.btn_modeHot);
		btn_modeOff = (Button) findViewById(R.id.btn_modeOff);
		
		
		lbl_dynamic_currentTemp = (TextView) findViewById(R.id.dynamic_disp_currentTemp);
		lbl_dynamic_setTemp = (TextView) findViewById(R.id.dynamic_disp_setTemp);
		lbl_dynamic_seatID = (TextView) findViewById(R.id.dynamic_lbl_seatID);
		lbl_dynamic_seatStatus = (TextView) findViewById(R.id.dynamc_lbl_seatStatus);
		
		/**************** Button Listeners *****************/
btn_setSetpoint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				//serialSend(serialSendText.getText().toString());										//Alert Dialog for selecting the BLE device
			}
		});

btn_tempUp.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
	}
});

btn_tempDown.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
	}
});

btn_btConnect.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		buttonScanOnClickProcess();										//Alert Dialog for selecting the BLE device
	}
});

btn_btChange.setOnClickListener(new OnClickListener() {
	
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

		setControllerMode("AUTO");
	
	}
});
		
		
btn_modeCold.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				setControllerMode("COLD");
				}
		});
		
btn_modeHot.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		setControllerMode("HOT");
		}
});

btn_modeOff.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		setControllerMode("OFF");
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
			
			lbl_dynamic_seatStatus.setText("Connected");
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
	
		//************** INSERT JSON CODE ******************//
		
	}

	public void setControllerMode(String function)
	{
		String sendString = "";
		if(function == "AUTO")
		{
			  sendString="mA/n";
		}
		else if (function == "OFF")
		{
			sendString="mO/n";
		}
		else if (function == "HOT")
		{

			sendString="mH/n";
		}
		else if (function == "COLD")
		{
			sendString="mC/n";
		}
		serialSend(sendString);
		
	}
	
}