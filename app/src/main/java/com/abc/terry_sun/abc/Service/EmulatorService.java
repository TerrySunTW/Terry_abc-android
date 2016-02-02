package com.abc.terry_sun.abc.Service;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.terry_sun.abc.Entities.DB_Cards;
import com.abc.terry_sun.abc.MainActivity;
import com.abc.terry_sun.abc.R;
import com.beardedhen.androidbootstrap.BootstrapButton;

import net.glxn.qrgen.android.QRCode;

import java.util.Calendar;


/**
 * Created by terry_sun on 2015/8/25.
 */
public class EmulatorService {
    static String TAG="EmulatorService";
    static Dialog EmulatorDialog;
    private static final EmulatorService _EmulatorService = new EmulatorService();
    public static EmulatorService getInstance() {
        return _EmulatorService;
    }
    static int QR_Default_Height=0;
    static int QR_Default_Width=0;
    static boolean IsBiggerQR=false;
    public void ShowEmulatorDialog(final String EntityCardID)
    {
        Context context= MainActivity.GetMainActivityContext();
        EmulatorDialog=new Dialog(context);
        final DB_Cards SelectedCardInfo=CardService.getInstance().GetCardsByEntityCardID(EntityCardID);

        EmulatorDialog.setContentView(R.layout.dialog_emulator);
        Window window = EmulatorDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.setLayout(ScreenService.GetScreenWidth(context).x - 100, ScreenService.GetScreenWidth(context).y - 300);


        LinearLayout LinearLayout_Background =(LinearLayout)EmulatorDialog.findViewById(R.id.LinearLayout_Background);
        LinearLayout_Background.setBackground(new BitmapDrawable(CardService.getInstance().GetCardImageByCardID(SelectedCardInfo.getCardID())));

        BootstrapButton Button_Exit=(BootstrapButton)EmulatorDialog.findViewById(R.id.Button_Exit);
        Button_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmulatorDialog.dismiss();
            }
        });
        TextView TextView_Info=(TextView)EmulatorDialog.findViewById(R.id.TextView_Info);
        TextView_Info.setText("Your phone is now Emu as " + SelectedCardInfo.getCardName());


        final ImageView ImageView_QR=(ImageView)EmulatorDialog.findViewById(R.id.ImageView_QR);

        QR_Default_Height=ImageView_QR.getLayoutParams().height;
        QR_Default_Width=ImageView_QR.getLayoutParams().width;
        ImageView_QR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"ImageViewonClick!!");
                if (IsBiggerQR) {
                    ImageView_QR.getLayoutParams().height = QR_Default_Height;
                    ImageView_QR.getLayoutParams().width = QR_Default_Width;
                    IsBiggerQR=false;
                } else {
                    ImageView_QR.getLayoutParams().height = QR_Default_Height * 3;
                    ImageView_QR.getLayoutParams().width = QR_Default_Width * 3;
                    IsBiggerQR=true;
                }
                ImageView_QR.requestLayout();
            }
        });

        Long TimeStamp = System.currentTimeMillis()/1000;
        Bitmap myBitmap = QRCode.from(String.valueOf(TimeStamp)+","+SelectedCardInfo.getUserCardID()).bitmap();
        ImageView_QR.setImageBitmap(myBitmap);

        EmulatorDialog.show();
    }
}
