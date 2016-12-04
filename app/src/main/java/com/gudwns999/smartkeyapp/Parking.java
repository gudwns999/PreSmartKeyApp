package com.gudwns999.smartkeyapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

/**
 * Created by Kim on 2016-11-06.
 */

public class Parking extends Activity implements View.OnClickListener {

    private EditText editTextIPAddress;
    private TextView textViewStatus;
    private Button buttonConnect;
    private Button buttonClose;
    private Button buttonUp;
    private Button buttonLeftTurn;
    private Button buttonRightTurn;
    private Button buttonDown;
    private Button buttonStop;
    private InputMethodManager imm;
    private String server = "192.168.0.241";
    private int port = 8888;
    private Socket socket;
    private OutputStream outs;
    private Thread parkThread;
    public logger logger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parking);

        editTextIPAddress = (EditText)this.findViewById(R.id.editTextIPAddress);
        editTextIPAddress.setText(server);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        textViewStatus = (TextView)this.findViewById(R.id.textViewStatus);
        textViewStatus.setText("TeleOp Client");

        logger = new logger(textViewStatus);

        buttonConnect   = (Button)this.findViewById(R.id.buttonConnect);
        buttonClose     = (Button)this.findViewById(R.id.buttonClose);
        buttonUp        = (Button)this.findViewById(R.id.buttonUp);
        buttonDown      = (Button)this.findViewById(R.id.buttonDown);

        buttonConnect.setOnClickListener(this);
        buttonClose.setOnClickListener(this);
        buttonUp.setOnClickListener(this);
        buttonDown.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        if(arg0 == buttonConnect)
        {
            imm.hideSoftInputFromWindow(editTextIPAddress.getWindowToken(), 0);

            try{
                if(socket!=null)
                {
                    socket.close();
                    socket = null;
                }

                server = editTextIPAddress.getText().toString();
                socket = new Socket(server, port);
                outs = socket.getOutputStream();

                parkThread = new Thread(new parkThread(logger, socket));
                parkThread.start();
                logger.log("Connected");
            } catch (IOException e){
                logger.log("Fail to connect");
                e.printStackTrace();
            }
        }

        if(arg0 == buttonClose)
        {
            imm.hideSoftInputFromWindow(editTextIPAddress.getWindowToken(), 0);

            if(socket!=null)
            {
                exitFromRunLoop();
                try{
                    socket.close();
                    socket = null;
                    logger.log("Closed!");
                    parkThread = null;
                } catch (IOException e){
                    logger.log("Fail to close");
                    e.printStackTrace();
                }
            }
        }

        if(arg0 == buttonUp || arg0 == buttonLeftTurn ||
                arg0 == buttonRightTurn || arg0 == buttonDown || arg0 == buttonStop )
        {
            String sndOpkey = "CMD";

            if(arg0 == buttonUp)      sndOpkey = "Up";
            if(arg0 == buttonDown)      sndOpkey = "Down";

            try{
                outs.write(sndOpkey.getBytes("UTF-8"));
                outs.flush();
            } catch (IOException e){
                logger.log("Fail to send");
                e.printStackTrace();
            }
        }
    }

    void exitFromRunLoop(){
        try {
            String sndOpkey = "[close]";
            outs.write(sndOpkey.getBytes("UTF-8"));
            outs.flush();
        } catch (IOException e) {
            logger.log("Fail to send");
            e.printStackTrace();
        }
    }
}
