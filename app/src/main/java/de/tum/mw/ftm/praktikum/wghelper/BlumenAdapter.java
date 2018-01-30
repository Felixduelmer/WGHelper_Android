package de.tum.mw.ftm.praktikum.wghelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class BlumenAdapter extends ArrayAdapter<String>  {
    private Context context;
    private int resource, i=0;
    private List<String> blumen;
    private Button water;

    BlumenAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.blumen = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(resource, parent, false);
        final TextView blumenName = layout.findViewById(R.id.blumenname);
        blumenName.setText(blumen.get(position));


        Button delete = layout.findViewById(R.id.delete_btn);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blumen.remove(position);
                notifyDataSetChanged();
            }
        });

        water = layout.findViewById(R.id.timerbtn);
           water.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (i < 1) {
                        CountDownTimer countDownTimer = new CountDownTimer(5000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                water.setText("Restzeit: " + millisUntilFinished / 1000);
                            }

                            @Override
                            public void onFinish() {
                                alertView("A Glas Wasser wär it schlecht!");
                            }

                        };

                    countDownTimer.start();
                        notifyDataSetChanged();
                    }

                    /*else{

                        countDownTimer.cancel();
                        countDownTimer = new CountDownTimer(5000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                water.setText("Restzeit: " + millisUntilFinished / 1000);
                            }

                            @Override
                            public void onFinish() {
                                alertView("A Glas Wasser wär it schlecht!");
                            }
                        };
                        countDownTimer.start();
                        i++;
                        notifyDataSetChanged();
                    }*/
                }
            });

           return layout;
    }

    private void alertView(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setTitle("Wasser aber zackig")
                .setMessage(message)

//              .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//              public void onClick(DialogInterface dialoginterface, int i) {
//                      dialoginterface.cancel();}})

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                    }
                }).show();

    }


}

