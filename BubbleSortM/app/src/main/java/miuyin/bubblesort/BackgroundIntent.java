package miuyin.bubblesort;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class BackgroundIntent extends IntentService {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String TAG = "BackgroundIntent";

    public BackgroundIntent() {
        super(BackgroundIntent.class.getName());
    }



    static final int MAX_VALUE = 100000;
    static int[] data = new int[MAX_VALUE];


    public void generarNuevoArreglo() {
        Random random = new Random();

        for (int i = 0; i < data.length; i++){
            data[i] = random.nextInt(MAX_VALUE);
        }
    }

    public static void Bubble(){
        BubbleSorter(data);
    }

    public static void BubbleSorter(int[] num)
    {
        int j;
        boolean flag = true;   // set flag to true to begin first pass
        int temp;   //holding variable

        while ( flag )
        {
            flag= false;    //set flag to false awaiting a possible swap
            for( j=0;  j < num.length -1;  j++ )
            {
                if ( num[ j ] < num[j+1] )   // change to > for ascending sort
                {
                    temp = num[ j ];                //swap elements
                    num[ j ] = num[ j+1 ];
                    num[ j+1 ] = temp;
                    flag = true;              //shows a swap occurred
                }
            }
        }

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Service Started!");

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");

        Bundle bundle = new Bundle();

 /* Service Started */
        receiver.send(STATUS_RUNNING, Bundle.EMPTY);
        try {

            long start = System.nanoTime();
            generarNuevoArreglo();
            Bubble();
            long end = System.nanoTime();

            long elapsedTime = end - start;
            String res = "trascurrieron: " + elapsedTime + "nano seconds\n" +
                    "lo cual es " + TimeUnit.NANOSECONDS.toSeconds(elapsedTime) + " segundos";

            bundle.putString("exectime",res);
        }
        catch (Error err)
        {
            bundle.putString(Intent.EXTRA_TEXT, err.getMessage());
            receiver.send(STATUS_ERROR, bundle);
        }

        /* Status Finished */
        receiver.send(STATUS_FINISHED, bundle);
        Log.d(TAG, "Service Stopping!");
        this.stopSelf();
    }
}