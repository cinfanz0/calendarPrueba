package calendar.academiamoviles.com.calendar;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyService extends Service {
    MyTask myTask;
    private final int NOTIFICATION_ID = 1010;
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "Servicio creado!", Toast.LENGTH_SHORT).show();
        myTask = new MyTask();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myTask.execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Servicio destruido!", Toast.LENGTH_SHORT).show();
        myTask.cancel(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void triggerNotification(){

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.descarga, "Nuevo mensaje!", System.currentTimeMillis());

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.img_notification, R.drawable.descarga);
        contentView.setTextViewText(R.id.txt_notification, "Notificacion Personalizada, aca va el texto que deseas enviar al usuario");

        notification.contentView = contentView;

        Intent notificationIntent = new Intent(this, MainActivity2Activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.contentIntent = contentIntent;

        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private class MyTask extends AsyncTask<String, String, String> {

        private DateFormat dateFormat;
        private String date;
        private boolean cent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dateFormat = new SimpleDateFormat("HH:mm:ss");
            cent = true;
        }

        @Override
        protected String doInBackground(String... params) {
            while (cent){
                date = dateFormat.format(new Date());
                try {
                    publishProgress(date);
                    triggerNotification();
                    //aqui prueba 2
                    //otra linea
                    // Stop 5s
                    Thread.sleep(10000); // 10segundos
                    //Toast.makeText(getApplicationContext(), "aqui sigue dando vuelta", Toast.LENGTH_SHORT).show();
                    Log.d(null,"por aqui dara vueltas",null);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            Toast.makeText(getApplicationContext(), "Hora actual: " + values[0], Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            cent = false;
        }
    }
}
