package com.abc.terry_sun.abc.Service;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abc.terry_sun.abc.Entities.Cards;
import com.abc.terry_sun.abc.Entities.Events;
import com.abc.terry_sun.abc.MainActivity;
import com.abc.terry_sun.abc.NFC.NfcStorage;
import com.abc.terry_sun.abc.R;

/**
 * Created by terry_sun on 2015/8/25.
 */
public class EmulatorService {
    static Dialog EmulatorDialog;
    private static final EmulatorService _EmulatorService = new EmulatorService();
    public static EmulatorService getInstance() {
        return _EmulatorService;
    }
    public void ShowEmulatorDialog(final String EntityCardID)
    {
        Context context= MainActivity.GetMainActivityContext();
        EmulatorDialog=new Dialog(context);
        final Cards SelectedCardInfo=CardService.getInstance().GetCardsByEntityCardID(EntityCardID);

        EmulatorDialog.setContentView(R.layout.dialog_emulator);
        Window window = EmulatorDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(0));
        window.setLayout(ScreenService.GetScreenWidth(context).x - 100, ScreenService.GetScreenWidth(context).y - 300);


        LinearLayout LinearLayout_Background =(LinearLayout)EmulatorDialog.findViewById(R.id.LinearLayout_Background);
        LinearLayout_Background.setBackground(new BitmapDrawable(CardService.getInstance().GetCardImageByCardID(SelectedCardInfo.getCardID())));

        Button Button_Exit=(Button)EmulatorDialog.findViewById(R.id.Button_Exit);
        Button_Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmulatorDialog.dismiss();
            }
        });
        TextView TextView_Info=(TextView)EmulatorDialog.findViewById(R.id.TextView_Info);

        EmulatorDialog.show();
        NfcStorage.SetAccount(context, SelectedCardInfo.getEntityCardID());
    }
}
